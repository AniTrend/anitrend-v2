# Studio Detail Page Design

Date: 2026-05-26
Status: Draft

> **Note:** The `QueryContainerBuilder` examples in this document are historical and superseded.
> Current implementation must use generated `GraphQLOperationRequest` operations built from codegen
> output (operation document, name, and typed variables) as covered in
> `.agents/skills/graphql-query-pattern/SKILL.md`.

## Problem

The studio detail screen (`StudioScreen`) exists as an empty scaffold with a back button.
Tapping a studio from the media detail page's studio section navigates to a blank page.
No data layer, no UI, no cache — only the navigation router and domain models exist.

## Scope

Implement the full Studio detail feature:
- Studio info header (image, name, favourites, siteUrl)
- Horizontal media grid showing media produced by the studio
- Edge enrichment (studio logos via TMDB)
- Offline-first caching
- Standard AniTrend data layer plumbing (controller → cache → source → repository → use case → ViewModel)

Out of scope: staff detail, studio list/browse, studio search.

## Architecture

### Data Flow

```
StudioViewModel
  → StudioDetailInteractor.getStudio(param)       [public typealias]
    → StudioDetailUseCaseImpl(repository)          [extends StudioUseCase<DataState<Studio>>]
      → StudioDetailRepository(source)             [implements IStudioRepository.Detail<DataState<Studio>>]
        → source create source(param)              [DataState.create wraps Flow]
          → StudioDetailSource.invoke(param)        [abstract source contract]
            → cachePolicy(block = ::getStudio)     [checks if refresh needed]
              → StudioDetailSourceImpl.getStudio(callback)
                → remoteSource.getStudioDetail(query)
                → controller(deferred, callback)   [GraphQLController]
                  → client.fetch(deferred)
                  → mapper.onResponseMapFrom(StudioDetailContainer)
                    → StudioModel.ExtractMediaConnection → MediaStudioConnectionEntity list
                    → StudioModel → StudioEntity
                  → mapper.onResponseDatabaseInsert(StudioDetailPersistenceData)
                    → localSource.upsert(StudioEntity)
                    → mediaStudioConnectionLocalSource.upsert(mediaConnections)
                → returns result
            → this.observable()                    [returns Flow<Studio>]
              → StudioDetailSourceImpl.observable()
                → combine(
                    localSource.studioByIdFlow(param.id),
                    mediaStudioConnectionLocalSource.entriesByStudioIdFlow(param.id)
                  ) { studio, connections → ... }
                → map(studioEntityConverter::convertFrom) + EdgeNetwork enricher
```

### New Files

| File | Purpose |
|---|---|
| `domain/.../studio/repository/IStudioRepository.kt` | Repository interface |
| `domain/.../studio/interactor/StudioUseCase.kt` | Abstract use case |
| `data/.../studio/Types.kt` | Typealias hub |
| `data/.../studio/model/remote/StudioDetailContainer.kt` | Flat response container |
| `data/.../studio/source/contract/StudioDetailSource.kt` | Abstract source contract |
| `data/.../studio/source/StudioDetailSourceImpl.kt` | Source implementation |
| `data/.../studio/mapper/StudioDetailMapper.kt` | GraphQL → Room mapper |
| `data/.../studio/repository/StudioDetailRepository.kt` | Repository impl |
| `data/.../studio/usecase/StudioDetailUseCaseImpl.kt` | Use case impl |
| `data/.../studio/cache/StudioCache.kt` | Cache policy |
| `data/.../studio/datasource/remote/StudioDetailRemoteSource.kt` | Retrofit/GraphQL interface |
| `data/.../studio/enricher/StudioEdgeEnricher.kt` | Edge enrichment |
| `feature/.../studio/component/viewmodel/StudioViewModel.kt` | Feature ViewModel |
| `feature/.../studio/component/compose/StudioContent.kt` | Main Compose content |

### Existing Files to Modify

| File | Change |
|---|---|
| `data/.../studio/koin/Modules.kt` | Wire source, repository, use case, mapper, cache modules |
| `data/.../studio/Types.kt` | Add typealiases |
| `feature/.../studio/koin/Modules.kt` | Wire ViewModel |
| `feature/.../studio/component/compose/StudioCompose.kt` | Replace placeholder with real UI |
| `feature/.../studio/component/screen/StudioScreen.kt` | Wire ViewModel |
| `data/.../studio/datasource/local/MediaStudioConnectionLocalSource.kt` | Add `entriesByStudioIdFlow()` |
| `data/.../studio/mapper/StudioMapper.kt` | Add converter method for StudioModel → StudioEntity |

### Layer Details

#### Remote Model

`StudioDetailContainer.kt` — flat container to avoid sealed class deserialization issues:

```kotlin
@Serializable
data class StudioDetailContainer(
    val id: Long = 0,
    val name: String = "",
    val image: String? = null,
    val isAnimationStudio: Boolean = false,
    val siteUrl: String? = null,
    val favourites: Int? = null,
    val isFavourite: Boolean = false,
    val isFavouriteBlocked: Boolean = false,
    val mediaConnection: MediaConnection? = null,
)
```

The GraphQL query returns a single `Studio` object with nested `mediaConnection`. This container maps cleanly from the response body without sealed class complexity.

#### Mapper

`StudioDetailPersistenceData` compound type:

```kotlin
data class StudioDetailPersistenceData(
    val studio: StudioEntity,
    val mediaConnections: List<MediaStudioConnectionEntity>,
)
```

`StudioDetailMapper : DefaultMapper<StudioDetailContainer, StudioDetailPersistenceData>`:

- `onResponseMapFrom()`: extracts `StudioEntity` from container fields + maps `mediaConnection.edges` → `List<MediaStudioConnectionEntity>`
- `persist()`: upserts `StudioEntity` via `studioLocalSource` + upserts `List<MediaStudioConnectionEntity>` via `mediaStudioConnectionLocalSource`

Follows the multi-source pattern validated by the mapper audit (MediaMapper.Detail, MediaListMapper.Entry, etc.).

#### Source

`StudioDetailSource : AbstractCoreDataSource()`:

- `operator fun invoke(param: StudioParam.Detail): Flow<Studio>`
- `observable()`: `combine(studioByIdFlow, entriesByStudioIdFlow).map { ... Studio(core + mediaEntries + edgeLogo) }`
- `getStudio(callback)`: single controller call via remote source

Edge enrichment is applied inside `observable()` via `StudioEdgeEnricher` which matches `Studio.name` against `EdgeNetworkEntity` by name similarity (following the `MediaStudioEntryEnricher` pattern).

#### Cache

`StudioCache : CacheStorePolicy` with `StudioCache.Identity.Detail(id)` — standard identity-based cache per studio ID.

#### Remote Source

`StudioDetailRemoteSource` — new Retrofit/GraphQL interface with:

```kotlin
@GRAPHQL
@POST(IEndpointType.BASE_ENDPOINT_PATH)
@GraphQuery("GetStudioDetail")
suspend fun getStudioDetail(@Body queryContainer: QueryContainerBuilder): Response<GraphQLResponse<StudioDetailContainer>>
```

The operation file `GetStudioDetail.graphql` queries `Studio(id: $id)` with `... StudioFull` fragment plus `mediaConnection { edges { ... } }`.

#### ViewModel

`StudioViewModel : AniTrendViewModelState<Studio>()`:

- Receives `StudioParam.Detail` via `invoke(param)`
- Calls `studioDetailInteractor.getStudio(param)`
- Exposes `state: LiveData<DataState<Studio>>` → `model` and `loadState`

#### Compose UI

Uses bottom app bar (existing `DefaultScaffold` pattern — `StudioCompose.kt` already provides back-button behavior). No top bar, no title bar — the scaffold integrates with the app shell's bottom navigation.

`StudioContent` fills the scaffold body:

```
┌─────────────────────────────────────┐
│                                     │
│           ┌───────────┐             │  ─surface spacer
│           │           │             │
│           │    Logo   │             │  AsyncImage(rounded, 96dp, centered)
│           │           │             │
│           └───────────┘             │
│                                     │
│          MAPPA Inc.                 │  title-large, centered
│                                     │
│   ★ 12,341 Favourites               │  body-medium, muted
│                                     │
│    [  Animation Studio  ]           │  SuggestionChip
│                                     │
│        anilist.co/studio/123        │  URL text (link-style, primary)
│                                     │
├─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤  surface break
│  Media                     See all  │  SectionHeader
│                                     │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──    │  horizontal LazyRow
│  │      │ │      │ │      │ │      │
│  │Poster│ │Poster│ │Poster│ │      │  MediaCardSmall (120w × 168h)
│  │      │ │      │ │      │ │      │
│  │Title │ │Title │ │Title │ │      │  caption, max 2 lines
│  │TV    │ │Movie │ │TV    │ │      │  format chip
│  │2022  │ │2021  │ │2023  │ │      │  year + score
│  └──────┘ └──────┘ └──────┘ └──    │
│                                     │
└─────────────────────────────────────┘
```

**Component Breakdown:**

| Component | Type | Responsibility |
|---|---|---|
| `StudioDetailHeader` | New section composable | Studio image, name, stats, badge, URL |
| `StudioMediaSection` | New section composable | SectionHeader + LazyRow of MediaCardSmall |
| `MediaCardSmall` | Existing (reuse) | Poster + title + format + year + score |
| `AniTrendSectionHeader` | Existing | Title + optional "See all" action |

**State Matrix:**

| State | Header | Media Section |
|---|---|---|
| Loading | Shimmer: circle + 2 lines | Shimmer: 2 horizontal card skeletons |
| Populated with media | Full rendered | LazyRow of MediaCardSmall |
| Populated, no media | Full rendered | "No media found" muted text (centered) |
| Error | Error icon + "Couldn't load studio" + Retry button | Hidden |
| No image URL | Name text in place of image (large initials avatar or just omit) | N/A |

**Design Quality Gates:**

- **Good looks like**: Clean brand page — centered logo feels like a profile, stats are scannable, media section is clearly secondary. Surface break between header and content creates clear hierarchy.
- **Avoid this**: Don't make the header a wall of equally weighted text. Favourites, badge, and URL should have progressively lower emphasis. Don't stretch the logo beyond readable size (max 96dp). Don't let the media row overflow without scroll indication.
- **Contrast risk**: Studio URL text on surface layer — ensure `onSurfaceVariant` meets 4.5:1. Badge chip must be distinguishable from background.
- **Accessibility**: Logo must have content description ("Studio name logo"). Every MediaCardSmall must have its title as content description. Touch targets on the URL and "See all" must be ≥48dp. No icon-only actions without labels.

**Preview Validation Matrix:**

| Variant | Required? | Why |
|---|---|---|
| `AniTrendPreview.Light` | Yes | Baseline layout verification |
| `AniTrendPreview.Dark` | Yes | Contrast and surface layering |
| `AniTrendPreview.Mobile` | Yes | Full-screen surface |
| `AniTrendPreview.Foldable` | No | No width-dependent hierarchy change |
| `AniTrendPreview.Tablet` | No | Same layout scales up |

State samples needed: Loading, Populated (with media), Populated (no media), Error, No image URL.

**Compose Implementation Notes:**

- `StudioDetailContent` takes `DataState<Studio>` and renders each state
- `.verticalScroll` on the outer Column (not LazyColumn — only 2 sections)
- Header uses `Column` with `horizontalAlignment = CenterHorizontally`
- Media section uses `LazyRow` with `contentPadding` for leading/trailing spacing
- Use `Modifier.width(120.dp)` for `MediaCardSmall` to match media detail's studio section
- Studio image: `AsyncImage` from Coil (existing pattern), fallback to placeholder if URL is null
- Badge: `SuggestionChip` or `Surface(shape = RoundedCornerShape)` with muted text
- URL: `ClickableText` with `TextStyle(color = MaterialTheme.colorScheme.primary)`
- SectionHeader: reuse from existing pattern (`Row` + title + trailing action)
- No animations — simple crossfade on DataState transitions
- `PreviewTheme` wrapper for all previews, `DarkThemeProvider` for dark variants

### GraphQL Query

New operation file: `data/src/main/assets/graphql/queries/studio/GetStudioDetail.graphql`

```graphql
query GetStudioDetail($id: Int) {
    Studio(id: $id) {
        id
        name
        image
        isAnimationStudio
        siteUrl
        favourites
        isFavourite
        isFavouriteBlocked
        mediaConnection {
            edges {
                node {
                    id
                    title {
                        userPreferred
                    }
                    coverImage {
                        large
                    }
                    format
                    startDate {
                        year
                    }
                    meanScore
                }
                staffRole
            }
        }
    }
}
```

The existing `StudioFull` fragment should be updated or a new fragment should be composed.
The media node fields follow the existing `MediaStudioConnectionEntity` shape.

### Edge Enrichment

`StudioEdgeEnricher` in `data/edge` (or referenced from `data/studio/enricher/`):

- Takes `StudioEntity` name → queries `EdgeNetworkLocalSource` by name similarity
- Returns `EdgeNetworkEntity?` for logo URL
- Follows the pattern established by `MediaStudioEntryEnricher`
- Called in `StudioDetailSourceImpl.observable()` during the domain model mapping

## States

| State | UI |
|---|---|
| Loading | Shimmer placeholder for header + 2 rows of card skeletons |
| Populated | Header + horizontal media cards |
| Empty | Header only (no media) |
| Error | Full-screen error with retry |
| Offline/cached | Stale data shown with offline indicator (existing DataState behavior) |

## Quality Gates

1. All states render without crash (loading, populated, empty, error)
2. Dark theme contrast: header text on surface layer meets 4.5:1 ratio
3. Media cards match existing card sizing in media detail's studio section
4. Cache hit serves content without network call
5. Cache refresh re-fetches and updates Room
6. Edge enrichment loads logo when available, doesn't crash when absent
7. Back navigation returns to media detail
8. SpotlessApply passes, tests pass

## Implementation Order

1. **Data layer types**: Types.kt, domain repository/use case interfaces
2. **GraphQL query**: GetStudioDetail.graphql operation + fragment updates
3. **Remote source**: StudioDetailRemoteSource
4. **Response container**: StudioDetailContainer
5. **Local source update**: entriesByStudioIdFlow on MediaStudioConnectionLocalSource
6. **Mapper**: StudioDetailMapper + StudioDetailPersistenceData
7. **Cache**: StudioCache
8. **Source**: StudioDetailSource + StudioDetailSourceImpl
9. **Repository + UseCase**: StudioDetailRepository + StudioDetailUseCaseImpl
10. **Koin wiring**: data layer modules
11. **Edge enricher**: StudioEdgeEnricher
12. **ViewModel**: StudioViewModel
13. **Compose UI**: StudioContent (header + media grid)
14. **Feature Koin wiring**: ViewModel module
15. **Format + test**: spotlessApply, unit tests

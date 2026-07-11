# Layer Example Matrix

## How to use this matrix

- Use this file as the concrete example inventory for the split-doc model.
- Keep policy and procedural guidance in the instruction files and skill files.
- Use these anchors when you need a real module path or end-to-end example before making a code
  change or writing a new skill.
- Pair Android-specific work with
  [android-platform-patterns](../../android-platform-patterns/SKILL.md).

## Review spine

Use `review` as the primary end-to-end example when you need one concept that crosses app
navigation, feature UI, shared entry logic, task workers, domain contracts, and data wiring.

```text
app/navigation router
    -> feature/review/discover provider + Koin + ViewModel + Compose route
    -> common/review presenters enqueue task routers
    -> task/review provider + Koin + workers
    -> domain/review params + repository + use case
    -> data/review Types + interactor + repository + source + Koin
    -> data/core GraphQLController
```

### Navigation contract

- `app/navigation/src/main/kotlin/co/anitrend/navigation/NavigationTargets.kt`
  - `ReviewDiscoverRouter`
  - `ReviewTaskRouter`
- `app/navigation/src/main/kotlin/co/anitrend/navigation/extensions/RouterExtensions.kt`

### Feature entry

- `feature/review/discover/src/main/kotlin/co/anitrend/review/discover/provider/FeatureProvider.kt`
- `feature/review/discover/src/main/kotlin/co/anitrend/review/discover/koin/Modules.kt`
- `feature/review/discover/src/main/kotlin/co/anitrend/review/discover/component/content/viewmodel/ReviewDiscoverViewModel.kt`
- `feature/review/discover/src/main/kotlin/co/anitrend/review/discover/component/compose/ReviewDiscoverRoute.kt`

### Common entry

- `common/review/src/main/kotlin/co/anitrend/common/review/ui/compose/ReviewCompose.kt`

### Task entry

- `task/review/src/main/kotlin/co/anitrend/task/review/provider/FeatureProvider.kt`
- `task/review/src/main/kotlin/co/anitrend/task/review/koin/Modules.kt`
- `task/review/src/main/kotlin/co/anitrend/task/review/component/ReviewVoteEntryWorker.kt`
- `task/review/src/main/kotlin/co/anitrend/task/review/component/ReviewSaveEntryWorker.kt`
- `task/review/src/main/kotlin/co/anitrend/task/review/component/ReviewDeleteEntryWorker.kt`

### Domain contract

- `domain/src/main/kotlin/co/anitrend/domain/review/model/ReviewParam.kt`
- `domain/src/main/kotlin/co/anitrend/domain/review/repository/IReviewRepository.kt`
- `domain/src/main/kotlin/co/anitrend/domain/review/interactor/ReviewUseCase.kt`

### Data implementation

- `data/src/main/kotlin/co/anitrend/data/review/Types.kt`
- `data/src/main/kotlin/co/anitrend/data/review/usecase/ReviewInteractor.kt`
- `data/src/main/kotlin/co/anitrend/data/review/repository/ReviewRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/review/source/contract/ReviewSource.kt`
- `data/src/main/kotlin/co/anitrend/data/review/source/ReviewSourceImpl.kt`
- `data/src/main/kotlin/co/anitrend/data/review/source/ReviewPagingSource.kt`
- `data/src/main/kotlin/co/anitrend/data/review/koin/Modules.kt`
- `data/core/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`

## Pattern variants

Use these when `review` is too broad and you need a tighter shape match.

| Shape | Reference | What it proves |
|---|---|---|
| Query-only baseline | `domain/tag` + `data/tag` | Single repository contract, single source, non-paged offline-first read |
| Read-heavy multi-contract | `domain/media` + `data/media` | Multiple read contracts and alias-based interactors against one entity family |
| Hybrid query + mutation | `domain/medialist` + `data/medialist` + `task/medialist` | Shared read and write domain area with task-backed mutations |
| Hybrid fetch + action | `domain/review` + `data/review` + `task/review` | Fetch plus distinct action routes such as vote/save/delete |
| Mutation-only | `domain/favourite` + `data/favourite` + `task/favourite` | Focused toggle/save/delete flow with no local read screen |

### Query-only baseline

- `domain/src/main/kotlin/co/anitrend/domain/tag/repository/ITagRepository.kt`
- `domain/src/main/kotlin/co/anitrend/domain/tag/interactor/TagUseCase.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/Types.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/repository/TagRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/source/contract/TagSource.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/source/TagSourceImpl.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/koin/Modules.kt`
- `data/src/main/kotlin/co/anitrend/data/tag/entity/TagEntity.kt`

### Read-heavy multi-contract

- `domain/src/main/kotlin/co/anitrend/domain/media/repository/IMediaRepository.kt`
- `domain/src/main/kotlin/co/anitrend/domain/media/interactor/MediaUseCase.kt`
- `data/src/main/kotlin/co/anitrend/data/media/Types.kt`
- `data/src/main/kotlin/co/anitrend/data/media/repository/MediaRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/media/source/contract/MediaSource.kt`
- `data/src/main/kotlin/co/anitrend/data/media/source/MediaSourceImpl.kt`
- `data/src/main/kotlin/co/anitrend/data/media/source/MediaPagingSource.kt`
- `data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt`

### Hybrid query plus mutation

- `domain/src/main/kotlin/co/anitrend/domain/medialist/repository/IMediaListRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/medialist/Types.kt`
- `data/src/main/kotlin/co/anitrend/data/medialist/repository/MediaListRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/medialist/source/contract/MediaListSource.kt`
- `data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt`
- `task/medialist/src/main/kotlin/co/anitrend/task/medialist/`

### Mutation-only

- `domain/src/main/kotlin/co/anitrend/domain/favourite/repository/IFavouriteRepository.kt`
- `domain/src/main/kotlin/co/anitrend/domain/favourite/interactor/FavouriteUseCase.kt`
- `data/src/main/kotlin/co/anitrend/data/favourite/Types.kt`
- `data/src/main/kotlin/co/anitrend/data/favourite/repository/FavouriteRepository.kt`
- `data/src/main/kotlin/co/anitrend/data/favourite/source/contract/FavouriteSource.kt`
- `data/src/main/kotlin/co/anitrend/data/favourite/koin/Modules.kt`
- `task/favourite/src/main/kotlin/co/anitrend/task/favourite/`

## Android platform

Treat `:android:*` as the first place to search for reusable Android-side APIs before creating a
feature-local helper or duplicating shell behavior.

### `android/core`

Use `:android:core` for shared Android infrastructure and UI primitives.

- `android/core/src/main/kotlin/co/anitrend/android/core/koin/Modules.kt`
  - Koin bindings for settings, dispatchers, storage, locale/theme/configuration helpers,
    notifications, shortcuts, and power controller
- `android/core/src/main/kotlin/co/anitrend/android/core/settings/helper/config/ConfigurationHelper.kt`
  - activity configuration, locale/theme application, edge-to-edge setup
- `android/core/src/main/kotlin/co/anitrend/android/core/ui/theme/Theme.kt`
  - `AniTrendTheme3`, dynamic color, Material3 theme surface
- `android/core/src/main/kotlin/co/anitrend/android/core/extensions/ContextExtensions.kt`
  - context-to-fragment-manager and lifecycle-owner helpers
- `android/core/src/main/kotlin/co/anitrend/android/core/helpers/notification/NotificationExtensions.kt`
  - notification permission and settings flows
- `android/core/src/main/kotlin/co/anitrend/android/core/storage/StorageController.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/controller/power/AndroidPowerController.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/shortcut/ShortcutController.kt`

### `android/navigation`

Use `:android:navigation` for app-shell navigation infrastructure rather than feature-local
re-creations of drawer behavior.

- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/koin/Modules.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/provider/FeatureProvider.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/component/content/BottomDrawerContent.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/component/presenter/DrawerPresenter.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/component/viewmodel/NavigationViewModel.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/component/viewmodel/AccountViewModel.kt`

### `android/deeplink`

Use `:android:deeplink` for external URI entry and route parsing instead of bypassing the parser
with direct feature intents.

- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/screen/DeepLinkScreen.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/viewmodel/DeepLinkViewModel.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/provider/FeatureProvider.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/route/AppRoutes.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/route/WebRoutes.kt`

## App shell integration

Use these files to see how Android-platform helpers are consumed by the shell.

- `app/core/src/main/kotlin/co/anitrend/core/initializer/injector/InjectorInitializer.kt`
  - starts Koin and loads `coreModules`
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`
  - includes `androidCoreModules` and shared app-core singletons
- `app/core/src/main/kotlin/co/anitrend/core/ui/UiExtensions.kt`
  - fragment creation, commit helpers, and app-shell fragment reuse
- `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`
  - consumes drawer router, bottom-drawer content, and notification helper APIs

## Persistence and infra anchors

- `data/src/main/kotlin/co/anitrend/data/tag/entity/TagEntity.kt`
- `data/schemas/`
- `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`

## Internal API reuse checklist

- Search `android/core`, `android/navigation`, `android/deeplink`, and `app/core` before adding a
  new Android helper.
- Reuse or extend an existing helper/controller/provider when the behavior already exists there.
- Keep reusable Android concerns in `:android:*`, not in `feature`, `common`, or `task`.
- Use shared router/provider/deeplink infrastructure before constructing direct intents.
- If a helper affects app-shell setup or Koin startup, inspect `app/core` before creating a new
  entry-layer abstraction.

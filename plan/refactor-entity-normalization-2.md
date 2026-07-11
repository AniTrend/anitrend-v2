# Refactor: Entity Normalization — Remove JSON Blobs & Align to Module Patterns

**Branch**: `refactor/1160-replace-json-blob-list-persistence`  
**Scope**: `data/` module — stats entities, user statistics, sidecar containers, TypeConverterObject  
**References**: `data/tag`, `data/genre`, `data/airing`, `data/link` as canonical patterns

---

## Problem Summary

Four separate drift patterns were introduced as features were added without full awareness of the
repository's offline-first entity architecture:

| # | Problem | Symptom |
|---|---------|---------|
| 1 | `MediaStatsEntity` stores `List<ScoreDistribution>` and `List<StatusDistribution>` as JSON blobs | `TypeConverterObject` has `fromMediaStatsScoreDistributionList`/`toMediaStatsStatusDistributionList` |
| 2 | `UserWithStatisticEntity` stores entire `UserStatisticModel.Anime` and `.Manga` objects as JSON blobs | `TypeConverterObject` has `fromStatisticAnime`/`toStatisticAnime`/`fromStatisticManga`/`toStatisticManga` |
| 3 | `UserSidecarModelContainer.AnimeFavourites`/`MangaFavourites` duplicate nested model classes inline instead of using the shared `MediaConnection` + `UserProfileFavouriteMediaEntity` | Sidecar pattern fragments the offline-first contract |
| 4 | GraphQL query proliferation (`GetUserProfile*.graphql`, `GetMediaWith*.graphql`) created shape-per-screen coupling and mapper drift | New query files became feature-specific contracts instead of module-level contracts |

### Drift timeline (git history)

- Media connection query proliferation aligns with media redesign work around `413a8dd5c` (PR #1103) and bulk additions in `cc2861663`.
- Profile query split aligns with `ef59c5d6d` (PR #1156), where `GetUserProfileFeed.graphql` and related profile-specific query shapes were introduced.
- This timeline confirms the issue is not only persistence modeling; it is also query-contract drift.

---

## Reference Architecture (Canonical Patterns)

### Pattern A — Connection entity with FK + unique index (tag, genre)

```
TagEntity          (table: tag)
TagConnectionEntity(table: tag_connection)
  ├── tag_id  FK → tag.id
  ├── media_id FK → media.id
  ├── rank, is_media_spoiler
  ├── UNIQUE INDEX (tag_id, media_id)
  └── autoincrement id
TagEntityView      (@Embedded connection + @Relation tag)
TagConnectionLocalSource  — upsertConnections(List<TagConnectionEntity>)
MediaMapper.Embed (or TagMapper as EmbedMapper) — called from MediaMapper
```

### Pattern B — Child entity with FK, natural PK (airing)

```
AiringScheduleEntity(table: airing_schedule)
  ├── media_id FK → media.id
  ├── episode, airing_at, time_until_airing
  └── primaryKeys = ["id"]      ← server-provided ID, not autoincrement
AiringMapper.Embed — called from MediaMapper for inline airing data
AiringScheduleEntity annotated @EntitySchema → AiringQueryFilter uses
  AiringScheduleEntitySchema.tableName / columns for FilterQueryBuilder
```

### Pattern C — Separate module for reusable remote model (link)

```
data/link/model/LinkModel.kt      ← remote model
data/link/entity/LinkEntity.kt    ← entity with media_id FK
data/link/mapper/LinkMapper.kt    ← standalone mapper
data/link/koin/Modules.kt         ← wiring
MediaEntityView.Extended embeds links via @Relation
```

### Pattern D — Scalable GraphQL query-family layout (character, studio)

```
queries/character/
  ├── GetCharacter.graphql
  ├── GetCharacterPaged.graphql
  └── connection/
      ├── GetCharacterFavourites.graphql
      ├── GetCharacterWithActor.graphql
      └── GetCharacterWithMedia.graphql

queries/studio/
  ├── GetStudio.graphql
  ├── GetStudioPaged.graphql
  └── connection/
      └── GetStudioWithMedia.graphql
```

This layout is a valid reference because it separates baseline queries from connection-focused
queries without introducing screen-specific contracts or sidecar-only nesting.

---

## Fix 1 — MediaStatsEntity: replace JSON blobs with connection entities

### Current state

```kotlin
// MediaStatsEntity — two JSON blob columns
@Entity(tableName = "media_stats", primaryKeys = ["media_id"])
data class MediaStatsEntity(
    @ColumnInfo(name = "media_id") override val id: Long,
    @ColumnInfo(name = "score_distribution") val scoreDistribution: List<ScoreDistribution>,
    @ColumnInfo(name = "status_distribution") val statusDistribution: List<StatusDistribution>,
)
```

### Target state

**New entities** (following `TagConnectionEntity` / `GenreConnectionEntity`):

```
media_score_distribution
  ├── media_id  LONG   FK → media.id CASCADE
  ├── score     INT    NOT NULL
  ├── amount    INT    NOT NULL
  ├── id        LONG   AUTOINCREMENT PK
  └── UNIQUE INDEX (media_id, score)

media_status_distribution
  ├── media_id  LONG   FK → media.id CASCADE
  ├── status    TEXT
  ├── amount    INT    NOT NULL
  ├── id        LONG   AUTOINCREMENT PK
  └── UNIQUE INDEX (media_id, status)
```

**Files to create**:
- `data/media/entity/stats/MediaScoreDistributionEntity.kt` — `@Entity`, `@EntitySchema`, FK to `MediaEntity`
- `data/media/entity/stats/MediaStatusDistributionEntity.kt` — same
- `data/media/entity/view/MediaStatsEntityView.kt` — `@Embedded MediaEntity` + `@Relation` both distribution lists
- `data/media/datasource/local/stats/MediaStatsLocalSource.kt` (extend to add `upsertScoreDistributions` / `upsertStatusDistributions` DAOs)

**Files to change**:
- `MediaStatsEntity.kt` — remove `scoreDistribution`/`statusDistribution` columns and the nested `@Serializable` inner classes; keep only as the parent row with `media_id`
- `MediaStatsMapper.kt` — persist distribution rows through two DAO upsert calls
- `TypeConverterObject.kt` — remove `fromMediaStatsScoreDistributionList`/`toMediaStatsScoreDistributionList`/`fromMediaStatsStatusDistributionList`/`toMediaStatsStatusDistributionList`
- `MigrationHelper.kt` — add migration steps: `ALTER TABLE media_stats DROP COLUMN score_distribution`, `ALTER TABLE media_stats DROP COLUMN status_distribution`, `CREATE TABLE media_score_distribution ...`, `CREATE TABLE media_status_distribution ...`

---

## Fix 2 — UserWithStatisticEntity: replace JSON blobs with normalized stat tables

### Current state

```kotlin
@Entity(tableName = "user_statistic")
data class UserWithStatisticEntity(
    @Embedded(prefix = "statistic_") val statistic: Statistic,
    @ColumnInfo(name = "user_id") val userId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) {
    data class Statistic(
        @ColumnInfo(name = "anime") val anime: UserStatisticModel.Anime?,   // ← JSON blob
        @ColumnInfo(name = "manga") val manga: UserStatisticModel.Manga?,   // ← JSON blob
    )
}
```

`UserStatisticModel.Anime` alone embeds 11 nested list types: `countries`, `formats`, `genres`, `lengths`, `releaseYears`, `scores`, `staff`, `statuses`, `studios`, `tags`, `voiceActors`. Each item carries `mediaIds: List<Long>`.

### Target state

**Step 1 — Aggregate summary entity** (replaces the blob Statistic):

```
user_statistic                 ← already exists as aggregate
  ├── user_id FK → user.id
  ├── media_type  TEXT  (ANIME | MANGA)
  ├── count, mean_score, standard_deviation
  ├── minutes_watched (ANIME only, nullable)
  ├── episodes_watched (ANIME only, nullable)
  ├── chapters_read (MANGA only, nullable)
  ├── volumes_read (MANGA only, nullable)
  ├── UNIQUE INDEX (user_id, media_type)
  └── id AUTOINCREMENT PK
```

**Step 2 — Per-dimension stat connection tables** (following tag_connection pattern):

Each dimension is a separate table keyed by `(user_id, media_type, dimension_key)`:

```
user_statistic_genre
  user_id FK, media_type TEXT, genre TEXT, count INT, mean_score FLOAT,
  watch_time INT,   -- minutes_watched or chapters_read depending on media_type
  id AUTOINCREMENT, UNIQUE (user_id, media_type, genre)

user_statistic_format
  user_id FK, media_type TEXT, format TEXT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, format)

user_statistic_status
  user_id FK, media_type TEXT, status TEXT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, status)

user_statistic_score
  user_id FK, media_type TEXT, score INT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, score)

user_statistic_release_year
  user_id FK, media_type TEXT, release_year INT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, release_year)

user_statistic_start_year
  user_id FK, media_type TEXT, start_year INT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, start_year)

user_statistic_length
  user_id FK, media_type TEXT, length TEXT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, length)

user_statistic_country
  user_id FK, media_type TEXT, country TEXT, count INT, mean_score FLOAT,
  watch_time INT, id AUTOINCREMENT, UNIQUE (user_id, media_type, country)

-- Staff and Studio reference existing entities:
user_statistic_staff
  user_id FK, media_type TEXT, staff_id FK → staff.id, count INT,
  mean_score FLOAT, watch_time INT, id AUTOINCREMENT,
  UNIQUE (user_id, media_type, staff_id)

user_statistic_studio
  user_id FK, media_type TEXT, studio_id FK → studio.id, count INT,
  mean_score FLOAT, watch_time INT, id AUTOINCREMENT,
  UNIQUE (user_id, media_type, studio_id)

user_statistic_tag
  user_id FK, media_type TEXT, tag_id FK → tag.id, count INT,
  mean_score FLOAT, watch_time INT, id AUTOINCREMENT,
  UNIQUE (user_id, media_type, tag_id)

-- Voice actors (anime only):
user_statistic_voice_actor
  user_id FK, media_type TEXT, staff_id FK → staff.id,
  character_name TEXT, count INT, mean_score FLOAT, watch_time INT,
  id AUTOINCREMENT, UNIQUE (user_id, media_type, staff_id)
```

> **Note on `mediaIds`**: Each stat item in `UserStatisticModel` carries a `mediaIds: List<Long>` that lists the media for that bucket. This can be a separate bridge table `user_statistic_media_ref (user_stat_id FK, media_id FK)` or stored as a comma-separated value (simpler, acceptable given it is display-only context). Decide at implementation time.

**Entity/view assembly** (following `MediaEntityView` / `UserEntityView` pattern):

```
UserStatisticEntityView
  @Embedded  UserWithStatisticEntity   ← aggregate row
  @Relation  List<UserStatisticGenreEntity>
  @Relation  List<UserStatisticFormatEntity>
  @Relation  List<UserStatisticStatusEntity>
  ... (one @Relation per dimension)
```

**Files to create** (under `data/user/entity/statistic/`):
- `UserStatisticGenreEntity.kt`, `UserStatisticFormatEntity.kt`, `UserStatisticStatusEntity.kt`, `UserStatisticScoreEntity.kt`, `UserStatisticReleaseYearEntity.kt`, `UserStatisticStartYearEntity.kt`, `UserStatisticLengthEntity.kt`, `UserStatisticCountryEntity.kt`, `UserStatisticStaffEntity.kt`, `UserStatisticStudioEntity.kt`, `UserStatisticTagEntity.kt`, `UserStatisticVoiceActorEntity.kt`
- `UserStatisticEntityView.kt`

**Files to change**:
- `UserWithStatisticEntity.kt` — restructure `Statistic` to hold only aggregate scalars (counts, meanScore, etc.)
- `UserConverters.kt` — update conversion logic to read from the normalized view
- `TypeConverterObject.kt` — remove `fromStatisticAnime`/`toStatisticAnime`/`fromStatisticManga`/`toStatisticManga`
- User stat mapper — persist each dimension list through DAO upserts
- `MigrationHelper.kt` — alter `user_statistic` table + create all 12 new tables

---

## Fix 3 — UserSidecarModelContainer + Query Contract Drift: align to UserProfileFavouriteMediaEntity + MediaEntityView

### Current state

`UserSidecarModelContainer.AnimeFavourites` and `MangaFavourites` were added as sidecar responses
containing inline nested `User.Favourites.MediaConnection` classes. These are loaded via
`GetAnimeFavourites.graphql` / `GetMangaFavourites.graphql` and the remote source bindings.

### Why this is wrong

`UserProfileFavouriteMediaEntity` already exists with schema:

```
user_profile_favourite_media
  user_id FK → user.id, media_id FK → media.id, category TEXT, sort_index INT
  UNIQUE (user_id, media_id, category)
```

The `AnimeFavourites/MangaFavourites` GraphQL responses return `MediaConnection.edges[].node`
which maps to `MediaModel.Core` — the same model that `MediaMapper` already converts to
`MediaEntity`. The `favouriteOrder` field maps to `sort_index`.

### Target state

**Remote source**: keep `getAnimeFavourites`/`getMangaFavourites` in `UserRemoteSource` — they
return the paged `MediaConnection` from the favourites field.

**Mapper**: introduce a `UserFavouriteMediaMapper` (following `AiringMapper.Embed` / `GenreMapper`
patterns):
```
UserFavouriteMediaMapper
  ├── Uses MediaMapper.Embed to persist media nodes into media table
  └── Upserts UserProfileFavouriteMediaEntity rows (user_id, media_id, category, sort_index)
```

**Source**: introduce `UserFavouriteMediaSource.Paged` that is offline-first:
```
observable(): Flow<PagingData<MediaEntityView>>
  ← reads from user_profile_favourite_media JOIN media (via MediaEntityView)
  ← network refresh via UserFavouriteMediaMapper
```

**Remove** the inline `AnimeFavourites.User.Favourites` / `MangaFavourites.User.Favourites`
nested classes from `UserSidecarModelContainer` (the `MediaConnection` nested class already
present in the same file is sufficient for the edge/node shape).

### Query-contract alignment (expanded scope)

The sidecar refactor must include the GraphQL sources that enabled the drift.

**Problem query families**:
- `data/src/main/graphql/queries/user/GetUserProfile*.graphql`
- `data/src/main/graphql/queries/media/connection/GetMediaWith*.graphql`

**Contract rules to enforce**:
1. Query files represent module contracts, not screen contracts.
2. Prefer fragment composition and existing container models over per-screen shape variants.
3. New query variants must map to existing entity/mapper module boundaries (`tag`, `genre`, `airing`, `link`, `media`) before introducing new container classes.
4. Use `character` and `studio` query-family layout as the baseline for new high-level modules: base query + paged query + `connection/` variants.
5. UI decisions must not force sidecar expansion unless there is measured resource benefit (memory/network/startup) documented in the PR.
6. Inline mapping inside sources is prohibited when a converter or mapper module already exists.
7. Mappers that persist multiple related entities must use transactions as the default behavior.
8. Re-use existing converters before adding new converter paths; new converters require a duplication check in sibling modules.

**Concrete actions**:
- Build a query inventory for both families and classify each file as `keep`, `merge`, or `delete`.
- For media connection queries, consolidate repeated connection shapes behind shared fragments and shared mapper entry points.
- For user profile queries, remove duplicated favourites/feed/overview shape layers that bypass existing local entities.
- Update generated GraphQL request bindings in remote sources to the consolidated set and remove orphan query assets.
- Add a module-level checklist gate for data-layer PRs:
  - query placement follows `character`/`studio` family layout,
  - mapper path uses existing converters,
  - multi-entity persistence is transactional,
  - no sidecar-only nested model trees unless resource benchmarks are attached.

**Acceptance criteria**:
- No new nested sidecar-only model trees for favourites/profile media edges.
- `GetUserProfile*.graphql` and `GetMediaWith*.graphql` families are reduced to a minimal canonical set.
- Every remaining query has a direct, documented mapping to one of: existing entity tables, connection entities, or view entities.
- No new inline source mapping where converter/mapper counterparts exist in module packages.
- Multi-entity embed persistence paths are transaction-backed.
- New query families use the `character`/`studio` baseline structure unless an architectural exception is documented.

---

## Fix 4 — TypeConverterObject: converters to remove

After fixes 1–3 are complete, the following converters have no remaining callers and must be
deleted from `TypeConverterObject`:

| Converter pair | Replaced by |
|---|---|
| `fromStatisticAnime` / `toStatisticAnime` | Normalized `user_statistic_*` tables |
| `fromStatisticManga` / `toStatisticManga` | Same |
| `fromMediaStatsScoreDistributionList` / `toMediaStatsScoreDistributionList` | `media_score_distribution` entity |
| `fromMediaStatsStatusDistributionList` / `toMediaStatsStatusDistributionList` | `media_status_distribution` entity |

Converters that remain (no change needed):
- `fromListString`/`toListString`, `fromListLong`/`toListLong`, `fromListFloat`/`toListFloat`
- `fromInstant`/`toInstant`, `fromCharSequence`/`toCharSequence`
- `fromUserNotificationSettingList`/`toUserNotificationSettingList`

---

## Migration Impact

Fixes 1, 2, and 3 require schema changes. The current `DATABASE_SCHEMA_VERSION` must be bumped to 21
(19→20 was handled this session; these changes are 20→21).

Migration checklist:
- [ ] Add `AutoMigration(from = 20, to = 21)` or manual migration spec
- [ ] Drop `score_distribution`, `status_distribution` columns from `media_stats`
- [ ] Alter `user_statistic`: drop `statistic_anime`, `statistic_manga` columns; add scalar columns
- [ ] Create 14 new tables (`media_score_distribution`, `media_status_distribution`, 12 `user_statistic_*`)
- [ ] Export schema JSON and verify identity hash
- [ ] Smoke-test migration on device with v20 database

---

## Implementation Order

Execute in this sequence to keep the build green at each step:

1. **Fix 1** (MediaStats distributions) — narrow scope, no domain layer changes, self-contained in `data/media/`
2. **Fix 3A** (UserFavouriteMedia mapper + source) — builds on existing `UserProfileFavouriteMediaEntity`; removes sidecar classes
3. **Fix 3B** (GraphQL contract consolidation) — reduce `GetUserProfile*` and `GetMediaWith*` query drift, then rebind remote sources
4. **Fix 2** (UserStatistic normalization) — largest scope; do after 3A/3B to avoid refactoring into unstable query contracts
5. **Fix 4** (TypeConverterObject cleanup) — final step once all converters are unreachable

---

## Files Changed Summary

| Fix | New files | Changed files |
|-----|-----------|---------------|
| 1 | `MediaScoreDistributionEntity`, `MediaStatusDistributionEntity`, `MediaStatsEntityView` | `MediaStatsEntity`, `MediaStatsMapper`, `TypeConverterObject`, `MigrationHelper` |
| 2 | 12× `UserStatistic*Entity`, `UserStatisticEntityView` | `UserWithStatisticEntity`, `UserConverters`, `TypeConverterObject`, `MigrationHelper` |
| 3A | `UserFavouriteMediaMapper`, `UserFavouriteMediaSource` | `UserSidecarModelContainer` (remove inline classes), user Koin `Modules.kt` |
| 3B | (no new entity files expected) | query assets under `graphql/queries/user` and `graphql/queries/media/connection`, remote source bindings, related mappers |
| 4 | — | `TypeConverterObject` (remove 8 converters) |

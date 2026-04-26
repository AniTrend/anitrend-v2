---
goal: Replace JSON-blob list persistence with module-owned relational entities and user-scoped projection/connection tables
version: 1.0
date_created: 2026-04-21
last_updated: 2026-04-21
owner: wax911
status: 'Completed'
tags: [refactor, architecture, migration, chore]
---

# Introduction

![Status: Completed](https://img.shields.io/badge/status-Completed-brightgreen)

Replace three JSON-blob list columns in `UserProfileOverviewEntity` and two in `UserProfileFeedEntity`
with thin user-scoped connection/ordering tables whose canonical content rows are persisted by their
owning modules (`data:media`, `data:status`/activity, `data:review`). The six offending
`TypeConverter` methods that serialise `List<RemotePayload>` are removed once the relational
projection is in place. The observable flow contracts (`entryByUserIdFlow`) and domain-layer models
(`ProfileOverview`, `ProfileFeed`) are preserved unchanged.

---

## 1. Requirements & Constraints

- **REQ-001**: `TypeConverterObject` must no longer contain `List<RemotePayload>` converters after this refactor. Acceptable converters are atomic/value-object types only (enums, bounded settings structs).
- **REQ-002**: Each `co.anitrend.data.*` module must own, persist, and relate its own slice of data. Media preview rows → `data:media`; activity rows → activity module; review rows → review module.
- **REQ-003**: Three user-scoped connection tables must be introduced: `user_profile_favourite_media`, `user_profile_activity`, `user_profile_review`. Each carries only `user_id`, the foreign content id, `category` (where applicable), and `sort_index`.
- **REQ-004**: `UserProfileOverviewEntity` and `UserProfileFeedEntity` must be removed from the database schema once the projection-based query flow is validated.
- **REQ-005**: Room `@DatabaseView`-style POJO projection or `@Relation`-annotated result class must replace the previous snapshot-row DAO return type.
- **REQ-006**: Observable flows (`entryByUserIdFlow`) must remain `Flow<T?>` returning `ProfileOverview` / `ProfileFeed` at the `UserSource` level — contract is unchanged.
- **REQ-007**: Schema version must be bumped (18 → 19) with an `@AutoMigration(from = 18, to = 19)` entry in `AniTrendStore`.
- **CON-001**: `data:favourite` remains mutation-only; no read expansion in this change.
- **CON-002**: Changes to `data:medialist`, `data:review`, or `data:status` module *external* contracts are out of scope unless already aligned.
- **CON-003**: `UserProfileFeedEntity` blob converters (`reviews`, `list_activity`) follow the same removal path as `UserProfileOverviewEntity`.
- **GUD-001**: Follow the `EmbedMapper` pattern (`UserMapper.GeneralOptionEmbed`, etc.) for coordinated sidecar persistence without injecting extra DAOs into parent mappers.
- **GUD-002**: Follow `MediaRelationConnectionEntity` as the reference shape for connection/ordering tables (composite `unique` index on `(user_id, content_id)`, `sort_index` index, auto-generated surrogate PK).
- **PAT-001**: Non-paged offline-first read via `AbstractCoreDataSource` + cache-policy-gated observable flow (see `data-state-pattern`).
- **PAT-002**: `room-entity-pattern` — four-file convention: entity / DAO / mapper / repository.

---

## 2. Implementation Steps

### Implementation Phase 1 — Freeze & add connection tables

- GOAL-001: Add the three user-scoped connection entity files and their DAOs; bump DB schema version to 19; register new entities in `AniTrendStore`.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-001 | Create `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileFavouriteMediaEntity.kt` with columns `user_id BIGINT`, `media_id BIGINT`, `category TEXT` (ANIME/MANGA), `sort_index INT`, surrogate PK auto-generated, unique index on `(user_id, media_id, category)`, FK to `UserEntity`. | ✅ | 2026-04-21 |
| TASK-002 | Create `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileActivityEntity.kt` with columns `user_id`, `activity_id`, `sort_index`, unique index on `(user_id, activity_id)`, FK to `UserEntity`. Store minimal activity data as embedded fields (`created_at`, `status`, `progress`, `site_url`, `type`, `media_id` nullable, `media_list_status`, `media_list_progress`, `media_list_volume_progress`) so no separate activity-module DAO injection is required at the overview level. | ✅ | 2026-04-21 |
| TASK-003 | Create `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileReviewEntity.kt` with columns `user_id`, `review_id`, `sort_index`, plus embedded review preview fields (`summary`, `score`, `rating`, `rating_amount`, `site_url`, `created_at`, `updated_at`, `media_id`, `media_type`), unique index on `(user_id, review_id)`, FK to `UserEntity`. | ✅ | 2026-04-21 |
| TASK-004 | Create `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileMediaPreviewEntity.kt` — embedded value object (`media_id`, `title_*`, `cover_*`, `type`, `format`, `status`, `episodes`, `chapters`, `volumes`, `is_favourite`, `mean_score`, `average_score`, `site_url`, `media_list_status`, `media_list_progress`, `media_list_volume_progress`) used by TASK-001/002/003 via `@Embedded`. | ✅ | 2026-04-21 |
| TASK-005 | Create `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileFavouriteMediaLocalSource.kt` — `@Dao` extending `AbstractLocalSource<UserProfileFavouriteMediaEntity>` with `entryByUserIdFlow(userId: Long): Flow<List<UserProfileFavouriteMediaEntity>>` and `clearByUserId(userId: Long)`. | ✅ | 2026-04-21 |
| TASK-006 | Create `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileActivityLocalSource.kt` — same pattern with `entryByUserIdFlow` and `clearByUserId`. | ✅ | 2026-04-21 |
| TASK-007 | Create `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileReviewLocalSource.kt` — same pattern. | ✅ | 2026-04-21 |
| TASK-008 | In `AniTrendStore.kt`: add the three new entity classes to the `entities` array; bump `DATABASE_SCHEMA_VERSION` from `18` to `19`; add `AutoMigration(from = 18, to = 19)` to the `autoMigrations` list. | ✅ | 2026-04-21 |
| TASK-009 | Add the three new DAOs as abstract properties to `IAniTrendStore` interface and `AniTrendStore` abstract class. | ✅ | 2026-04-21 |

### Implementation Phase 2 — Rewrite mappers

- GOAL-002: Update `UserProfileOverviewMapper` and `UserProfileFeedMapper` to persist canonical preview data into the new connection tables using the `EmbedMapper` pattern instead of serialising to JSON blobs.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-010 | Rewrite `UserProfileOverviewMapper.onResponseMapFrom` to build `List<UserProfileFavouriteMediaEntity>` (anime + manga, tagged by category and sort_index) and `List<UserProfileActivityEntity>` from the `Overview` payload, then upsert them; no longer construct `UserProfileOverviewEntity`. | ✅ | 2026-04-21 |
| TASK-011 | Change `UserProfileOverviewMapper` to implement `DefaultMapper<UserSidecarModelContainer.Overview, Unit>` (or a dedicated POJO) since there is no longer a single entity row to return; `persist` upserts the three connection tables directly. | ✅ | 2026-04-21 |
| TASK-012 | Rewrite `UserProfileFeedMapper.onResponseMapFrom` to build `List<UserProfileReviewEntity>` and `List<UserProfileActivityEntity>` from the `Feed` payload and upsert them; no longer construct `UserProfileFeedEntity`. | ✅ | 2026-04-21 |
| TASK-013 | Update `Types.kt`: change `UserProfileOverviewController` and `UserProfileFeedController` type aliases to reflect the new mapper output type (Unit or a dedicated marker). | ✅ | 2026-04-21 |

### Implementation Phase 3 — Rewrite DAO queries and converters

- GOAL-003: Replace snapshot DAO queries with relational list queries; rewrite converters to build domain objects from the connection-table rows.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-014 | Remove `UserProfileOverviewLocalSource` and `UserProfileFeedLocalSource` (the old single-row DAOs). Update `UserSourceImpl.Overview` and `UserSourceImpl.Feed` to inject the three new DAOs. | ✅ | 2026-04-21 |
| TASK-015 | Add a multi-query observable in `UserSourceImpl.Overview.observable()` that combines `favouriteMediaLocalSource.entryByUserIdFlow(id)` and `activityLocalSource.entryByUserIdFlow(id)` via `combine` to emit a `ProfileOverview`. | ✅ | 2026-04-21 |
| TASK-016 | Add a multi-query observable in `UserSourceImpl.Feed.observable()` that combines `reviewLocalSource.entryByUserIdFlow(id)` and `activityLocalSource.entryByUserIdFlow(id)` via `combine` to emit a `ProfileFeed`. | ✅ | 2026-04-21 |
| TASK-017 | Rewrite `UserProfileOverviewConverter` to accept `Pair<List<UserProfileFavouriteMediaEntity>, List<UserProfileActivityEntity>>` (or a simple data holder) instead of `UserProfileOverviewEntity`. | ✅ | 2026-04-21 |
| TASK-018 | Rewrite `UserProfileFeedConverter` to accept `Pair<List<UserProfileReviewEntity>, List<UserProfileActivityEntity>>` instead of `UserProfileFeedEntity`. | ✅ | 2026-04-21 |

### Implementation Phase 4 — Remove blob converters and old entities

- GOAL-004: Delete dead code; remove the six JSON-blob converters from `TypeConverterObject`; delete the two old sidecar entity files.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-019 | Delete `UserProfileOverviewEntity.kt` and `UserProfileFeedEntity.kt` from `data/src/main/kotlin/co/anitrend/data/user/entity/sidecar/`. | ✅ | 2026-04-21 |
| TASK-020 | Remove the six methods from `TypeConverterObject.kt`: `fromUserProfileMediaPreviewList`, `toUserProfileMediaPreviewList`, `fromUserProfileListActivityList`, `toUserProfileListActivityList`, `fromUserProfileReviewPreviewList`, `toUserProfileReviewPreviewList`. | ✅ | 2026-04-21 |
| TASK-021 | Remove `UserSidecarModelContainer.MediaPreviewPayload`, `ListActivityPayload`, `ReviewPreviewPayload` serializer imports from `TypeConverterObject.kt` (they may still exist in `UserSidecarModelContainer.kt` for mapper use, but no longer referenced by the converter). | ✅ | 2026-04-21 |
| TASK-022 | Update `AniTrendStore` entities list: remove `UserProfileOverviewEntity::class` and `UserProfileFeedEntity::class`; verify `autoMigrations` list covers the 18→19 step which drops those two tables and adds the three new tables. | ✅ | 2026-04-21 |

### Implementation Phase 5 — Tests

- GOAL-005: Update or replace unit tests for the affected mappers and converters.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-023 | Update/replace `UserProfileOverviewConverterTest` to use the new data holder / connection-entity list inputs. | ✅ | 2026-04-21 |
| TASK-024 | Update/replace `UserProfileFeedConverterTest` to use the new data holder / connection-entity list inputs. | ✅ | 2026-04-21 |
| TASK-025 | Update/replace `UserProfileFeedMapperTest` to reflect the new `UserProfileFeedMapper` DAO interactions (no longer a single `upsert(UserProfileFeedEntity)`). | ✅ | 2026-04-21 |
| TASK-026 | Add `UserProfileOverviewMapperTest` verifying that anime-favourite, manga-favourite, and activity rows are upserted into the three connection DAOs. | ✅ | 2026-04-21 |

---

## 3. Alternatives

- **ALT-001**: Use a Room `@DatabaseView` joining the three connection tables back to a single projection class, keeping a single `entryByUserIdFlow` in one DAO. Rejected initially in favour of `combine` in the source layer to avoid complex SQL views and keep the migration path simpler, but this remains viable as a follow-up optimisation.
- **ALT-002**: Introduce a `data:activity` module that owns `ListActivityPayload` rows and expose them via its own DAO. This is the ideologically pure approach but is out of scope for this issue; the activity data is embedded in the connection entity to avoid cross-module DAO injection for now.
- **ALT-003**: Keep `UserProfileOverviewEntity` but replace its blob columns with foreign-key references and `@Relation` annotations. Rejected because `@Relation` returns unordered lists and requires careful use of `sort_index` ordering at the Kotlin level, while the flat connection-table approach is simpler and consistent with `MediaRelationConnectionEntity`.

---

## 4. Dependencies

- **DEP-001**: `androidx.room` — `@Entity`, `@Dao`, `@DatabaseView`, `@AutoMigration`, `@Embedded`, `@Relation`.
- **DEP-002**: `co.anitrend.arch.data.converter.SupportConverter` — base class for converters.
- **DEP-003**: `co.anitrend.data.android.mapper.EmbedMapper` / `DefaultMapper` — base classes for mappers.
- **DEP-004**: `kotlinx.coroutines.flow.combine` — merges multiple DAo flows in `UserSourceImpl`.
- **DEP-005**: `UserEntity` — parent FK target for all three new connection entities (already in schema).
- **DEP-006**: `MediaEntity` — canonical media rows; `UserProfileFavouriteMediaEntity` embeds media preview fields rather than FK-ing directly to avoid requiring a media row to exist at insert time.

---

## 5. Files

- **FILE-001**: `data/src/main/kotlin/co/anitrend/data/android/database/converter/TypeConverterObject.kt` — remove 6 blob-list converter methods (lines 108–138 pre-refactor).
- **FILE-002**: `data/src/main/kotlin/co/anitrend/data/user/entity/sidecar/UserProfileOverviewEntity.kt` — deleted.
- **FILE-003**: `data/src/main/kotlin/co/anitrend/data/user/entity/sidecar/UserProfileFeedEntity.kt` — deleted.
- **FILE-004**: `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileFavouriteMediaEntity.kt` — new.
- **FILE-005**: `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileActivityEntity.kt` — new.
- **FILE-006**: `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileReviewEntity.kt` — new.
- **FILE-007**: `data/src/main/kotlin/co/anitrend/data/user/entity/connection/UserProfileMediaPreviewEntity.kt` — new embedded value object.
- **FILE-008**: `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileFavouriteMediaLocalSource.kt` — new DAO.
- **FILE-009**: `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileActivityLocalSource.kt` — new DAO.
- **FILE-010**: `data/src/main/kotlin/co/anitrend/data/user/datasource/local/connection/UserProfileReviewLocalSource.kt` — new DAO.
- **FILE-011**: `data/src/main/kotlin/co/anitrend/data/user/mapper/UserProfileOverviewMapper.kt` — rewritten.
- **FILE-012**: `data/src/main/kotlin/co/anitrend/data/user/mapper/UserProfileFeedMapper.kt` — rewritten.
- **FILE-013**: `data/src/main/kotlin/co/anitrend/data/user/converter/UserProfileOverviewConverter.kt` — rewritten.
- **FILE-014**: `data/src/main/kotlin/co/anitrend/data/user/converter/UserProfileFeedConverter.kt` — rewritten.
- **FILE-015**: `data/src/main/kotlin/co/anitrend/data/user/source/UserSourceImpl.kt` — `Overview` and `Feed` inner classes updated.
- **FILE-016**: `data/src/main/kotlin/co/anitrend/data/user/datasource/local/sidecar/UserProfileOverviewLocalSource.kt` — deleted.
- **FILE-017**: `data/src/main/kotlin/co/anitrend/data/user/datasource/local/sidecar/UserProfileFeedLocalSource.kt` — deleted.
- **FILE-018**: `data/src/main/kotlin/co/anitrend/data/android/database/AniTrendStore.kt` — entities list updated, version 19, new auto-migration.
- **FILE-019**: `data/src/main/kotlin/co/anitrend/data/user/Types.kt` — controller type aliases updated.
- **FILE-020**: `data/src/test/kotlin/co/anitrend/data/user/converter/UserProfileOverviewConverterTest.kt` — updated.
- **FILE-021**: `data/src/test/kotlin/co/anitrend/data/user/converter/UserProfileFeedConverterTest.kt` — updated.
- **FILE-022**: `data/src/test/kotlin/co/anitrend/data/user/mapper/UserProfileFeedMapperTest.kt` — updated.

---

## 6. Testing

- **TEST-001**: `UserProfileOverviewConverterTest` — verify that a list of `UserProfileFavouriteMediaEntity` (anime/manga categories) + list of `UserProfileActivityEntity` is correctly mapped to a `ProfileOverview` with correct `animeFavourites`, `mangaFavourites`, and `recentActivity` lists, preserving `sort_index` order.
- **TEST-002**: `UserProfileFeedConverterTest` — verify that a list of `UserProfileReviewEntity` + list of `UserProfileActivityEntity` maps to a `ProfileFeed` with correct `reviews` and `listActivity` lists.
- **TEST-003**: `UserProfileOverviewMapperTest` — verify that `onResponseMapFrom` produces the correct number of `UserProfileFavouriteMediaEntity` rows (one per anime edge + one per manga edge) and `UserProfileActivityEntity` rows, and that `persist` calls `upsert` on both connection DAOs.
- **TEST-004**: `UserProfileFeedMapperTest` — verify `onResponseMapFrom` produces correct `UserProfileReviewEntity` and `UserProfileActivityEntity` rows and `persist` calls `upsert` on both connection DAOs.
- **TEST-005**: Build-time — confirm `DATABASE_SCHEMA_VERSION = 19` and the Room schema JSON export for version 19 contains `user_profile_favourite_media`, `user_profile_activity`, `user_profile_review` tables and does *not* contain `user_profile_overview` or `user_profile_feed`.

---

## 7. Risks & Assumptions

- **RISK-001**: The `@AutoMigration(from = 18, to = 19)` will only work if the removed tables (`user_profile_overview`, `user_profile_feed`) are detected as dropped and the new tables as added. If Room's auto-migration spec cannot infer these, a manual `Migration(18, 19)` must be written to `DROP TABLE` the two old tables and `CREATE TABLE` the three new ones. This is the most likely blocker; validate with a schema diff test.
- **RISK-002**: `UserProfileFeedConverter` currently constructs a temporary `UserProfileOverviewEntity` to reuse `UserProfileOverviewConverter.mediaPreview` logic. After the rewrite both converters must share a common helper function to avoid duplication.
- **RISK-003**: `combine` on three flows (favourite media, activity, review) emits on *any* upstream change, which could cause redundant re-emissions. This is acceptable for now; distinctUntilChanged mitigates churn.
- **ASSUMPTION-001**: `UserSidecarModelContainer.MediaPreviewPayload`, `ListActivityPayload`, and `ReviewPreviewPayload` remain in `UserSidecarModelContainer.kt` as network-deserialization models even after the converters are removed, since the mappers still consume them.
- **ASSUMPTION-002**: There are no production users on schema version 18 who require a non-destructive migration path (the DB is rebuilt on fresh install and cache-invalidated on upgrade).
- **ASSUMPTION-003**: The activity data embedded in `UserProfileActivityEntity` is sufficient for the `ProfileOverview.ListActivityPreview` domain model without cross-module joins.

---

## 8. Related Specifications / Further Reading

- [GitHub Issue #1160](https://github.com/AniTrend/anitrend-v2/issues/1160)
- [MediaRelationConnectionEntity.kt](data/src/main/kotlin/co/anitrend/data/media/entity/connection/MediaRelationConnectionEntity.kt) — reference shape for connection entities
- [UserMapper.kt](data/src/main/kotlin/co/anitrend/data/user/mapper/UserMapper.kt) — EmbedMapper pattern reference
- [AniTrendStore.kt](data/src/main/kotlin/co/anitrend/data/android/database/AniTrendStore.kt) — entity registration and auto-migration list
- [TypeConverterObject.kt](data/src/main/kotlin/co/anitrend/data/android/database/converter/TypeConverterObject.kt) — converters to be cleaned up
- Room AutoMigration documentation: https://developer.android.com/training/data-storage/room/migrating-db-versions#automigration

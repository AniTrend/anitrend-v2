---
name: layered-module-patterns
description: 'Reference matrix for how domain and data modules are wired into feature, common, and task modules. Use when choosing the right repository/use-case/source shape or reviewing docs against real code.'
---

# Skill: Layered Module Patterns

## Overview

AniTrend does not have a single universal module recipe. The stable rules are the layer
boundaries and contract shapes; the concrete wiring varies by whether the module is query-only,
mutation-only, or hybrid.

Use this skill to choose the closest existing pattern before adding or documenting a new module.

## Pattern matrix

| Pattern | Reference module | When to copy it | Domain shape | Data shape | Entry-point shape |
|---|---|---|---|---|---|
| Simple query-only | `domain/tag` + `data/tag` | Small non-paged read-only collections or singleton/detail lookups | Single repository contract + abstract use case | Single repository + non-paged source + `TagUseCaseImpl` | Feature/common code reads through interactor |
| Read-heavy multi-contract | `domain/media` + `data/media` | Detail, paged, and network variants in one domain package | Nested repository contracts such as `Detail`, `Paged`, `Network` | `Types.kt` aliases + `MediaRepository.*` + `MediaInteractor.*` | Feature ViewModels consume read interactors |
| Hybrid query + mutation | `domain/medialist` + `data/medialist` + `task/medialist` | Fetch plus save/delete/sync for the same concept | Nested contracts per operation | Separate source/repository/interactor classes per operation | Feature/common code reads directly, mutations usually enqueue task workers |
| Hybrid fetch + action | `domain/review` + `data/review` + `task/review` | Paged/detail reads plus vote/save/delete writes | Nested contracts per read/write action | Separate read and mutation sources, repositories, and interactors | Review UI fetches via feature ViewModel; voting/deleting routes through task workers |
| Mutation-only | `domain/favourite` + `data/favourite` + `task/favourite` | A focused toggle/save/delete operation with no local read screen | Small sealed param + single repository contract | Lean `Types.kt`, single repository, single interactor bridge | UI/common code creates task params; worker runs the mutation interactor |

## Key files to read

- `domain/src/main/kotlin/co/anitrend/domain/media/`
- `data/src/main/kotlin/co/anitrend/data/media/`
- `domain/src/main/kotlin/co/anitrend/domain/medialist/`
- `data/src/main/kotlin/co/anitrend/data/medialist/`
- `task/medialist/src/main/kotlin/co/anitrend/task/medialist/`
- `domain/src/main/kotlin/co/anitrend/domain/review/`
- `data/src/main/kotlin/co/anitrend/data/review/`
- `task/review/src/main/kotlin/co/anitrend/task/review/`
- `domain/src/main/kotlin/co/anitrend/domain/favourite/`
- `data/src/main/kotlin/co/anitrend/data/favourite/`
- `task/favourite/src/main/kotlin/co/anitrend/task/favourite/`

## Standard wiring

```
feature ViewModel / common presenter / task Worker
        -> data.*Interactor alias
        -> domain use case
        -> domain repository contract
        -> data repository
        -> data source
        -> GraphQL controller / Room / cache
```

The public dependency surface that feature and task modules usually see is the interactor alias
exported from the data module `Types.kt`. That alias resolves to a domain use case specialized
with `DataState<T>`.

## Rules

- Keep params, repository contracts, and abstract use cases in `:domain`.
- Keep concrete repositories, sources, controllers, converters, mappers, and Koin bindings in
  `:data`.
- Keep `Types.kt` lean: controller aliases, repository aliases, and interactor aliases only.
- For offline-first non-paged read flows, wire the repository as `source create source(param)`
  over a source that extends `AbstractCoreDataSource` and returns a local observable flow.
- For offline-first paged read flows, wire the repository as `source create source(param)` over a
  source that reads from Room via `observable(): Flow<PagedList<T>>`. Do not keep a separate
  factory-based network paging path once a local source-of-truth exists.
- Imports like `co.anitrend.data.review.GetReviewPagedInteractor` in `feature` or `task` modules
  are acceptable because they alias domain use cases. Imports of `ReviewRepository`,
  `ReviewSourceImpl`, `ReviewMapper`, or remote models are not.
- Split repository contracts by operation when the module mixes detail/paged/sync/save/delete/rate
  behavior. Do not collapse unrelated flows into one broad repository API.
- Prefer task-backed mutation flows when the existing user flow already uses WorkManager or when
  the write should survive process transitions. `medialist`, `review`, and `favourite` are the
  current references.
- Use `tag` as the smallest baseline only. Do not treat it as the canonical pattern for media,
  review, or other mutation-heavy modules.

## Offline-first read notes

### Non-paged offline-first reads

- Use `TagSource`, `GenreSource`, `EdgeConfigSource`, `MediaSource.Detail`,
  `ReviewSource.Entry`, and `UserSource.Identifier/Viewer/Profile/Statistic` as the baseline
  references.
- Sources should return `Flow<Model>` or `Flow<List<Model>>`, not `PagedList`.
- The source `invoke(...)` operator should store query context, call `cachePolicy(...)`, and then
  return the local `observable()` flow.
- `observable()` should read Room-backed local state and project it with converters, typically
  across IO and computation dispatcher boundaries.
- Multi-context entity families should model distinct source variants as inner classes instead of
  collapsing unrelated query contexts into one broad source contract.
- Mutation-only variants such as review save/rate/delete or user follow/update are separate
  source shapes and should not be used as the baseline for non-paged read contracts.

### Paged offline-first reads

- Use `MediaSource.Paged`, `AiringScheduleSource.Paged`, and `UserSource.Search` as the baseline
  for paged offline-first reads.
- Sources should coordinate refreshes and paging triggers, not convert remote payloads into domain
  models inline.
- Mappers own persistence. If the local table shape depends on request context such as a parent id
  or page number, pass that context into the mapper before invoking the controller.
- Converters own projection from local entity or view types into domain models consumed by the
  `FlowPagedListBuilder` pipeline.
- Relationship collections such as media-to-character or media-to-staff should use dedicated
  connection entities instead of trying to reuse the remote edge model as the local cache shape.

## Choosing the nearest reference

- Copy `tag` when the module is a single non-paged read path with no task or mutation plumbing.
- Copy `media` when you need multiple read contracts against the same entity package.
- Copy `medialist` when reads and writes share one domain area and writes are background-task
  driven.
- Copy `review` when the module fetches entities but actions such as vote/save/delete are separate
  mutation paths.
- Copy `favourite` when the job is only a single toggle-style mutation.

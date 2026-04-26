# GraphQL to Kotlin Mapping Matrix

## Purpose

Use this matrix to map contract changes from schema and operation files to Kotlin model and source wiring.

## Primary Asset Mapping

| Layer | AniList Path | Edge Path | Kotlin Target | Notes |
|---|---|---|---|---|
| Schema authority | `data/schema.graphql` | `data/anitrend.schema.graphql` | `data/*/model` and containers | Defines allowable fields per graph.
| Fragment shape | `data/src/main/assets/graphql/fragments/**` | `data/src/main/assets/graphql/fragments/edge/**` | `data/*/model/**` variant classes | Fragment granularity should mirror variant responsibility.
| Query composition | `data/src/main/assets/graphql/queries/**` | edge-backed operations under `queries/series` and edge fragments | `data/*/datasource/remote/*RemoteSource.kt` | `@GraphQuery` must match operation.
| Mutation composition | `data/src/main/assets/graphql/mutations/**` | edge mutations if introduced | mutation response model container types | Keep post-write state needs explicit.
| Remote binding | `data/src/main/kotlin/**/datasource/remote/**` | `data/edge/src/main/kotlin/**/datasource/remote/**` | container generics | Container type must match payload root and variant.
| Source orchestration | `data/src/main/kotlin/**/source/**` | `data/edge/src/main/kotlin/**/source/**` | source contract variants | Source selects operation + variant fit.

## Concrete Media Mapping

### Fragment family

- `data/src/main/assets/graphql/fragments/media/Media.graphql`
- `data/src/main/assets/graphql/fragments/media/MediaCore.graphql`
- `data/src/main/assets/graphql/fragments/media/MediaExtended.graphql`

### Model family

- `data/src/main/kotlin/co/anitrend/data/media/model/MediaModel.kt`

### Operation usage

- `data/src/main/assets/graphql/queries/media/GetMediaDetail.graphql` uses `... MediaExtended`
- `data/src/main/kotlin/co/anitrend/data/media/datasource/remote/MediaRemoteSource.kt` binds `@GraphQuery("GetMediaDetail")`
- `data/src/main/kotlin/co/anitrend/data/media/source/MediaSourceImpl.kt` calls `remoteSource.getMediaDetail(...)`

### Shape reasoning

- `MediaCore` extends base media shape and includes `trailer` and `mediaListEntry`.
- `MediaModel.Core` includes base fields plus `trailer` and `mediaListEntry`.
- This is a near 1:1 mapping by design and should be preserved when adding fields.

## Field Introduction Decision Table

| Change Type | Where to start | Required follow-up |
|---|---|---|
| New read-only AniList field on media detail | `fragments/media/*.graphql` | Update model variant + container + remote response typing + source use path |
| New edge-only media enrichment field | edge fragments + edge query operation | Update edge model + edge local/source + enrich converter |
| New mutation post-write field | mutation selection set + reusable fragment if possible | Update mutation container + mapper/local updates |
| New shared field across multiple queries | reusable fragment first | update all dependent operations and variants intentionally |

## Anti-Drift Rules

- Do not add model fields without operation-level selection coverage.
- Do not use broad variants where a narrower variant exists.
- Do not rely on nullable compatibility fields to hide contract mismatches.
- Do not mix edge and AniList ownership for the same field without explicit reconciliation.

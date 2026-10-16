---
name: staged-sidecar-persistence
description: 'Use when AniTrend sidecar rows or Room joins are missing despite successful responses, or when changing stateful EmbedMapper and writer Koin wiring.'
---

# Staged Sidecar Persistence

Embed mappers (`EmbedMapper.onEmbedded`) buffer converted rows in `entities` until
`persistEmbedded()` drains them. A mapper stages into its **own** embed instances; a writer
constructed from separately resolved `get<...>()` instances drains **different, empty** buffers.
Rows silently vanish: the media row lands (it is the writer's `data`), every staged sidecar
(tag, genre, link, rank, airing, media-list, user, custom list/score) does not.

## Three-way diagnosis

Decide which failure class the evidence shows before touching code:

| Evidence | Class | Where to look |
|---|---|---|
| Request never fired or failed (cache log, Chucker error) | Request absent | source/cache policy; cross-check `cache-request-isolation` skill before assuming a cache collision |
| Response 200 with valid payload, primary rows present, sidecar rows absent | Stage/drain ownership | this skill: `onEmbedded` site vs its actual `persistEmbedded()` drainer |
| Response 200 with valid payload, no rows at all | Deserialization gate | decode the captured payload into its container model: missing required fields (fragment selections vs model) and sealed-class discriminator mismatches throw before any mapper runs |
| Sidecar rows carry fk parents (tag/staff/studio) the write path never stages | Parent staging | the enclosing transaction rolls back every row including the summary row; persist parent entities before junction rows |
| Sidecar rows present but a join returns fewer rows than the response | Partial join | the actual query (INNER JOIN, filters); query evidence, not guesswork |

Enum reuse alone is not a cache collision: `cache_log` identity is `request + cache_item_id`;
two variants sharing a `CacheRequest` value are fine when the identity rows differ.

## Required sequence

1. Correlate evidence: network/HTTP window, primary table rows, sidecar table rows, join count.
   "Rows absent" must mean absent for the SAME payload window that produced the primary rows.
2. Inventory every `onEmbedded` call site and the writer that actually runs
   `persistEmbedded()` for that buffer, including nested chains (media inside media-list,
   media-list inside media).
3. Prove the missing rows with the real Koin graph and real Room (in-memory `AniTrendStore`
   bound as `IAniTrendStore`), asserting row values through relationship views.
4. Fix ownership: share factory-local instances and drain every staged buffer.
5. Verify refresh value replacement (same logical keys, new values, stable cardinality) and
   factory-graph isolation (two mapper instances must not flush each other's buffers).
6. Expand the same trace to sibling flows (media, media-list, review, profile) and record
   confirmed regression anchors below.

## Shared construction (verified pattern)

Inside ONE Koin `factory`, resolve each staged embed once, build the writer inline from the
same references, and pass those same references to the mapper. Keep `factory`; never `single`:

```kotlin
factory {
    val tagMapper = get<TagMapper.Embed>()
    val genreMapper = get<GenreMapper.Embed>()
    val linkMapper = get<LinkMapper.Embed>()
    val rankMapper = get<RankMapper.Embed>()
    val airingMapper = get<AiringMapper.Embed>()
    val mediaListMapper = get<MediaListMapper.Embed>()
    val localSource = store().mediaDao()
    MediaMapper.Paged(
        mediaListMapper = mediaListMapper,
        tagMapper = tagMapper,
        genreMapper = genreMapper,
        airingMapper = airingMapper,
        linkMapper = linkMapper,
        rankMapper = rankMapper,
        writer = MediaPagedWriter(
            localSource = localSource,
            linkPersistence = linkMapper,
            rankPersistence = rankMapper,
            tagPersistence = tagMapper,
            genrePersistence = genreMapper,
            airingPersistence = airingMapper,
            mediaListPersistence = mediaListMapper,
        ),
        localSource = localSource,
        converter = get(),
    )
}
```

Capture the owning DAO once per factory (`val localSource = store().<dao>()`) and share it the
same way. Persist all rows already staged; never delete staging calls to make a test pass.

## Verification checklist

- Assert stored row VALUES through relationship views (`mediaByIdFlow(id)` extended relations),
  not mock invocation counts.
- Refresh with identical logical keys and changed values: latest values must win and logical
  cardinality must stay stable (watch auto-generated surrogate keys on join tables).
- Catalog/FK prerequisites: tag and genre connection rows need their catalog parents seeded
  (`TagEntity`, `GenreEntity(id = genre.toHashId(), ...)`) or upserts fail on FKs; record the
  actual FK failure, never disable constraints.
- Factory isolation test: stage into mapper A, persist mapper B first, assert A's rows are NOT
  flushed, then persist A and assert they land.

## Non-fixes (do not teach these)

Singleton embeds ("share one instance everywhere"), exception suppression around upserts,
retry loops, and relaxed Room relations all mask the ownership error. The fix is shared
construction plus a complete drain.

## Known latent findings (record, do not fix without a demonstrated consumer defect)

- `EdgeThemeMapper` can overwrite child theme batches under multiple stagings; no current
  consumer path to that sequence has been demonstrated.
- Dead paths: `AiringWriter`/`StatisticEmbed` wiring, unstaged `Review.Entry` author rows, and
  the empty episode drain are not authorization for unrelated cleanup.
- Non-paged recommendations must call `mapper.onRequest(...)` before the refresh deferred,
  otherwise the snapshot clear targets media `0` instead of the requested media.

## Regression anchors

- `data/src/androidTest/kotlin/co/anitrend/data/core/IsolatedSidecarTestSuite.kt`
- `data/src/androidTest/kotlin/co/anitrend/data/media/mapper/MediaSidecarPersistenceTest.kt`
- `data/src/androidTest/kotlin/co/anitrend/data/medialist/mapper/MediaListSidecarPersistenceTest.kt`
- `data/src/androidTest/kotlin/co/anitrend/data/review/mapper/ReviewSidecarPersistenceTest.kt`
- `data/src/androidTest/kotlin/co/anitrend/data/user/mapper/ProfileSidecarPersistenceTest.kt`
- `data/src/androidTest/kotlin/co/anitrend/data/recommendation/mapper/RecommendationRefreshPersistenceTest.kt`

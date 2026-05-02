# Flow and DataState Notes

AniTrend already uses Turbine in data-layer tests. The important pattern is stable:

- Mock the collaborator that produces the upstream input.
- Collect the exposed `Flow` with Turbine.
- Assert only the emissions the caller depends on.

## Repo anchors

- `data/src/androidTest/kotlin/co/anitrend/data/media/datasource/local/MediaLocalSourceTest.kt`
  uses Turbine against Room-backed flows and `expectMostRecentItem()`.
- `data/feed/src/androidTest/kotlin/co/anitrend/data/feed/news/datasource/remote/NewsRemoteSourceTest.kt`
  uses Turbine with a content-provider backed flow and `awaitItem()`.

## Practical guidance

- Use `awaitItem()` when order matters and the first emission is part of the contract.
- Use `expectMostRecentItem()` when the stream may emit intermediate values and only the latest stable state matters.
- Use `cancelAndIgnoreRemainingEvents()` for long-lived streams when completion is not part of the contract.
- When mocking a collaborator that returns a `Flow`, use `every { dependency.observe(...) } returns flowOf(...)`.
- When mocking a suspend collaborator that triggers the stream, use `coEvery`.

## Common mistakes

- Using `coEvery` for a regular `Flow` returning function.
- Verifying every intermediate emission instead of the meaningful one.
- Converting an otherwise unit-testable flow test into `androidTest` unnecessarily.

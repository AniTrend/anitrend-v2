# domain/src/main/kotlin/co/anitrend/domain/review/

## Responsibility

Defines review detail, paged review search, rating, save, and delete operations.

## Design Patterns

`IReviewRepository` is split into entry, paged, rate, save, and delete contracts. `ReviewUseCase` mirrors these slices. `ReviewParam.Save` includes validation helpers for summary and body content.

## Data & Control Flow

Screens or workers submit a `ReviewParam` variant to a use case. The repository performs the read or mutation and returns state or paging data.

## Integration Points

Implemented by review data and consumed by `task/review/`, review detail, editor, voting, and list surfaces.

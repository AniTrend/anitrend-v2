# domain/src/main/kotlin/co/anitrend/domain/studio/

## Responsibility

Defines studio detail, paged studio search, studio entity contracts, and sort values.

## Design Patterns

`StudioUseCase` wraps `IStudioRepository` detail and paged operations. `Studio`, `StudioDetailData`, and `IStudio` define the app-facing studio shape.

## Data & Control Flow

Callers request a specific studio or a paged list. Data resolves the repository call and maps results back to studio domain entities.

## Integration Points

Consumed by studio search/detail flows, media studio lists, and favourite operations.

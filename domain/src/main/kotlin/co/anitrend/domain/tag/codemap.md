# domain/src/main/kotlin/co/anitrend/domain/tag/

## Responsibility

Defines media tag lookup contracts and tag entities.

## Design Patterns

`TagUseCase` is a thin wrapper over `ITagRepository`. `TagParam` carries tag lookup options and `Tag` models media tags.

## Data & Control Flow

A caller passes `TagParam` to `getMediaTags`; the repository returns media tag state.

## Integration Points

Implemented by tag data, consumed by search/filter UI, and refreshed by `task/tag/`.

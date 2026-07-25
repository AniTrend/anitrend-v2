# domain/src/main/kotlin/co/anitrend/domain/episode/

## Responsibility

Defines episode detail, paged episode listing, and episode sync contracts.

## Design Patterns

`EpisodeUseCase` splits detail, paged, and sync operations. `EpisodeParam` carries detail identifiers and paged sync filters. `Episode` is the app-facing entity.

## Data & Control Flow

A caller invokes detail, paged, or sync use cases. The repository either returns one episode, a paged collection, or a sync state.

## Integration Points

Implemented by episode data and scheduled by `task/episode/` for background sync.

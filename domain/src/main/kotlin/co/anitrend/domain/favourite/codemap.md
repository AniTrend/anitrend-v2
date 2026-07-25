# domain/src/main/kotlin/co/anitrend/domain/favourite/

## Responsibility

Defines favourite toggle and favourite ordering mutations for anime, manga, characters, staff, and studios.

## Design Patterns

`FavouriteUseCase.Toggle` wraps `IFavouriteRepository.Toggle`. `FavouriteInput` is a sealed mutation input with entity-specific toggle and ordering variants.

## Data & Control Flow

A task or UI passes a `FavouriteInput` variant into the use case. The repository performs the mutation and returns a state for success or failure handling.

## Integration Points

Consumed by `task/favourite/` and favourite actions in media, character, staff, and studio flows.

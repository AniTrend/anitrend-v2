# task/favourite/

## Responsibility

Runs favourite toggle mutations through WorkManager.

## Design Patterns

`MediaFavouriteWorker` transforms `FavouriteTaskRouter` params into `FavouriteInput` variants and calls `ToggleFavouriteInteractor`. `FeatureProvider` exposes the worker class.

## Data & Control Flow

A feature enqueues favourite work through the router. The worker invokes the favourite interactor and returns success or failure from the terminal load state.

## Integration Points

Consumes `domain/favourite` inputs and data favourite interactors. Used by media, character, staff, and studio favourite actions.

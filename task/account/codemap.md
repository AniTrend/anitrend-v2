# task/account/

## Responsibility

Runs account sign-in and sign-out work in the background so account state changes can be routed through WorkManager.

## Design Patterns

`AccountSignInWorker` and `AccountSignOutWorker` extend `SupportCoroutineWorker`. `FeatureProvider` implements `AccountTaskRouter.Provider`. `FeatureInitializer` loads Koin worker bindings.

## Data & Control Flow

Router params are transformed into `AccountParam.SignIn` or `AccountParam.SignOut`. Workers call the data account interactor and return success or failure from the terminal load state.

## Integration Points

Consumes domain account params and data account interactors. Exposed to app flows through `AccountTaskRouter`.

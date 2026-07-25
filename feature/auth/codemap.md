# feature/auth/

## Responsibility

Owns OAuth style authentication entry, result handling, and authenticated state presentation.

## Design Patterns

- `AuthResultContract` wraps activity result handling.
- `Authentication` models auth payload state.
- `AuthPresenter` and `AuthViewModel` coordinate auth UI state.
- `FeatureProvider`, initializer, and Koin modules expose the auth route.

## Data & Control Flow

- Navigation enters `AuthScreen` through the auth router provider.
- `AuthScreen` and `AuthCompose` render login actions and result handling.
- `AuthViewModel` and `AuthPresenter` process submitted auth data and update state.

## Integration Points

- Uses browser support for external auth flow.
- Uses `common/shared` for shared UI scaffolding.
- Connects to core presenter and navigation payload helpers.

## Key Paths

- `feature/auth/src/main/kotlin/`
- `feature/auth/src/main/AndroidManifest.xml`
- `feature/auth/build.gradle.kts`

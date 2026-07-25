# feature/about/

## Responsibility

Owns the About destination that presents application and project information.

## Design Patterns

- Activity style screen entry with `AboutScreen` and Compose content in `AboutCompose`.
- `AboutViewModel` supplies screen state through the standard feature ViewModel pattern.
- `FeatureProvider`, `FeatureInitializer`, and `koin/Modules.kt` expose the route to app navigation.

## Data & Control Flow

- `AboutRouter` resolves `FeatureProvider` and starts `AboutScreen`.
- `AboutScreen` creates the Compose surface and delegates content rendering to `AboutCompose`.
- `AboutViewModel` provides data used by the surface.

## Integration Points

- Uses `common/shared` for shared Compose helpers.
- Connects to `app/navigation` through `AboutRouter.Provider`.
- Initializes through `core` feature initializer and Koin module loading.

## Key Paths

- `feature/about/src/main/kotlin/`
- `feature/about/src/main/AndroidManifest.xml`
- `feature/about/build.gradle.kts`

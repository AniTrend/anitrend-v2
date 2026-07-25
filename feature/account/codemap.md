# feature/account/

## Responsibility

Owns account-facing Compose screen scaffolding for authenticated user account content.

## Design Patterns

- Small screen module with `AccountScreen` as host and `AccountCompose` for the rendered surface.
- No module-local ViewModel, provider, or Koin files were present under `feature/account/src/main/kotlin` at inspection time.

## Data & Control Flow

- `AccountScreen` creates the screen host.
- `AccountCompose` renders the account UI using shared Compose infrastructure.

## Integration Points

- Uses `common/shared` and core screen components.
- Receives navigation payloads through shared navigation model types where needed.

## Key Paths

- `feature/account/src/main/kotlin/`
- `feature/account/src/main/AndroidManifest.xml`
- `feature/account/build.gradle.kts`

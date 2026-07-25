# feature/staff/

## Responsibility

Owns staff detail destination presentation.

## Design Patterns

- Small screen and Compose split through `StaffScreen` and `StaffCompose`.
- Provider and Koin modules expose `StaffRouter.Provider`.

## Data & Control Flow

- `StaffRouter` enters `StaffScreen` with staff payloads.
- `StaffScreen` hosts `StaffCompose` for presentation.

## Integration Points

- Uses `common/shared`, app navigation, Android core, and browser support.

## Key Paths

- `feature/staff/src/main/kotlin/`
- `feature/staff/src/main/AndroidManifest.xml`
- `feature/staff/build.gradle.kts`

# app/core/src/main/kotlin/co/anitrend/core/migration/

## Responsibility

This package manages app level migrations that must run when the stored app version changes.

## Design Patterns

- `IMigrationManager` defines the public migration contract.
- `AbstractMigrationManager` supplies shared migration sequencing behavior.
- Migration models describe versioned steps and migration collections.
- A dedicated AndroidX Startup initializer invokes migration execution.

## Data & Control Flow

Startup reads persisted version settings, builds the applicable migration list, executes pending steps, and updates stored version state when migration succeeds.

## Integration Points

- Uses settings from `android/core` and data settings contracts.
- Invoked by `app/core/src/main/kotlin/co/anitrend/core/initializer/migration/MigrationInitializer.kt`.
- Covered by unit tests under `app/core/src/test/`.

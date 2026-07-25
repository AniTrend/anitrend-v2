# app/core/src/main/kotlin/co/anitrend/core/config/

## Responsibility

This package defines developer mode configuration contracts used by app flavors and debug builds.

## Design Patterns

- Contract based configuration separates debug and release behavior.
- Abstract base config provides shared defaults while source sets provide concrete implementations.

## Data & Control Flow

The active source set supplies a developer mode config implementation. Consumers resolve it through Koin or direct source set wiring to enable debug-only behavior.

## Integration Points

- Debug implementation is under `app/src/debug/kotlin/co/anitrend/config/`.
- Used by runtime and settings surfaces that expose developer controls.

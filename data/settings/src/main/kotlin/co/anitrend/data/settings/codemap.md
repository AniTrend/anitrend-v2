# data/settings/src/main/kotlin/co/anitrend/data/settings/

## Responsibility

This package is the settings contract namespace. Each child directory defines interfaces and related values for one settings concern.

## Design Patterns

- One interface per settings concern keeps contracts narrow and mockable.
- The package separates cache, connectivity, customize, developer, feature, notification, power, privacy, push, refresh, sort, and sync settings.

## Data & Control Flow

Feature, task, and data-layer code reads or writes settings through these interfaces. Concrete storage providers are wired by DI so callers do not depend on storage details.

## Integration Points

- Used by authentication, cache, networking, task scheduling, notification, and settings UI flows.
- Shares package names with settings feature sections but remains data-layer contract code.

# android/core/src/main/kotlin/co/anitrend/android/core/controller/

## Responsibility

This package contains Android service backed controllers for power and widget operations.

## Design Patterns

- Controller contracts isolate Android system services from callers.
- Implementations receive typed Android services and settings through Koin.
- Power control checks combine system power state, connectivity state, and persisted power settings.

## Data & Control Flow

Koin creates controllers using Android system services. Consumers ask the controller for current platform state or perform widget related operations without directly resolving framework services.

## Integration Points

- Power controller is bound in `android/core/koin/Modules.kt`.
- Power settings come from `android/core/settings/Settings.kt`.
- Consumed by data refresh logic, task scheduling logic, and image loading policy.

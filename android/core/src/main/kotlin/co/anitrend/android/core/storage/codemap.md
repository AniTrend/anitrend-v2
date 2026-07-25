# android/core/src/main/kotlin/co/anitrend/android/core/storage/

## Responsibility

This package owns file and cache storage helpers used by app core and data integrations.

## Design Patterns

- Storage controller contract abstracts cache and storage directory access.
- Storage type enum classifies usage such as cache storage.
- Extensions provide storage size and cleanup helpers.
- Settings provide storage usage limits.

## Data & Control Flow

Consumers request storage directories and size limits from `IStorageController`. App core Coil setup uses these values to configure disk cache location and maximum size.

## Integration Points

- Bound by `android/core/koin/Modules.kt`.
- Used by `app/core` image loading setup.
- Reads cache settings from data settings contracts implemented by `Settings`.

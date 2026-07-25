# android/core/src/main/kotlin/co/anitrend/android/core/extensions/

## Responsibility

This package contains low level Android extension functions for context, intent, view, inset, LiveData, Compose, Chip, and Timber helpers.

## Design Patterns

- Extension functions adapt Android framework APIs without introducing global utility classes.
- Context helpers safely resolve lifecycle and fragment manager access where possible.
- View and inset helpers centralize repeated UI framework operations.

## Data & Control Flow

Platform and feature code call these helpers to resolve context owned services, start framework operations, update views, handle insets, log diagnostics, and bridge lifecycle aware APIs.

## Integration Points

- Used across app core, Android platform modules, and feature UI modules.
- Complements `app/core/src/main/kotlin/co/anitrend/core/extensions/` for app runtime extensions.

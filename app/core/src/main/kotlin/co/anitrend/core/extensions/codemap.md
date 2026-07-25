# app/core/src/main/kotlin/co/anitrend/core/extensions/

## Responsibility

This package contains app runtime extension functions for coroutine scopes, authentication checks, flows, components, collections, and general core helpers.

## Design Patterns

- Extension functions provide narrow framework adapters without widening base classes.
- Flow helpers standardize common stream transformations.
- Component helpers keep lifecycle related operations reusable across screens.

## Data & Control Flow

Screens, presenters, and view models call these helpers to access lifecycle scopes, validate authentication state, process flows, and perform small reusable component operations.

## Integration Points

- Used throughout app core and feature presentation code.
- Complements `android/core/src/main/kotlin/co/anitrend/android/core/extensions/` for lower level Android helpers.

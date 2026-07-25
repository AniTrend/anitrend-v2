# app/core/src/main/kotlin/co/anitrend/core/ui/

## Responsibility

This package provides app specific UI helpers for fragment transactions, tagged fragment reuse, scoped injection, and lightweight fragment item models.

## Design Patterns

- `FragmentItem` bundles a fragment class, parameters, and optional tag for navigation commits.
- Extension functions hide repetitive fragment transaction and Koin scope access code.
- Commit helpers keep route selection code in screens concise.

## Data & Control Flow

A screen builds a `FragmentItem`, resolves an existing tagged fragment or creates a new one, and commits it into the target container. Injection helpers resolve scoped dependencies from the current component context.

## Integration Points

- Used heavily by `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`.
- Consumes router outputs from `app/navigation`.
- Relies on app core component base classes and Koin scopes.

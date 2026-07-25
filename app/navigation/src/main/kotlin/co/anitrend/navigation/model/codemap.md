# app/navigation/src/main/kotlin/co/anitrend/navigation/model/

## Responsibility

This package stores shared route payload models and parameter marker contracts.

## Design Patterns

- `IParam` marks objects that can be converted to Android navigation payloads.
- `NavPayload` groups target data used by route extension helpers.
- Sorting wrappers pair domain sort keys with sort order for route parameters.

## Data & Control Flow

Screens create parameter data classes or sorting values, extension helpers convert them to a `Bundle` or `Intent`, and the destination reads them through its own argument handling.

## Integration Points

- Used by route params in `NavigationTargets.kt`.
- Depends on domain sort enums and shared domain sort order contracts.
- Consumed by feature modules that expose or receive typed route inputs.

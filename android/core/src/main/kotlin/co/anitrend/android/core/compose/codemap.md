# android/core/src/main/kotlin/co/anitrend/android/core/compose/

## Responsibility

This package provides shared Compose primitives, design components, shape helpers, dimensions, and preview providers.

## Design Patterns

- Material3 components use tokens from `android/core/src/main/kotlin/co/anitrend/android/core/ui/`.
- Design components are grouped by reusable UI purpose such as header, sheet, toggle, image, card, choice, slider, and page indicator.
- Preview providers supply stable sample data for Compose previews and tests.

## Data & Control Flow

Compose screens import design components and theme values, pass state models into components, and receive UI callbacks from reusable controls. Components render against `AniTrendTheme3` where available.

## Integration Points

- Used by `android/deeplink`, `android/navigation`, and newer feature surfaces.
- Shape and avatar behavior is covered by tests under `android/core/src/test/` and `android/navigation/src/test/`.

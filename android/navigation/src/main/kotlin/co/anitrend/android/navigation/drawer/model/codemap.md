# android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/model/

## Responsibility

This package stores drawer data models for accounts, navigation sections, sheet state, and internal mapping.

## Design Patterns

- Public models describe account and navigation rows.
- Internal models represent modern drawer state before legacy conversion.
- Resolver and mapper classes keep selection and config logic outside UI components.
- Defaults and resource registries centralize IDs, labels, and icon resources.

## Data & Control Flow

View models combine settings, account data, and config data into drawer models. Internal mappers convert these models into either Compose content state or legacy navigation menu values.

## Integration Points

- Consumed by drawer view models, adapters, legacy adapter, and Compose drawer content.
- Uses resources from `android/navigation/src/main/res/`.
- Maps to navigation IDs handled by `MainScreen`.

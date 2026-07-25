# android/core/src/main/kotlin/co/anitrend/android/core/settings/

## Responsibility

This package owns app preference backed settings and helper contracts for locale, theme, and configuration.

## Design Patterns

- `Settings` extends support preference storage and implements many fine grained settings interfaces.
- Setting wrappers provide typed access for booleans, enums, numbers, strings, and sets.
- Locale, theme, and configuration helpers are split into contract, model, and implementation packages.
- Configuration helper applies theme, edge to edge, and locale behavior during activity lifecycle.

## Data & Control Flow

Koin creates `Settings`, then binds it to all supported settings contracts. Activity lifecycle code calls `IConfigurationHelper.onCreate` and `onResume`, which reads current locale and theme settings and applies updated configuration when values change.

## Integration Points

- Uses resource keys from `android/core/src/main/res/values/settings.xml` and related values files.
- Implements interfaces from `data/settings`, `data/auth`, and `data/user` settings packages.
- Consumed by app core, settings feature, data sources, task schedulers, and notification flows.

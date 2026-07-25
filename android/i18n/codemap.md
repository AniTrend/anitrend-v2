# android/i18n/

## Responsibility

`android/i18n/` is a small Android resource module for shared localized strings that can be consumed by platform and presentation modules.

## Design Patterns

- Resource only module keeps shared strings independent of feature implementation code.
- Android manifest and Gradle module setup expose resources through the normal Android resource pipeline.

## Data & Control Flow

Android resource merging makes strings from this module available to dependent modules at build time. Runtime code accesses them through generated resource IDs.

## Integration Points

- Consumed by modules that need shared localization strings without depending on a feature module.
- Source resources live under `android/i18n/src/main/res/values/strings.xml`.

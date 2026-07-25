# common/shared/

## Responsibility

Provides shared Compose scaffolding, list bottom sheets, chart components, share extensions, and markdown image plugin wiring used across feature modules.

## Design Patterns

- Shared Compose helpers centralize common layouts.
- Chart components render score and status distribution data.
- Markdown plugin, image span configuration, and image tag handler extend common markdown behavior.
- Initializer and Koin modules register shared UI dependencies.

## Data & Control Flow

- Feature and common modules call shared Compose helpers or chart components directly.
- Markdown image plugin setup is provided through Koin and initializer wiring.
- Share extensions translate UI actions into platform share intents.

## Integration Points

- Depends on `common/markdown`, Markwon, AniTrend markdown support, and Android core helpers.
- Used broadly by feature modules for shared UI surfaces.

## Key Paths

- `common/shared/src/main/kotlin/`
- `common/shared/src/main/AndroidManifest.xml`
- `common/shared/build.gradle.kts`

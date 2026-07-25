# common/markdown/

## Responsibility

Provides reusable Markdown text widgets, Compose Markdown text, Markwon plugins, image spans, alignment decorators, size measurement, and press animation helpers.

## Design Patterns

- Widget and Compose rendering paths coexist for hybrid UI.
- Plugin packages isolate Markwon store, decorator, image span, and configuration behavior.
- Initializer and Koin modules register markdown UI dependencies.

## Data & Control Flow

- Feature or common UI supplies markdown text and optional span configuration.
- Markdown components configure Markwon and render styled content.
- Plugin handlers decorate alignment, paragraph, image, and size behavior.

## Integration Points

- Uses Markwon, Coil Markwon integration, BetterLinkMovementMethod, Android core, and common domain text models.

## Key Paths

- `common/markdown/src/main/kotlin/`
- `common/markdown/src/main/AndroidManifest.xml`
- `common/markdown/build.gradle.kts`

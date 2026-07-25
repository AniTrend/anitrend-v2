# common/editor/

## Responsibility

Provides reusable markdown input widget support for feature modules.

## Design Patterns

- `MarkDownInputWidget` wraps editor UI behavior.
- Initializer and Koin modules register editor UI dependencies.

## Data & Control Flow

- Feature code embeds the widget.
- The widget handles markdown input behavior and delegates lifecycle setup through the common initializer.

## Integration Points

- Uses Markwon editor, emojify dependencies, Android core, and support-arch UI widgets.

## Key Paths

- `common/editor/src/main/kotlin/`
- `common/editor/src/main/AndroidManifest.xml`
- `common/editor/build.gradle.kts`

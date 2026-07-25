# common/medialist/

## Responsibility

Provides reusable media list editing controls.

## Design Patterns

- `CounterEditor` provides a focused Compose counter editing component.

## Data & Control Flow

- Feature code embeds `CounterEditor` where numeric media list progress or counts are edited.
- The component emits edited counter values to its caller.

## Integration Points

- Depends on `common/media` and Android core UI primitives.

## Key Paths

- `common/medialist/src/main/kotlin/`
- `common/medialist/src/main/AndroidManifest.xml`
- `common/medialist/build.gradle.kts`

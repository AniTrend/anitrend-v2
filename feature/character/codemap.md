# feature/character/

## Responsibility

Owns the character destination screen and character detail ViewModel.

## Design Patterns

- Activity screen plus ViewModel pattern through `CharacterScreen` and `CharacterViewModel`.
- `FeatureProvider` and Koin modules satisfy `CharacterRouter.Provider`.

## Data & Control Flow

- `CharacterRouter` supplies payloads to `CharacterScreen`.
- `CharacterScreen` delegates state work to `CharacterViewModel`.
- The ViewModel prepares character detail state for the screen.

## Integration Points

- Connects to `app/navigation` through `CharacterRouter`.
- Uses Android core and app core screen infrastructure.
- Browser dependency is declared for external links.

## Key Paths

- `feature/character/src/main/kotlin/`
- `feature/character/src/main/AndroidManifest.xml`
- `feature/character/build.gradle.kts`

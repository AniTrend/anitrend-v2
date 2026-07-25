# domain/src/main/kotlin/co/anitrend/domain/character/

## Responsibility

Defines character search, identity, role, and sort domain models.

## Design Patterns

`CharacterUseCase` wraps `ICharacterRepository.Search`. `CharacterParam` captures query and filter input. `Character` implements `ICharacter` contracts for shared id, name, image, and favourite behavior.

## Data & Control Flow

Search params flow from UI to the use case, then to the repository search contract, which returns paged character results.

## Integration Points

Implemented by character data sources and consumed by character search, media cast, and detail surfaces.

# domain/src/main/kotlin/co/anitrend/domain/config/

## Responsibility

Defines the app configuration read contract and config entity.

## Design Patterns

`ConfigUseCase.Get` wraps `IConfigRepository.Get`. `Config` is the domain output for remote or local application settings.

## Data & Control Flow

Startup or task code asks for config. The repository returns a state containing the current configuration.

## Integration Points

Implemented by config data and scheduled by `task/config/` to refresh configuration in the background.

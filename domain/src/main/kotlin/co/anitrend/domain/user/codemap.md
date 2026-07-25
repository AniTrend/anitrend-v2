# domain/src/main/kotlin/co/anitrend/domain/user/

## Responsibility

Defines user lookup, authenticated profile reads, profile search, follow toggles, profile updates, statistics, overview, feed params, user entities, options, statistics, and profile models.

## Design Patterns

`IUserRepository` is split into user, authenticated, profile, search, toggle follow, update, statistic, overview, and feed contracts. `UserUseCase` mirrors those operations. Entity packages separate core user, profile, option, and statistic contracts.

## Data & Control Flow

User params choose one repository slice. Data resolves the user/profile/social operation and returns UI state or paging data to the caller.

## Integration Points

Implemented by user data and consumed by profile screens, account startup, follow actions, and `task/user/` sync workers.

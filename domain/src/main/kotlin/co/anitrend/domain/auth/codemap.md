# domain/src/main/kotlin/co/anitrend/domain/auth/

## Responsibility

Defines the authenticated-user contract used after account authorization.

## Design Patterns

`AuthUseCase<State>` wraps `AuthRepository<State>`. The contract exposes `getAuthenticatedUser` without Android or storage details.

## Data & Control Flow

A caller asks for the current authenticated user. Data resolves the session and returns the user state through the generic domain contract.

## Integration Points

Used by profile, account, startup, and task user sync flows that need current viewer context.

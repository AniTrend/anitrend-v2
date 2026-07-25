# domain/src/main/kotlin/co/anitrend/domain/account/

## Responsibility

Owns domain operations for local account identity, sign in, sign out, and authorized account lookup.

## Design Patterns

`AccountUseCase` is an abstract wrapper over `AccountRepository<State>`. `AccountParam` contains sign-in and sign-out payloads. The contract stays generic over `UiState<*>` so data can specialize return behavior.

## Data & Control Flow

A caller requests authorized accounts or submits account auth params. The use case delegates to `getAccountUsers`, `signIn`, or `signOut` on `AccountRepository`.

## Integration Points

Implemented by data account/auth code and consumed by `task/account/` workers plus account-facing UI flows.

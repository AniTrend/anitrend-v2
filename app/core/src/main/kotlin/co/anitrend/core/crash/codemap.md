# app/core/src/main/kotlin/co/anitrend/core/crash/

## Responsibility

This package owns app crash handling abstractions and runtime exception delegation.

## Design Patterns

- Interface first crash handler contract allows flavor or runtime specific handling.
- A runtime uncaught exception handler centralizes fallback crash behavior.
- Timber logging keeps crash diagnostics in the shared logging path.

## Data & Control Flow

Unhandled exceptions are routed through the installed exception handler, delegated to the app crash handler, then logged or forwarded based on available runtime services.

## Integration Points

- Used during app startup and runtime error handling.
- Complements flavor specific analytics or crash reporting code under app source sets.

# domain/src/main/kotlin/co/anitrend/domain/status/

## Responsibility

Defines user activity status params, status entity shape, status type, and status sort values.

## Design Patterns

`StatusParam` provides builder-style request input. `Status`, `StatusType`, and `StatusSort` model social activity feed values.

## Data & Control Flow

Status params are passed into data flows that load or filter activity status content.

## Integration Points

Used by profile, social, and activity feed features.

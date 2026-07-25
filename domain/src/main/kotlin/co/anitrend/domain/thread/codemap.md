# domain/src/main/kotlin/co/anitrend/domain/thread/

## Responsibility

Defines forum thread params, thread entity shape, and thread sort enums.

## Design Patterns

`ThreadParam` is a sealed request model. `Thread`, `ThreadSort`, and `ThreadCommentSort` model forum thread listing and comment sorting values.

## Data & Control Flow

Thread params are passed into data-backed forum flows that retrieve thread or comment content.

## Integration Points

Used by forum feature modules and data mapping.

# domain/src/main/kotlin/co/anitrend/domain/airing/

## Responsibility

Defines airing schedule queries, schedule entities, sort values, and media-linked schedule results.

## Design Patterns

`AiringScheduleUseCase` wraps `IAiringScheduleRepository.Paged`. `AiringParam` carries schedule filters. `AiringSchedule` and `AiringScheduleWithMedia` keep schedule data separate from linked media presentation.

## Data & Control Flow

Callers submit airing params to the paged use case. The repository provides paged schedule data, optionally including media data for display.

## Integration Points

Implemented by airing data sources and consumed by airing schedule feature surfaces.

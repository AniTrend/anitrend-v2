# domain/src/main/kotlin/co/anitrend/domain/media/

## Responsibility

Defines the central media domain for anime and manga detail, search, relations, recommendations, characters, staff, studios, stats, titles, images, ranks, scores, links, trailers, sources, and enums.

## Design Patterns

`IMediaRepository` is split into detail, relations, recommendations, paged recommendations, characters, staff, studios, stats, paged, and network contracts. `MediaUseCase` mirrors these operations with sealed use case classes. Entity attributes are factored into nested packages for reusable rank, origin, theme, link, score, trailer, image, and title models.

## Data & Control Flow

Feature params from `MediaParam` select an operation and filters. Use cases delegate to the matching repository slice. Data maps API or cache records into `Media` and related entries.

## Integration Points

Implemented by media data and consumed by media detail, search, discovery, recommendation, relation, character, staff, and studio screens.

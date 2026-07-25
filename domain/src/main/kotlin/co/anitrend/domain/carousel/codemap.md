# domain/src/main/kotlin/co/anitrend/domain/carousel/

## Responsibility

Defines media carousel request and entity shapes for curated media rows.

## Design Patterns

`MediaCarouselUseCase` wraps `IMediaCarouselRepository`. `CarouselParam` describes the carousel request and `MediaCarousel` represents the app-facing result.

## Data & Control Flow

Callers invoke the use case with carousel params. Data fetches or assembles carousel content and returns it as a UI state.

## Integration Points

Consumed by discovery or home surfaces that render grouped media rails.

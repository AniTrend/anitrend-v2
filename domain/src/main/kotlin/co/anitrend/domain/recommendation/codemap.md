# domain/src/main/kotlin/co/anitrend/domain/recommendation/

## Responsibility

Defines recommendation params, rating enum, sort enum, and recommendation entity.

## Design Patterns

`RecommendationParam` describes recommendation request filters. `RecommendationRating` and `RecommendationSort` encode rating and sorting choices. `Recommendation` is the app-facing item.

## Data & Control Flow

Recommendation params are passed into data-backed recommendation flows, often through media recommendation contracts.

## Integration Points

Used by media recommendation screens and data mapping.

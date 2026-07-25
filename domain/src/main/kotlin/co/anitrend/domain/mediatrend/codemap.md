# domain/src/main/kotlin/co/anitrend/domain/mediatrend/

## Responsibility

Defines media trend entities and trend params used by media trend queries.

## Design Patterns

`MediaTrendParam` provides builder-style trend request input. `MediaTrend` is a sealed app-facing trend model.

## Data & Control Flow

Trend filters are carried as params into media trend data flows, which return trend entities for display.

## Integration Points

Used by data and discovery surfaces that show trending media.

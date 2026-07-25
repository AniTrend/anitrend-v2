# data/core/src/main/kotlin/co/anitrend/data/android/controller/

## Responsibility

The controller package coordinates request execution, response mapping, persistence, and load-state publication for default REST-like responses and GraphQL responses.

## Design Patterns

- `DefaultController` handles standard Retrofit response payloads.
- `GraphQLController` handles `GraphQLResponse` envelopes, warns on GraphQL errors, and throws a request error when data is absent.
- `ControllerStrategy` implementations decide online or offline load-state behavior around the request block.

## Data & Control Flow

Sources pass deferred Retrofit responses and request callbacks into a controller. The controller fetches network data, runs the mapper, inserts mapped output into the database on the configured dispatcher, and returns mapped data to the strategy block.

## Integration Points

- Uses mapper contracts from `data/core/src/main/kotlin/co/anitrend/data/android/mapper/`.
- Uses network clients from `data/core/src/main/kotlin/co/anitrend/data/android/network/client/`.
- Consumed by AniList, Edge, and feed data sources through type aliases and DI bindings.

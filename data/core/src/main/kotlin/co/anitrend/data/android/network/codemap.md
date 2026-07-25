# data/core/src/main/kotlin/co/anitrend/data/android/network/

## Responsibility

The network package provides shared HTTP and GraphQL request infrastructure for data modules, including clients, cookies, interceptors, cache headers, response models, and default network adapters.

## Design Patterns

- Client abstraction through `AbstractNetworkClient` and deferrable clients.
- Retrofit and OkHttp adapters keep transport details out of repositories and sources.
- GraphQL adapter support keeps AniList and Edge request envelopes consistent.
- Cookie and interceptor packages isolate cross-cutting HTTP behavior.

## Data & Control Flow

Remote sources issue Retrofit requests. Controllers fetch through deferrable network clients, which unwrap responses and surface request errors back to controller strategies.

## Integration Points

- Used by remote source interfaces throughout `data/src/main/kotlin/co/anitrend/data/` and `data/edge/`.
- The user-modified files under `data/core/src/main/kotlin/co/anitrend/data/android/network/client/` were not edited.

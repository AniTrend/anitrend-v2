# domain/src/main/kotlin/co/anitrend/domain/

## Responsibility

This package root contains all domain contracts and app-facing models used by AniTrend layers. Each direct child package maps to one product concept or shared domain utility.

## Design Patterns

- `interactor/` classes are thin use case wrappers around repositories.
- `repository/` interfaces define the operations that data modules must implement.
- `model/` classes define request params, mutation inputs, and paging or filter payloads.
- `entity/` classes define app-facing domain objects, often backed by small `contract/` interfaces for shared behavior.
- `enums/` and `common/sort/` encode stable API values and user sort preferences.

## Data & Control Flow

Callers pass a typed param into a use case or interactor. The use case forwards to the matching repository contract. Data implementations translate those params into local cache, GraphQL, Edge, or third-party requests, then map results back into the domain entity shape.

## Integration Points

- Direct child packages are documented by their own `codemap.md` files in this directory.
- Data modules bind these contracts and export aliases used by UI and task modules.
- Task modules use mutation and sync params from `account`, `favourite`, `medialist`, `review`, `episode`, `config`, `genre`, `news`, `tag`, and `user`.

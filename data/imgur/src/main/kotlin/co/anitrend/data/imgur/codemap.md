# data/imgur/src/main/kotlin/co/anitrend/data/imgur/

## Responsibility

This package contains the Imgur data namespace and Koin wiring present in this checkout.

## Design Patterns

- Minimal package surface with DI-first organization.
- No local Room or Retrofit source package is present here based on the inspected files.

## Data & Control Flow

The package currently contributes bindings only. There is no standalone repository or source flow documented by the local file structure.

## Integration Points

- `koin/` is the integration point for registering Imgur-related data dependencies.
- Kept under `data/imgur/` so future Imgur implementation can remain isolated from AniList and Edge modules.

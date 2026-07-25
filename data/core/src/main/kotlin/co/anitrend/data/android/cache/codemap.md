# data/core/src/main/kotlin/co/anitrend/data/android/cache/

## Responsibility

The cache package records request identities and decides whether a source can refresh a resource. It stores cache log entries and exposes helpers used by offline-first and paged sources.

## Design Patterns

- `CacheRequest` identifies the logical resource family.
- `CacheIdentity` identifies the specific resource item and variant.
- `CacheStorePolicy` evaluates expiration windows and invalidates request history.
- Cache extension functions wrap source refresh calls with cache and paging state checks.

## Data & Control Flow

Sources construct a cache identity, ask the policy if a refresh should run, and invalidate identities when local data is cleared. Successful refreshes update cache history so later requests can avoid unnecessary network calls.

## Integration Points

- Used by `cache/` packages inside AniList data domains such as media, genre, tag, review, staff, studio, user, and airing.
- Backed by local source and entity classes in this package.

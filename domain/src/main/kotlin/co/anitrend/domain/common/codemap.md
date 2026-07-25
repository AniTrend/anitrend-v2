# domain/src/main/kotlin/co/anitrend/domain/common/

## Responsibility

Provides reusable domain primitives shared across entity packages.

## Design Patterns

Contracts cover identity, names, cover images, synopsis, favourites, page info, and sortability. Shared value objects include `FuzzyDate`, `CoverImage`, `CoverName`, `PageInfo`, `SortWithOrder`, and custom scalar type aliases.

## Data & Control Flow

Feature-specific entities implement common contracts so UI, data mappers, and paging code can handle repeated concepts consistently.

## Integration Points

Imported throughout domain packages and mirrored by data model mapping when constructing app-facing entities.

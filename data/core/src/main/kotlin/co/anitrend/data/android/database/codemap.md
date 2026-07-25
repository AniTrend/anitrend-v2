# data/core/src/main/kotlin/co/anitrend/data/android/database/

## Responsibility

The database package contains shared Room database utilities used by data stores.

## Design Patterns

- Transaction helper abstraction for consistent database transaction boundaries.
- Common DAO and source helpers are kept close to the data infrastructure package.

## Data & Control Flow

Mappers and local sources use database helpers when persistence work must happen inside a transaction or shared Room access boundary.

## Integration Points

- Used by mapper and source implementations in concrete data packages.
- Complements local `I...Store` interfaces in domain packages that define each Room store surface.

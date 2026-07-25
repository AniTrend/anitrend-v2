# data/src/main/graphql/queries/

## Responsibility

AniList GraphQL read operations for detail, search, paging, profile, social, and list flows.

## Design Patterns

- Assets are grouped by domain folder so generated request and model names stay close to data package names.
- Shared field sets live in fragments and are referenced by read or write operations instead of duplicating selections.
- Paged and connection subfolders isolate AniList connection shapes when the operation needs edge or page metadata.

## Data & Control Flow

The GraphQL generator reads these files and emits Kotlin operation classes. Data sources import those generated classes, map domain params into generated variables, and pass requests through the shared GraphQL controller path.

## Integration Points

- Used by remote sources under `data/src/main/kotlin/co/anitrend/data/`.
- Connected to Retrofit GraphQL requests through `GraphQLRequest` and the generated AniList namespace.

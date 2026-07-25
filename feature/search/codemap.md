# feature/search/

## Responsibility

Owns multi-scope search for media, characters, staff, studios, and users.

## Design Patterns

- `SearchScope`, `SearchSubmission`, and `SearchState` model local search state.
- Section Composables split search result rendering by entity type.
- `SearchPresenter` supports presentation logic outside the ViewModel.
- Provider and Koin modules expose search navigation.

## Data & Control Flow

- Navigation enters `SearchScreen`.
- `SearchChrome` and screen content collect query and scope state.
- `SearchViewModel` dispatches entity-specific searches and result sections render the output.

## Integration Points

- Uses `common/shared` and `common/media`.
- Consumes character, media, staff, studio, and user interactors.
- Uses Paging Compose for result lists.

## Key Paths

- `feature/search/src/main/kotlin/`
- `feature/search/src/main/AndroidManifest.xml`
- `feature/search/build.gradle.kts`

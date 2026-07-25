# feature/news/

## Responsibility

Owns news browsing, news detail presentation, and RSS or embedded content decoration.

## Design Patterns

- Separates list content ViewModel from screen ViewModel.
- Uses markdown plugins, tag handlers, image span configuration, and presenter logic for rich news content.
- Provider, initializer, and Koin modules expose `NewsRouter.Provider`.

## Data & Control Flow

- `NewsRouter` enters `NewsScreen` or related news destinations.
- `NewsContent` and screen Compose classes render list and detail state.
- ViewModels consume news interactors and presenters decorate rich content for display.

## Integration Points

- Uses `common/markdown` and `common/shared`.
- Consumes Edge news data aliases and news domain interactors.
- Uses Paging Compose, browser support, Jsoup, Markwon, link movement, and annotated text libraries.

## Key Paths

- `feature/news/src/main/kotlin/`
- `feature/news/src/main/AndroidManifest.xml`
- `feature/news/build.gradle.kts`

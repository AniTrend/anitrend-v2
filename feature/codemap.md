# feature/

## Responsibility

`feature/` contains app-facing screen modules. Each child module owns a user-visible destination or flow, while data access remains behind domain and data alias interactors. Feature modules render screens, collect user actions, bind ViewModels or presenters, and expose navigation providers where the app shell or shared routers need to enter the feature.

## Design Patterns

- Most modules follow `component/screen`, `component/compose` or `component/content`, `component/viewmodel`, `provider`, `initializer`, and `koin` packages.
- Navigation is router-driven through provider implementations such as `FeatureProvider` classes, then wired through `koin/Modules.kt` and `FeatureInitializer`.
- UI is hybrid. Some destinations are Compose-first, some keep content or sheet boundaries, and larger modules split route, section, and content packages.
- Feature modules consume common UI packages from `common/` and interactor aliases from data or domain layers. They do not own persistence or remote source implementations.

## Data & Control Flow

- App navigation or a deep-link route resolves a router in `app/navigation`, then the feature provider returns the screen, fragment, or sheet class owned by the feature.
- Screen classes create the host surface and pass payloads into Compose content, content controllers, presenters, or ViewModels.
- ViewModels request data through interactor aliases and expose screen state to Compose or legacy UI components.
- User actions return to ViewModels, presenters, action providers, or task routers for writes that are handled outside the feature UI layer.

## Integration Points

- Navigation contracts live outside this scope in `app/navigation`, with feature providers implemented inside each feature module.
- Shared visual and presentation components come from `common/*`, especially `common/shared`, `common/media`, `common/markdown`, `common/review`, `common/episode`, `common/genre`, and `common/tag`.
- Data access is through exported interactor aliases such as media, user, settings, review, news, episode, airing, and studio interactors.
- Koin module loading is coordinated by module-local initializers and `core` initializer infrastructure.

## Module Map

- `feature/about`: about screen and app information.
- `feature/account`: account screen shell.
- `feature/airing`: airing schedule browsing.
- `feature/auth`: authentication entry and result handling.
- `feature/character`: character detail screen.
- `feature/episode`: episode content and episode sheet.
- `feature/feed`: user feed surface.
- `feature/forum`: forum surface.
- `feature/image-viewer`: full-screen image viewer.
- `feature/media`: media detail hub, sub-screens, sections, actions, and schedule sheet.
- `feature/medialist`: user media list screen.
- `feature/news`: news list, detail content, and rich content handling.
- `feature/notification`: notification screen.
- `feature/profile`: profile overview, library, feed, and stats.
- `feature/recommendation`: recommendation feature shell with no inspected Kotlin sources.
- `feature/review`: review browsing or detail surface.
- `feature/search`: multi-entity search.
- `feature/settings`: settings surfaces and local settings navigation.
- `feature/staff`: staff detail screen.
- `feature/studio`: studio detail screen.
- `feature/suggestion`: suggestion screen.
- `feature/updater`: update check screen.

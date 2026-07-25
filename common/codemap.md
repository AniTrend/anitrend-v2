# common/

## Responsibility

`common/` contains reusable UI and presentation modules shared by multiple features. These modules provide Compose components, legacy widgets, adapters, presenters, markdown helpers, controller models, charts, and other view-layer primitives. They do not own screen destinations or data repositories.

## Design Patterns

- Modules use `ui/compose` for Compose components and `ui/widget`, `ui/controller`, or `ui/adapter` for legacy view and RecyclerView surfaces.
- Shared controller model packages adapt domain objects into list items without taking ownership of data loading.
- Initializer and Koin packages appear where a common module registers reusable plugins or widgets.
- Empty shell modules remain present for module boundary consistency even when no Kotlin sources are currently present.

## Data & Control Flow

- Feature modules pass domain models, paging data, or local UI state into common components.
- Common components format, render, and emit UI events back to their caller.
- Navigation actions inside reusable list items call shared router helpers rather than importing feature classes directly.
- Markdown and shared plugin modules configure rendering behavior that other common and feature modules consume.

## Integration Points

- Used by `feature/*` modules for reusable media, markdown, review, episode, genre, tag, and shared UI.
- Consumes domain model types for display only, such as media, episode, review, news, genre, tag, and user related models.
- Integrates with Android core, support-arch UI, Koin, Markwon, Paging Compose, Coil, and app navigation router helpers.

## Module Map

- `common/character`: character shared UI shell.
- `common/editor`: markdown input widget support.
- `common/episode`: episode cards, sheet content, adapter, and widgets.
- `common/feed`: feed shared UI shell.
- `common/forum`: forum shared UI shell.
- `common/genre`: genre adapter and Compose component.
- `common/markdown`: Markdown rendering, plugins, spans, and widgets.
- `common/media`: media cards, sections, widgets, controllers, and presenter helpers.
- `common/medialist`: media list editing controls.
- `common/navigation`: navigation shared UI shell.
- `common/news`: news list item and differ support.
- `common/recommendation`: recommendation shared UI shell.
- `common/review`: review card Compose presentation.
- `common/shared`: general shared Compose helpers, charts, sheets, share helpers, and markdown plugin wiring.
- `common/staff`: staff shared UI shell.
- `common/studio`: studio shared UI shell.
- `common/tag`: tag adapter and Compose component.
- `common/user`: user shared UI shell.

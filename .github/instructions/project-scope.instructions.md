---
applyTo: **
description: This file describes the project scope and purpose for AniTrend v2.
---

## Project Purpose and Scope

For architecture and implementation routing, pair this file with:

- `.github/instructions/context.instructions.md` for clean-architecture boundaries.
- `.github/instructions/guides.instructions.md` for contribution conventions.
- `.agents/skills/key-libraries/SKILL.md` for library-level detail.
- `.agents/skills/reference-map/SKILL.md` for task-first navigation across instructions and skills.

**AniTrend v2** is an Android client for the AniList service — an anime and manga tracking
platform. The app lets users discover and track anime/manga, read news and forum posts, get
recommendations, see trending/airing shows, and manage their AniList profile and lists.

### External data sources

Each integration has a dedicated data module with its own network client and models:

| Source | Module | Purpose |
|---|---|---|
| **AniList GraphQL API** | `:data` (core) | Primary source: anime/manga data, user lists, social |
| **MyAnimeList via Jikan** | `:data:jikan` | Supplementary MAL data |
| **Imgur** | `:data:imgur` | Image uploads |
| **AniTrend Edge Functions** | `:data:edge` | Deno-based aggregation (news, recommendations, etc.) |
| **TMDB** | `:data:tmdb` | Additional media metadata / images |
| **Trakt** | `:data:trakt` | Watch-history sync |
| **Firebase** | `google` flavor only | Analytics (Crashlytics, Analytics) |

### Key libraries and frameworks

For the complete library reference including versions, DI wiring, and usage rules, see
`.agents/skills/key-libraries/SKILL.md`. Summary:

- **Jetpack**: Lifecycle, Room, Paging, WorkManager, Navigation Compose — core Android stack.
- **support-arch** (in-house, via JitPack) — `DataState`, `UiState`, base data sources and UI.
- **Retrofit + OkHttp + retrofit-graphql** — networking and GraphQL requests.
- **Kotlinx Serialization** — JSON serialization.
- **Koin** — dependency injection (see `.agents/skills/koin-module-wiring/SKILL.md`).
- **Coil** — image loading (GIF, SVG, video frames).
- **Material3 Compose** — primary design system.
- **Timber** — logging (use `Timber.*` not `Log.*`).

## Domain Model Context

Core domain concepts and where to find them:

| Concept | Module hint |
|---|---|
| **Media** (anime/manga) | `:feature:media:*`, `:common:media`, `:data:media` |
| **Characters, Staff, Studio** | `:feature:character`, `:feature:staff`, `:common:character` |
| **Airing schedule** | `:feature:airing` |
| **News / Forum** | `:feature:news`, `:feature:forum` |
| **Notifications** | `:feature:notification` |
| **Profile / Social** | `:feature:profile` |
| **Media lists** | `:feature:media-list` |
| **Search / Discover** | `:feature:search` |
| **Settings** | `:feature:settings`, `:data:settings` |
| **Updates** | `:feature:updater` |

**Navigation rule:** data logic lives in `:data:<entity>`, UI in `:feature:<name>`, shared UI
components in `:common:<name>`.

## Custom Edge Functionality

The `on-the-edge` Deno functions (separate repo) offload data-aggregation tasks — news feeds,
recommendation crunching, MAL/AniList bridging — to cloud functions. From the app's perspective
they are plain HTTP endpoints consumed by `:data:edge`. Always use `DataState` for edge calls so
loading/error states propagate correctly to the UI.

## Guidelines for Contributors and AI Assistants

- **Follow established patterns**: pick the closest current reference module before copying a
  structure. Use `tag` for simple query-only flows, `media` for read-heavy modules, and
  `medialist` / `review` / `favourite` for hybrid or mutation-heavy flows.
- **Respect module boundaries**: feature and task code may depend on interactor aliases exported by
  `:data:*`, but they must not import data repositories, sources, mappers, controllers, or remote
  models directly.
- **Background work**: schedule via WorkManager in `:task:*` modules.
- **Performance**: use Paging (`PagingData` / `Pager`) for list/feed screens; do not build a
  custom scroll + load mechanism.
- **Testing**: see `.agents/skills/testing-guidelines/SKILL.md`.
- **String resources**: see `.agents/skills/string-resources-convention/SKILL.md`.

---

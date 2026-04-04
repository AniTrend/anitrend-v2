---
name: key-libraries
description: 'Library stack reference for AniTrend. Use when selecting dependencies, understanding framework usage, and aligning integrations with established project conventions.'
---

# Skill: Key Libraries and Frameworks

## Overview

AniTrend v2 uses a combination of standard Android Jetpack libraries and in-house support
libraries. All versions are centrally managed in `gradle/libs.versions.toml`; use the generated
`libs.*` accessors rather than hardcoded coordinates.

## Jetpack components

- **Lifecycle + ViewModel** — lifecycle-aware holders for UI state.
- **Room** — SQLite ORM; entities, DAOs, and migrations live in `:data:*` modules.
- **Paging** — `PagingData` / `Pager` drives infinite-scroll lists; reuse the existing setup
  rather than building a custom scroll mechanism.
- **WorkManager** — schedules background jobs in `:task:*` modules.
- **Navigation Compose** — drives screen routing via `NavHost`; see `:app:navigation`.

## In-house support libraries (via JitPack)

All versions are declared in `gradle/libs.versions.toml`.

| Library | Purpose |
|---|---|
| `support-arch` | Base classes for `DataState`, `UiState`, data sources, ViewModels, UI state layout |
| `support-query-builder` | Schema-aware DSL for composing GraphQL and Room SQL queries |
| `support-markdown` | Markwon wrapper for rendering Markdown-rich content |

## Networking

- **Retrofit + OkHttp** — HTTP client; shared configuration injected by `buildSrc`.
- **`retrofit-graphql`** — AniTrend's custom Retrofit converter for GraphQL requests.
- **Kotlinx Serialization** — JSON serialization; configured alongside Retrofit.
- **Chucker** — Debug HTTP traffic inspector; only included in debug builds.

## Image loading

- **Coil** — Configured in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` with
  GIF, SVG, and video-frame support plus tuned memory/disk caches. Use the Koin-provided
  `ImageLoader` singleton rather than creating ad-hoc loaders.

## UI and theming

- **Material3 Compose** — primary design system; use `MaterialTheme` tokens for colors and
  typography.
- **support-arch theme** — `AniTrendTheme3` and `PreviewTheme` wrappers; apply them in
  composables and previews.
- **android-emojify** — Emoji parsing/rendering exposed as `EmojiManager` singleton via Koin;
  registered in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.

## Dependency injection

- **Koin** — loaded at startup via `InjectorInitializer`. See `.github/skills/koin-module-wiring/SKILL.md`
  for wiring conventions.

## Logging and analytics

- **Timber** — added globally; use `Timber.d/e/w` instead of `Log.*` or `println`.
- **Firebase Analytics + Crashlytics** — enabled only in the `google` product flavor via
  `buildSrc` plugin logic. Gate any analytics calls behind a flavor check or the analytics
  helper if available.

## Testing

- **JUnit 4** — base test runner.
- **MockK** — Kotlin-idiomatic mocking; prefer mocking interfaces over concrete classes.
- **Turbine** — Flow testing; use `turbine` to assert emissions from `DataState` flows.
- **kotlinx-coroutines-test** — `TestCoroutineDispatcher` / `runTest` for coroutine tests.
- Test dependencies are auto-added to every module by `DependencyStrategy.kt`; no manual
  declaration needed.
  - See `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt`

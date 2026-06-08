---
name: key-libraries
description: 'Library stack reference for AniTrend. Use when selecting dependencies, understanding framework usage, and aligning integrations with established project conventions.'
---

# Skill: Key Libraries and Frameworks

## Overview

AniTrend v2 uses a combination of standard Android Jetpack libraries and in-house support
libraries. All versions are centrally managed in `gradle/libs.versions.toml`; use the generated
`libs.*` accessors rather than hardcoded coordinates.

For AniTrend's repo-specific Android wrappers around system services, theme/configuration helpers,
notifications, deep links, and app-shell behavior, read
`.agents/skills/android-platform-patterns/SKILL.md` before adding a new helper API.

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
- **Chucker** — Debug HTTP traffic inspector; only included in debug builds. When runtime evidence is needed, prefer inspecting recorded responses from the debug app sandbox before changing serializers, mappers, or UI assumptions.
- **Chucker** — Debug HTTP traffic inspector; only included in debug builds. Before modifying a serializer, response mapper, or UI data assumption that depends on a live API response, inspect the response recorded by Chucker in the debug build to confirm the actual payload shape.

## Image loading

- **Coil** — Configured in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` with
  GIF, SVG, and video-frame support plus tuned memory/disk caches. Use the Koin-provided
  `ImageLoader` singleton rather than creating ad-hoc loaders.

## UI and theming

- **Material3 Compose** — primary design system; use `MaterialTheme` tokens for colors and
  typography.
- **support-arch theme** — `AniTrendTheme3` and `PreviewTheme` wrappers; apply them in
  composables and previews.
- **AniTrend Android core UI** — `:android:core` owns repo-specific theme, configuration, helper,
  and Compose utility surfaces consumed by the app shell and feature modules.
- **android-emojify** — Emoji parsing/rendering exposed as `EmojiManager` singleton via Koin;
  registered in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.

## Dependency injection

- **Koin** — loaded at startup via `InjectorInitializer`. See `.agents/skills/koin-module-wiring/SKILL.md`
  for wiring conventions.

## Logging and analytics

- **Timber** — added globally; use `Timber.d/e/w` instead of `Log.*` or `println`.
- **Firebase Analytics + Crashlytics** — enabled only in the `google` product flavor via
  `buildSrc` plugin logic. Always gate analytics calls behind a flavor check. If an analytics
  helper exists, prefer it over a raw flavor check; if no helper exists, use `BuildConfig` and
  leave a TODO to extract a helper.

## Testing

- **JUnit 4** — base test runner.
- **MockK** — Kotlin-idiomatic mocking; prefer mocking interfaces over concrete classes.
- **Turbine** — Flow testing; use `turbine` to assert emissions from `DataState` flows.
- **kotlinx-coroutines-test** — `TestCoroutineDispatcher` / `runTest` for coroutine tests.
- **kotlinx-coroutines-test** — `runTest`, `StandardTestDispatcher`, or
  `UnconfinedTestDispatcher` for coroutine tests.
- Test dependencies are auto-added to every module by `DependencyStrategy.kt`; no manual
  declaration needed.
  - See `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt`

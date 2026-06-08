---
name: android-platform-patterns
description: 'Android platform/helper layer guide for AniTrend. Use when working in `:android:*` modules or deciding whether to reuse or extend existing Android-side helpers for configuration/theme, context or fragment utilities, notification permission flows, deep links, drawer/app-shell behavior, or other platform APIs.'
---

# Skill: Android Platform Patterns

## Overview

`:android:*` is AniTrend's platform/helper layer. It owns reusable Android-side APIs that should
be shared across the app shell, feature modules, common presenters, and task modules instead of
being recreated inside those entry layers.

Before adding a new helper, wrapper, extension, or provider, inspect the existing platform
surfaces first:

- `:android:core` for settings, configuration, theme, Compose primitives, controllers, helpers,
  and context/fragment utilities
- `:android:navigation` for drawer and app-shell navigation content
- `:android:deeplink` for URI entry, parser registration, and external-intent routing
- `:app:core` for app-shell integration points that already consume those Android helpers

Use the concrete anchors in the
[layer example matrix](../reference-map/references/layer-example-matrix.md) when you need a
real code path to copy.

## Module roles

- `:android:core` centralizes reusable Android-facing APIs such as `Settings`,
  `ConfigurationHelper`, `AniTrendTheme3`, notification helpers, storage/power/shortcut
  controllers, and fragment/context utilities.
- `:android:navigation` owns the navigation drawer shell: fragment content, drawer presenter,
  viewmodels, adapters, and router provider wiring.
- `:android:deeplink` owns external URI intake: `DeepLinkScreen`, parser assembly, route
  registration, and the provider that turns a URI into a routed intent.

## Reuse-first workflow

1. Search `android/core`, `android/navigation`, `android/deeplink`, and `app/core` before
   creating anything new.
2. If an existing helper/controller/provider already models the behavior, extend that surface in
   place instead of cloning the API in `feature`, `common`, or `task`.
3. If the behavior is app-shell specific, inspect
   `android/navigation/src/main/kotlin/co/anitrend/android/navigation/` and
   `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`.
4. If the behavior starts from an external URI, inspect `android/deeplink` before touching a
   feature module.
5. Only add a new internal API when reuse would break ownership boundaries or force an awkward
  abstraction that would require changing more than one existing public interface in the platform
  layer or would force a dependency that the consuming module is not allowed to take.

## Decision rules

Apply the Reuse-first workflow first. The decision rules below are the constraints that govern the
workflow steps.

- Do not recreate context or fragment lookup helpers if
  `android/core/src/main/kotlin/co/anitrend/android/core/extensions/ContextExtensions.kt` or
  `app/core/src/main/kotlin/co/anitrend/core/ui/UiExtensions.kt` already covers the call pattern.
- Do not recreate theme or configuration wiring if `ConfigurationHelper`, `ThemeHelper`, or
  `AniTrendTheme3` already owns it.
- Do not recreate notification permission or settings flows if `NotificationHelper` or
  `NotificationExtensions.kt` already models the behavior.
- Do not bypass router and provider contracts with direct intent construction when
  `:app:navigation`, `:android:navigation`, or `:android:deeplink` already owns the route.
- Keep platform-wide helpers in `:android:*`; keep feature-specific logic in the owning feature.
- If an existing helper/controller/provider already covers the required behavior without changing
  any existing method signatures or callers, extend that surface in place instead of cloning the
  API in `feature`, `common`, or `task`.

## Canonical files

- `android/core/src/main/kotlin/co/anitrend/android/core/koin/Modules.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/settings/helper/config/ConfigurationHelper.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/extensions/ContextExtensions.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/helpers/notification/NotificationExtensions.kt`
- `android/core/src/main/kotlin/co/anitrend/android/core/ui/theme/Theme.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/koin/Modules.kt`
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/provider/FeatureProvider.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/screen/DeepLinkScreen.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/provider/FeatureProvider.kt`
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`
- `app/core/src/main/kotlin/co/anitrend/core/ui/UiExtensions.kt`
- `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`

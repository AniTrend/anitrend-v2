---
name: navigation-architecture
description: 'AniTrend UI navigation architecture guide. Use when tracing deep links, router/provider contracts, Activity-to-Fragment flows, Compose NavHost usage, or adding a new screen destination.'
argument-hint: 'Describe the flow you want to trace or the destination you want to add'
---

# Skill: Navigation Architecture

## Overview

AniTrend navigation is hybrid. Do not assume a single app-wide Compose `NavHost`, and do not
assume features navigate to each other by importing activities directly.

The current model has three layers:

- **Deep link entry** in `:android:deeplink` maps external URIs to internal intents.
- **Shared router contracts** in `:app:navigation` expose stable navigation entry points and
  parameter types.
- **Feature-owned destinations** implement those contracts through Koin-backed `FeatureProvider`
  classes, then choose whether the feature UI is activity-only, fragment-backed, or local
  Compose navigation.

This matters because the repo is still in a staged migration. The app is not moving to a full
Compose-only navigation stack yet. Respect the existing `Activity -> Fragment` boundaries and
only use local Compose navigation where the feature already does so.

## When to Use

- Trace how a URI or button click reaches a screen.
- Add a new screen or route without breaking module boundaries.
- Debug why a router resolves to the wrong activity, fragment, or sheet.
- Understand whether a feature is legacy fragment-backed or Compose-first.
- Review navigation changes for architecture drift.

## Mental Model

### 1. External deep links

External URIs enter through `DeepLinkScreen`, not straight into feature screens.

Flow:

1. `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/screen/DeepLinkScreen.kt`
   receives the incoming intent.
2. `DeepLinkViewModel` calls `DeepLinkRouter.forMatchingIntent(uri)`.
3. `DeepLinkRouter.Provider` is implemented by
   `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/provider/FeatureProvider.kt`.
4. That provider delegates to a `DeepLinkParser` built in
   `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`.
5. Route objects in `AppRoutes.kt` and `WebRoutes.kt` convert the URI into a target router call
   such as `SettingsRouter.forActivity(...)` or `MainRouter.forActivity(...)`.
6. The deep-link screen launches the resolved intent, then exits.

### 2. Internal router-driven navigation

Internal navigation goes through router singletons in
`app/navigation/src/main/kotlin/co/anitrend/navigation/NavigationTargets.kt`.

Each router:

- extends `NavigationRouter`
- resolves its provider through Koin
- exposes a contract such as `activity()`, `fragment()`, or `sheet()`
- defines typed params with `IParam` when payload is required

The shared helpers in
`app/navigation/src/main/kotlin/co/anitrend/navigation/extensions/RouterExtensions.kt` build or
start intents from those providers. Callers should prefer router helpers over constructing intents
directly.

### 3. Feature-owned UI host style

Once a router resolves a feature, the target feature usually matches one of these patterns:

- **Activity only**: `FeatureProvider.activity()` returns a screen class and that screen renders
  the whole feature directly.
- **Activity -> Fragment bridge**: the screen is Compose-hosted but embeds a fragment through
  `FragmentItemHost`, which commits the fragment into a `FragmentContainerView`.
- **Activity -> Compose -> local NavHost**: the feature uses `rememberNavController()` and a
  feature-local `NavHost` for section-to-section movement inside the screen.

Examples:

- `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt` is the app shell and still
  uses fragment transactions plus the navigation drawer router.
- `feature/airing/src/main/kotlin/co/anitrend/airing/component/screen/AiringScreen.kt` shows the
  bridge pattern: activity host, Compose scaffold, then
  `FragmentItemHost(fragment = AiringRouter.forFragment())`.
- `feature/settings/src/main/kotlin/co/anitrend/settings/component/screen/SettingsScreen.kt` and
  `feature/settings/src/main/kotlin/co/anitrend/settings/component/compose/SettingsCompose.kt`
  show the Compose-local navigation pattern with `SettingsRouter.Destination` and a local Compose
  `NavHost`.

## Procedure: Trace an Existing Flow

1. Identify the entry type.
   - External URI: start in `:android:deeplink`.
   - Internal button/menu action: start from the router call site.
   - Screen-local tab/section change: start inside the feature's Compose or fragment host.
2. Find the router object in `NavigationTargets.kt`.
   - Check its provider interface for required capabilities like `activity()`, `fragment()`, or `sheet()`.
   - Check whether it defines a typed param class.
3. Find the feature implementation.
   - Open the feature's `provider/FeatureProvider.kt`.
   - Open the feature's `koin/Modules.kt` and confirm `factory<XxxRouter.Provider> { FeatureProvider() }`.
4. Identify the host model.
   - `setContent` plus `FragmentItemHost` means the feature is still fragment-backed.
   - `setContent` plus `rememberNavController` and `NavHost` means navigation is local to that feature.
   - XML view binding plus fragment transactions usually means app-shell or legacy flow.
5. Follow payload transport.
   - Routers use `NavPayload`, `IParam`, and intent extras.
   - Deep-link routes often build params first, then call `forActivity(...)`.
6. Confirm the final destination class.
   - Activity class comes from `activity()`.
   - Fragment class comes from `fragment()`.
   - Bottom sheets or dialogs come from `sheet()`.

## Procedure: Add or Change Navigation

1. Decide which layer owns the change.
   - External URI support: update `:android:deeplink` routes.
   - Cross-feature navigation: update or add a router in `:app:navigation`.
   - Section switching within one feature screen: keep it local to that feature.
2. Define or extend the router contract in `NavigationTargets.kt`.
   - Add a new router or destination enum if needed.
   - Add an `IParam` payload type when arguments must cross module boundaries.
3. Implement the feature provider.
   - Add or update `provider/FeatureProvider.kt` in the owning feature module.
   - Return only the classes owned by that feature.
4. Bind the provider in the feature's `koin/Modules.kt`.
   - Use `factory<XxxRouter.Provider> { FeatureProvider() }`.
5. Add the host implementation.
   - Fragment-backed feature: ensure the fragment is registered in Koin and returned by `fragment()`.
   - Compose-local feature: update the feature screen and local `NavHost` routes.
   - Sheet/dialog feature: return the dialog fragment class from `sheet()`.
6. If the route must be deep-linkable, add a `Route` implementation and register it in
   `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`.

## Decision Rules

- If the user comes from a browser or app link, go through `DeepLinkScreen` and route objects.
- If one feature opens another feature, use the shared router contract, not direct activity imports.
- If the feature already hosts a fragment via `FragmentItemHost`, keep that boundary unless the
  task explicitly migrates the feature.
- If the change is only inside one Compose screen, prefer the feature-local `NavHost` and keep it
  scoped there.
- Do not replace current router or fragment patterns with Navigation 3 assumptions. This repo is
  still on the existing AndroidX Navigation Compose setup where Compose is incremental, not the
  global navigation authority.

## Review Checklist

- Router contract lives in `:app:navigation`, not in the feature module.
- Feature provider is bound through Koin and satisfies every router method the app calls.
- Navigation arguments cross module boundaries as typed params, not ad-hoc string extras.
- Deep-linkable destinations are added to both a `Route` implementation and the parser builder.
- Feature code does not import unrelated feature activities directly when a router exists.
- Compose navigation changes stay local unless the task explicitly changes shared navigation.

## Canonical Files

- `app/navigation/src/main/kotlin/co/anitrend/navigation/NavigationTargets.kt`
- `app/navigation/src/main/kotlin/co/anitrend/navigation/router/NavigationRouter.kt`
- `app/navigation/src/main/kotlin/co/anitrend/navigation/provider/INavigationProvider.kt`
- `app/navigation/src/main/kotlin/co/anitrend/navigation/extensions/RouterExtensions.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/screen/DeepLinkScreen.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/viewmodel/DeepLinkViewModel.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/route/AppRoutes.kt`
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/route/WebRoutes.kt`
- `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`
- `common/shared/src/main/kotlin/co/anitrend/common/shared/ui/compose/SharedCompose.kt`
- `feature/airing/src/main/kotlin/co/anitrend/airing/component/screen/AiringScreen.kt`
- `feature/airing/src/main/kotlin/co/anitrend/airing/provider/FeatureProvider.kt`
- `feature/settings/src/main/kotlin/co/anitrend/settings/component/screen/SettingsScreen.kt`
- `feature/settings/src/main/kotlin/co/anitrend/settings/component/compose/SettingsCompose.kt`

## Useful External Reference

- Koin Context7 library ID: `/insertkoinio/koin`
  Use this when you need framework-level DI syntax or lifecycle details beyond the repo's local
  patterns.

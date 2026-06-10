# Navigation 3 runtime-only spike result

Branch: `spike/nav3-about-runtime-only`

## What was tested
About feature rendered through Navigation 3 while `feature/about` remained `runtimeOnly` in the app module.

## What worked
- `app/navigation` compiled with Nav3 contracts (`AniTrendNavKey`, `AboutNavKey`, `Nav3SpikeHomeKey`, `NavCommand`, `NavigationDispatcher`, `FeatureNavEntryProvider`, `FeatureNavRegistry`)
- `feature/about` compiled with `AboutNavEntryProvider` implementing `FeatureNavEntryProvider`
- `feature/about` Koin module binds `FeatureNavEntryProvider` alongside existing `AboutRouter.Provider`
- App module compiled with `AniTrendNav3Host`, `RuntimeFeatureNavRegistry`, `AniTrendNavigationDispatcher`, `Nav3SpikeHome`
- `Nav3SpikeActivity` registered in debug manifest (exported for adb launch)
- APK assembled successfully (46.5MB)

## What changed
Added files:

```
gradle/libs.versions.toml                    -- added androidx-navigation3 = "1.1.2", nav3 libs, serialization plugin
app/navigation/build.gradle.kts              -- added kotlinx-serialization plugin, nav3-runtime (api), kotlinx-serialization-json
app/build.gradle.kts                         -- added navigation3-ui
feature/about/build.gradle.kts              -- made app/navigation dependency explicit

app/navigation/src/main/kotlin/.../nav3/
  AniTrendNavKey.kt                          -- sealed interface : NavKey @Serializable
  AboutNavKey.kt                             -- data object : AniTrendNavKey
  Nav3SpikeHomeKey.kt                        -- data object : AniTrendNavKey
  NavCommand.kt                              -- sealed interface (Push, Pop)
  NavigationDispatcher.kt                    -- interface
  FeatureNavRegistry.kt                      -- interface + FeatureNavEntryScope interface
  FeatureNavEntryProvider.kt                 -- interface

app/src/main/kotlin/.../nav3/
  AniTrendNavigationDispatcher.kt            -- SharedFlow-based dispatcher impl
  RuntimeFeatureNavRegistry.kt               -- adapter from FeatureNavEntryProvider -> Nav3 entries
  AniTrendNav3Host.kt                        -- @Composable host with NavDisplay + BackHandler
  Nav3SpikeHome.kt                           -- spike home screen composable

app/src/debug/kotlin/.../nav3/
  Nav3SpikeActivity.kt                       -- debug-only Activity for spike

feature/about/src/main/kotlin/.../provider/
  AboutNavEntryProvider.kt                   -- registers AboutNavKey content

app/src/main/kotlin/.../koin/Modules.kt     -- added NavigationDispatcher + AniTrendNavigationDispatcher bindings
feature/about/.../koin/Modules.kt           -- added FeatureNavEntryProvider binding
feature/about/.../compose/AboutCompose.kt   -- added visible content for testing
app/src/debug/AndroidManifest.xml           -- registered Nav3SpikeActivity
```

## What did not work
- Initially used `alias(libs.plugins.jetbrains.kotlin.serialization)` but project uses legacy `id("kotlinx-serialization")`
- Sealed interface initially placed keys in subpackage `nav3.keys` but Kotlin requires sealed subtypes in same package
- App module has flavor variants (`google`, `github`); compile tasks need flavor prefix
- `app/navigation` didn't have Compose compiler plugin → `NoSuchMethodError` on `FeatureNavRegistry.register` (Fixed: added Compose plugin + build feature to `app/navigation/build.gradle.kts`)
- `Nav3SpikeActivity` extended `ComponentActivity` but `AniTrendTheme3` requires `FragmentActivity` (Fixed: changed to `FragmentActivity`)

## Manual test results (device: SM-A546E, Android 16)
- ✅ App installed
- ✅ `Nav3SpikeActivity` launched via `adb shell am start`
- ✅ "Navigation 3 Runtime Feature Spike" home screen rendered
- ✅ Tapped "Open About via Nav3"
- ✅ About screen showed content from `feature/about` (runtimeOnly): "AniTrend" + "Navigation 3 runtime-only feature spike"
- ✅ System back returned to spike home
- ✅ System back from spike home closed activity
- ✅ No crashes during navigation or back

## Boundary verification
- `feature/about` on `googleDebugRuntimeClasspath`: YES
- `feature/about` on `googleDebugCompileClasspath`: NO
- App imports `co.anitrend.about.*`: NO
- `AboutScreen` Activity still intact: YES (fallback preserved)
- `AboutRouter.Provider` still bound in Koin: YES (old path preserved)

## Manual test steps
```bash
# Install
adb install app/build/outputs/apk/google/debug/app-google-debug.apk

# Launch spike
adb shell am start -n co.anitrend/co.anitrend.app.navigation.nav3.Nav3SpikeActivity

# Expected: "Navigation 3 Runtime Feature Spike" screen
# Tap "Open About via Nav3"
# Expected: "AniTrend" and "Navigation 3 runtime-only feature spike" text
# Press back
# Expected: return to spike home
# Press back again
# Expected: activity exits

# Process death test (verified 2025-06-10, emulator API 35):
# Open About via Nav3 > am force-stop > relaunch spike
# Result: no serialization crash; host starts from Nav3SpikeHomeKey ✅
```

## PR 1 (complete): common:navigation split
- `common:navigation` module created with `FeatureNavEntryProvider`, `FeatureNavRegistry`, `FeatureNavEntryScope`
- Compose removed from `app:navigation` — now a pure contracts module
- All imports updated, boundaries verified

## PR 2 (complete): harden registry
- `FeatureNavEntryProviderRepository` interface + `KoinFeatureNavEntryProviderRepository`
- Duplicate key detection (`check(previous == null)`)
- Provider/key logging with Timber
- Replaced `GlobalContext.get().getAll()` with DI-injected repository
- Unit tests: duplicate throws, missing key returns false, provider installs key, empty registry, multi-provider
- Koin wiring added

## Decision
**Proceed** to full About migration then next simple screen. The spike proves:
- Navigation 3 can host content from runtimeOnly feature modules
- The contract layer (`app/navigation`) → feature module → app host architecture works
- `FeatureNavEntryProvider` + Koin + Startup discovery pattern is viable
- Process death test TBD (will need to verify `@Serializable` NavKey restoration)

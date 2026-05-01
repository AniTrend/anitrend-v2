# Koin Testing

AniTrend already includes Koin test dependencies through `buildSrc`, so most modules do not need
manual test dependency declarations for Koin.

## What to use

- `KoinTest` when the test should behave as a `KoinComponent` and resolve definitions with `get()`
  or `by inject()`.
- `MockProviderRule.create { mockk() }` when `checkModules()` needs mocks for unresolved
  collaborators during graph verification.
- `declareMock` or `declare` when a running Koin test context needs one binding swapped or added
  for a focused scenario.
- `checkModules { modules(...) }` for the repo's current graph-verification style.

## Repo anchor

`data/src/androidTest/kotlin/co/anitrend/data/android/koin/ModulesTest.kt` currently verifies the
data graph with:

```kotlin
class ModulesTest : KoinTest {
    @get:Rule
    val mockProviderRule = MockProviderRule.create { mockk() }

    @Test
    fun verifyKoinDependencyGraph() {
        checkModules {
            modules(dataModules)
        }
    }
}
```

## How to choose the test shape

- Use Koin graph verification when you changed bindings, module includes, or worker registration.
- Use behavioral unit tests when you changed repository, mapper, interactor, or ViewModel logic.
- Use both when the work changes wiring and behavior.

## Guidance from current Koin testing docs

Koin's current testing guidance includes:

- `KoinTest` for injection helpers in tests
- `MockProviderRule` for mock creation during module verification
- `declareMock` and `declare` for overriding definitions in tests
- module verification helpers for dry-run dependency resolution

In this repo, prefer the established `checkModules()` pattern unless you are intentionally
modernizing an isolated area and have verified the alternative against the current Koin version in
use.

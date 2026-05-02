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

## Preferred guard for data-module wiring changes

When a refactor changes a binding in `data/**/koin/Modules.kt`, add a focused `src/test` Koin
resolution test for the exact binding you touched.

Use this shape when the change affects factory wiring for writers, mappers, repositories, or embed
mapper dependencies:

1. start Koin with the smallest module set that includes the changed binding
2. provide mocks for unrelated collaborators
3. resolve the changed contract with `get()`
4. assert resolution succeeds

This catches missing `get<ConcreteType>()` bindings in the normal `testDebugUnitTest` path instead
of waiting for the broader `androidTest` graph check or a runtime crash.

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

- Use a focused `src/test` resolution test when you changed a specific binding in a central module,
  especially `:data` factories that wire writers, mappers, repositories, or embed dependencies.
- Use Koin graph verification when you changed bindings, module includes, or worker registration
  and want a broader safety net around the module set.
- Use behavioral unit tests when you changed repository, mapper, interactor, or ViewModel logic.
- Use both when the work changes wiring and behavior.

## Guidance from current Koin testing docs

Koin's current testing guidance includes:

- `KoinTest` for injection helpers in tests
- `MockProviderRule` for mock creation during module verification
- `declareMock` and `declare` for overriding definitions in tests
- module verification helpers for dry-run dependency resolution

In this repo, do not rely on the broad `checkModules()` pattern alone for central data wiring.
Pair it with a binding-specific unit test when a specific definition changed.

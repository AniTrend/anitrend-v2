---
name: testing-guidelines
description: 'Testing strategy for unit and instrumentation coverage. Use when writing tests for DataState flows, repositories, ViewModels, and WorkManager logic.'
---

# Skill: Testing Guidelines

## Overview

The project is structured so that domain and data layers can be tested with plain JUnit without an
Android device. UI and integration tests belong in `androidTest` source sets and require an emulator
or device.

## Key files to read

- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt` —
  auto-adds JUnit, MockK, Turbine, and coroutines-test to every module's test configurations
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt` —
  shows where WorkManager test utilities are referenced (currently commented out)

## Unit tests (`test/`)

- Mirror the production package name inside `src/test/kotlin/`.
- Use **JUnit 4** as the base runner.
- Use **MockK** to mock dependencies; prefer mocking interfaces over concrete classes for
  stability across implementation changes.
- Use **Turbine** to assert `Flow` / `DataState` emissions:
  ```kotlin
  dataState.data.test {
      assertEquals(expectedItem, awaitItem())
      cancelAndIgnoreRemainingEvents()
  }
  ```
- Use `kotlinx-coroutines-test` (`runTest`, `TestCoroutineDispatcher`) for any suspend-function
  tests.
- All test dependencies are injected automatically — no manual `testImplementation` declarations
  needed for the standard stack.

## Instrumented tests (`androidTest/`)

- Use **Espresso** for UI interaction on critical flows.
- Use Koin test modules to inject mocks or fakes into the DI graph during tests.
- Document emulator/device requirements in the PR description.

## WorkManager tasks

- Test worker logic by calling the internal logic directly (without the WorkManager runtime) where
  possible.
- Use `WorkManagerTestInitHelper` for end-to-end worker tests if needed.

## Running tests

```bash
# Unit tests (all modules, debug variant)
./gradlew testDebugUnitTest --no-daemon

# Instrumented tests (requires connected device/emulator)
./gradlew connectedDebugAndroidTest --no-daemon

# Check formatting before submitting
./gradlew spotlessCheck
```

## Best practices

- Write pure functions in use cases to simplify unit testing.
- Do not remove or modify existing tests to make a PR pass; fix the underlying code instead.
- Include test results or a brief pass/fail note in the PR checklist.

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
- `.agents/skills/mockk-testing-patterns/SKILL.md` — repo-specific MockK workflow, decision
  points, plus extended examples for flow assertions, worker logic coverage, and Koin module validation
- `.agents/skills/koin-module-wiring/SKILL.md` — binding patterns and module aggregation rules
- `.agents/skills/testing-guidelines/references/koin-testing.md` — Koin test primitives and the
  repo's current DI graph verification pattern

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
- Use `kotlinx-coroutines-test` (`runTest`, `StandardTestDispatcher`, or
  `UnconfinedTestDispatcher`) for any suspend-function tests.
- All test dependencies are injected automatically — no manual `testImplementation` declarations
  needed for the standard stack.

## Source set decision

- Place tests in `src/test` by default.
- Move to `androidTest` only when the code under test directly invokes Room, ContentResolver,
  WorkManager scheduling, or another Android runtime dependency that cannot be instantiated in a
  plain JVM test.

## Koin testing

- Use `KoinTest` when the test needs Koin-backed lookup via `get()` or `by inject()`.
- For data-layer DI changes, add a focused `src/test` resolution test for the changed binding or
  writer path. Start Koin with only the module(s) that directly declare or aggregate the binding
  under test, provide mocks for unrelated collaborators, and assert that the concrete binding
  resolves successfully.
- Use the `androidTest` graph-check pattern from
  `data/src/androidTest/kotlin/co/anitrend/data/android/koin/ModulesTest.kt` as a secondary
  regression guard for central `:data` wiring and app-wide aggregators.
- When a test needs to replace one binding inside an active Koin context, prefer Koin test
  utilities such as `declareMock` or `declare` instead of editing production modules just for test
  coverage.
- Use Koin graph checks to validate that module wiring resolves correctly; use regular unit tests
  to validate behavior of the resolved objects.
- Keep graph verification narrow. Load only the module(s) that directly declare or aggregate the
  binding under test, plus the minimum stub modules needed to satisfy its declared dependencies.
- If you add or refactor a module aggregator, writer factory, embed mapper dependency, or worker
  binding, add or update a Koin validation test alongside it.

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
- For offline-first fixed-size cached reads, add at least one unit test that proves the local
  entity converter or mapper preserves the UI-facing fields the screen depends on.
- When a mapper intentionally persists empty collections to avoid repeat fetches, cover that path
  with a unit test or an integration-style mapper test.
- If a cache key varies by request shape, add an assertion around the variant identity or the
  converter output that makes the distinction explicit.
- For dependency wiring work, add a Koin validation test so broken bindings fail before runtime.
- For central `:data` module wiring, prefer a binding-specific unit test in `src/test` that proves
  the changed definition resolves, then keep the broader graph check as additional coverage.
- Do not remove or modify existing tests to make a PR pass; fix the underlying code instead.
- Include test results or a brief pass/fail note in the PR checklist.

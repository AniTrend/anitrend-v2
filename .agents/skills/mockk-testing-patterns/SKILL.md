---
name: mockk-testing-patterns
description: 'Repo-specific MockK testing workflow for AniTrend. Use when writing or reviewing unit tests or androidTest coverage with MockK, deciding between every/coEvery or verify/coVerify, checking Koin modules with mock providers, or confirming whether MockK dependencies need to be added manually. Covers buildSrc auto-wiring, coroutine-aware stubbing, and concrete AniTrend test examples.'
argument-hint: '[what are you testing, for example: converter, mapper, use case, ViewModel, worker, or Koin module]'
---

# MockK Testing Patterns

## When to Use

- You are adding or reviewing AniTrend tests that rely on MockK.
- You need to decide whether a test belongs in `test` or `androidTest`.
- You need the correct MockK primitive for a suspend call, regular function, or DI graph check.
- You want examples that match AniTrend's buildSrc-managed test stack instead of adding ad hoc dependencies.

## What This Skill Produces

- A test in the correct source set.
- MockK usage aligned with AniTrend's existing JUnit, coroutine-test, Turbine, and Koin setup.
- Minimal mocking at the boundary under test instead of mocking value objects or the class under test.
- Verification steps that prove the test actually exercised the intended behavior.
- Guidance for common AniTrend extensions: `Flow` or `DataState` assertions, worker logic coverage, and test review checks.

## Core Rules

1. Start with `src/test` unless the code needs Android framework runtime behavior.
2. Do not add `testImplementation` or `androidTestImplementation` for MockK in normal modules. `buildSrc` already injects MockK, JUnit, Turbine, and coroutine-test.
3. Prefer real domain models, entities, and params when construction is cheap. Mock collaborators, not simple value objects.
4. Use `every` and `verify` for regular calls.
5. Use `coEvery` and `coVerify` for suspend calls.
6. Pair MockK with Turbine when validating `Flow` or `DataState` emissions.
7. Use Koin's `MockProviderRule.create { mockk() }` for DI graph tests when the test is in
   `androidTest` and validates the real module graph.
8. Finish with meaningful assertions or verification, then use `confirmVerified` when interaction coverage matters.

## Source set decision

- Place tests in `src/test` by default.
- Move to `androidTest` only when the code under test directly invokes Room, ContentResolver,
  WorkManager scheduling, or another Android runtime dependency that requires a device or emulator
  to instantiate.

## Workflow

1. Identify the test shape.
   Use a pure unit test for converters, mappers, state holders, use cases, and most repository helpers.
   Use `androidTest` only when Android APIs, startup wiring, or integration behavior requires it.
2. Check the closest repo reference before inventing a pattern.
   Read [project wiring](./references/project-wiring.md) and [MockK API notes](./references/mockk-api-notes.md)
   for where MockK is injected and how the repo currently stubs collaborators.
3. Choose the smallest useful mock surface.
   Mock interfaces, gateways, DAOs, helpers, or repositories.
   Keep data fixtures real unless the type is too expensive or awkward to construct.
4. Stub with the matching API.
   Use `every { ... } returns ...` for synchronous members.
   Use `coEvery { ... } returns ...` for suspending collaborators.
   Use `just Runs` for non-suspending functions that return `Unit`. Use `just Awaits` for
   suspending functions that return `Unit`. For all other return types, provide an explicit return
   value with `returns`.
5. Execute under the standard test runtime.
   Use `runTest` for suspend paths.
   Use Turbine for `Flow` assertions.
6. Verify the contract.
   Assert returned state first.
   Add `verify` or `coVerify` for collaborator interactions that define the behavior.
7. Run the narrowest relevant command.
   Prefer module-targeted tests while iterating, then run the wider debug unit suite before closing the work.

## Extended Directions

### `Flow` and `DataState`

- Use MockK to control upstream collaborators.
- Use Turbine to assert emissions.
- Assert every emission that a downstream collector would act on, typically `Loading`, `Success`,
  and `Error`. Skip internal buffering or operator-level intermediate values that are not part of
  the public contract.
- If the stream is long-lived, end with `cancelAndIgnoreRemainingEvents()` or use `expectMostRecentItem()` when that matches the contract better.
- Start with a unit test when the flow can be exercised without Android runtime. Use `androidTest`
   only when the source depends on Room, a `ContentResolver`, or another Android integration point.

### Worker logic

- Prefer testing the worker's core decision path directly instead of booting the full WorkManager runtime.
- If a worker mixes parameter decoding, interactor invocation, and result mapping, extract the result-mapping logic behind a small seam and unit test that seam with MockK.
- Reserve `WorkManagerTestInitHelper` for the smaller set of cases that truly require end-to-end scheduling or runtime behavior.
- For AniTrend's current task modules, this is the recommended pattern because the repo has many coroutine workers but little value in heavy scheduling tests for each one.

### Review checklist

- Ask whether the test would still pass if the production behavior regressed in the way you care about.
- Remove mocks for simple data carriers if a real instance is cheaper and clearer.
- Replace broad `any()` matchers with concrete values when those values are part of the contract.
- Verify only behavior that matters externally.
- Check that the test name describes the scenario and the expected outcome.

## Decision Points

| Situation | Use |
|---|---|
| Regular function or property | `every` and `verify` |
| Suspend function | `coEvery` and `coVerify` |
| Fire-and-forget dependency call | `just Runs` with `verify` |
| `Flow` or `DataState` output | MockK plus Turbine |
| Koin module validation | `MockProviderRule.create { mockk() }` |
| Need partial real behavior | `spyk`, but only when a focused seam is not practical |

## Completion Checks

- The test source set matches the runtime requirement.
- No redundant MockK dependency was added to the module build file.
- The mock type is a collaborator, not the subject under test.
- Suspend and non-suspend APIs use the correct MockK functions.
- The test asserts observable results, not only implementation details.
- Verification is narrow enough to avoid brittle tests.
- The relevant Gradle test task was run.

## Resources

- [Project wiring](./references/project-wiring.md)
- [MockK API notes](./references/mockk-api-notes.md)
- [Flow and DataState notes](./references/flow-and-datastate.md)
- [Worker testing notes](./references/worker-testing.md)
- [Review checklist](./references/review-checklist.md)
- [Unit test example](./assets/example-unit-test.kt)
- [Flow test example](./assets/example-flow-test.kt)
- [Worker-focused test example](./assets/example-worker-test.kt)
- [Koin module test example](./assets/example-koin-modules-test.kt)

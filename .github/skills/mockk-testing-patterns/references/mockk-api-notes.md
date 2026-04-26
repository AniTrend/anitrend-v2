# MockK API Notes

This is a repo-focused summary of MockK usage patterns that appear most often in AniTrend.

## Stubbing

```kotlin
every { formatter.format(value) } returns "formatted"
coEvery { repository.getById(id) } returns expectedItem
```

- Use `every` for regular calls.
- Use `coEvery` for suspend functions.
- Prefer concrete return values over broad argument matchers unless the argument is not relevant.

## Verification

```kotlin
verify(exactly = 1) { formatter.format(value) }
coVerify(exactly = 1) { repository.getById(id) }
confirmVerified(formatter, repository)
```

- Verify only interactions that define the contract.
- Avoid over-verifying incidental internal calls.

## Relaxed Mocks

```kotlin
val logger = mockk<Logger>(relaxed = true)
```

- Use relaxed mocks for dependencies whose return values do not matter.
- Do not use relaxed mocks when the returned value drives the behavior under test.

## Capturing Arguments

```kotlin
val slot = slot<SaveParams>()
every { dao.upsert(capture(slot)) } returns Unit
```

- Capture when the output is passed into a collaborator and cannot be asserted directly.
- Prefer direct state assertions when possible.

## Spies

```kotlin
val subject = spyk(realObject)
```

- Use `spyk` sparingly.
- Prefer extracting a seam or mocking a collaborator before spying on the subject.

## Coroutine and Flow Tests

- Wrap suspend tests in `runTest`.
- Combine MockK with Turbine when the result is a `Flow` or `DataState` stream.
- Stub upstream collaborators with MockK, then assert downstream emissions with Turbine.

## Koin Graph Checks

```kotlin
@get:Rule
val mockProviderRule = MockProviderRule.create { mockk() }
```

- This is the default AniTrend pattern for `checkModules` coverage.
- Reach for this before introducing custom fake bindings unless the test needs explicit behavior.

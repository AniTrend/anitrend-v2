# Review Checklist

Use this when reviewing AniTrend tests that use MockK.

## Contract quality

- Does the test name describe both the scenario and the expected result?
- Would the test fail if the intended behavior regressed?
- Are the assertions on observable state or outputs first, with interaction checks only where needed?

## MockK quality

- Is the subject under test real?
- Are value objects real where construction is cheap?
- Are `every` and `coEvery` used on the correct call types?
- Are `verify` and `coVerify` narrow enough to avoid brittleness?
- Are relaxed mocks limited to dependencies whose return values do not drive behavior?

## Repo alignment

- Does the test stay in `test` unless Android runtime is actually required?
- Did the author avoid adding redundant MockK dependencies to the module build file?
- If the code is flow-based, would Turbine express the contract more clearly than manual collection?
- If the code is worker-based, could the logic be tested directly without full WorkManager runtime setup?

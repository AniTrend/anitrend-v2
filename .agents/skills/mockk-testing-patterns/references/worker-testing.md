# Worker Testing Notes

AniTrend has many task workers built on `SupportCoroutineWorker`, for example:

- `task/news/src/main/kotlin/co/anitrend/task/news/component/NewsWorker.kt`
- `task/review/src/main/kotlin/co/anitrend/task/review/component/ReviewSaveEntryWorker.kt`
- `task/favourite/src/main/kotlin/co/anitrend/task/favourite/component/MediaFavouriteWorker.kt`

## Preferred approach in this repo

- Unit test the logic that determines success, retry, or failure.
- Mock the interactor and any settings or parameter providers.
- Keep WorkManager runtime coverage narrow and intentional.

This matches the existing repo testing guidance: call worker logic directly where possible, and only use `WorkManagerTestInitHelper` for the smaller set of scenarios that require runtime validation.

## What to extract when a worker gets awkward to test

- Parameter decoding from `inputData`
- Mapping `LoadState` or repository output to `Result`
- Retry or failure branching rules

## Example decision path

For `NewsWorker`, the contract is roughly:

1. Read locale settings.
2. Build `NewsParam`.
3. Invoke the interactor.
4. Wait for success or error load state.
5. Return `Result.success()` or `Result.failure()`.

The high-value unit test focuses on steps 3 to 5. It does not need full WorkManager scheduling.

# data/android/

## Responsibility

`data/android/` is a minimal Android data module shell. In this checkout it contains an Android manifest but no Kotlin package tree.

## Design Patterns

- Empty implementation module shape with Android packaging metadata.
- No repository, source, mapper, cache, or network implementation currently lives here.

## Data & Control Flow

There is no runtime data flow in this module based on the inspected files.

## Integration Points

- Sits under the broader `data/` module grouping.
- Shared Android data infrastructure is implemented in `data/core/src/main/kotlin/co/anitrend/data/android/`, not here.

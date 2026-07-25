# android/core/src/main/kotlin/co/anitrend/android/core/recycler/

## Responsibility

This package contains RecyclerView support classes for carousel presentation, item binding models, spacing decorators, and selection behavior.

## Design Patterns

- Item binding models keep adapter item metadata reusable.
- Decorators centralize spacing rules.
- Selection mode helpers standardize selectable list behavior.
- Carousel recycler helper supports horizontally oriented content surfaces.

## Data & Control Flow

Feature lists and adapters apply decorators, selection mode, and carousel helpers to configure RecyclerView behavior around existing item adapters.

## Integration Points

- Used by legacy feature list UI and drawer adapters.
- Depends on shared resources and dimensions from `android/core/src/main/res/`.

# android/core/src/main/kotlin/co/anitrend/android/core/shortcut/

## Responsibility

This package creates Android app shortcuts for supported destinations.

## Design Patterns

- Shortcut controller contract hides `ShortcutManager` access.
- Shortcut models describe available app shortcut targets.
- App startup decides when shortcut creation should run.

## Data & Control Flow

`ApplicationInitializer` checks first install state, resolves `IShortcutController`, and requests shortcut creation. The controller translates shortcut models into platform shortcut info.

## Integration Points

- Bound in `android/core/koin/Modules.kt`.
- Used by `app/src/main/kotlin/co/anitrend/initializer/ApplicationInitializer.kt`.
- Uses drawable resources from `android/core/src/main/res/drawable/`.

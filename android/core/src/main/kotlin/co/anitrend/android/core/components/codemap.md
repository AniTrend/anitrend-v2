# android/core/src/main/kotlin/co/anitrend/android/core/components/

## Responsibility

This package contains reusable Android view components for sheets, floating action menus, and custom edge treatments.

## Design Patterns

- Bottom sheet callbacks and action contracts decouple drawer and sheet surfaces from direct widget state.
- Floating action menu contracts define common behavior for menu style action groups.
- Shape edge treatments isolate custom Material shape rendering.

## Data & Control Flow

Components receive lifecycle or motion callbacks, dispatch slide and state change actions, and expose UI behavior to app shell or drawer code through small contracts.

## Integration Points

- `android/navigation` drawer implements sheet action contracts.
- `app/src/main/kotlin/co/anitrend/component/action/` uses these contracts to update the main shell FAB and menu state.

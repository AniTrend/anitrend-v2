# android/core/src/main/kotlin/co/anitrend/android/core/views/

## Responsibility

This package contains shared View system utilities and custom views.

## Design Patterns

- Binding aware view containers simplify custom ViewBinding usage.
- Drawable and text view helpers encapsulate custom rendering.
- Compose view factory bridges View based hosts with Compose content.

## Data & Control Flow

Legacy UI code uses these views and factories when rendering reusable content in XML or mixed Compose and View surfaces.

## Integration Points

- Used by Android platform modules and feature modules that still render View based UI.
- Complements Compose components under `android/core/compose/`.

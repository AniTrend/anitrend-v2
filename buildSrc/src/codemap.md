# buildSrc/src/

## Responsibility

`buildSrc/src/` contains the source sets for AniTrend Gradle build logic. The active implementation lives in `buildSrc/src/main/`, while tests are intentionally outside this codemap scope.

## Design Patterns

- Gradle `buildSrc` source layout with `main` for production build logic.
- Kotlin DSL plugin implementation compiled as part of the build bootstrap.
- Package hierarchy mirrors the build logic roles under `co.anitrend.buildSrc`.

## Data & Control Flow

Gradle compiles this source tree before configuring the root project. The compiled classes expose the AniTrend convention plugin and helper APIs consumed by module build configuration.

## Integration Points

- `buildSrc/build.gradle.kts` defines source set dependencies and Kotlin DSL setup.
- `buildSrc/src/main/java/co/anitrend/buildSrc/` contains the actual convention plugin implementation.

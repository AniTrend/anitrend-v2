# Project Wiring

This skill assumes AniTrend's current build logic and test stack.

## Where MockK Comes From

- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt`
  adds `test(project.libs.mockk)` for unit tests.
- The same file adds `androidTest(project.libs.mockk.android)` for instrumentation tests.
- The same strategy also injects JUnit, Turbine, Koin test support, and `kotlinx-coroutines-test`.

Result: most modules do not need manual MockK dependency declarations.

## Version Source

- `gradle/libs.versions.toml` declares the MockK catalog entry and version.

If you need to change MockK itself, update the version catalog and let `buildSrc` consume it.

## Existing AniTrend References

- `data/feed/src/test/kotlin/co/anitrend/data/feed/episode/converter/EpisodeModelConverterTest.kt`
  uses `every` with a mocked model interface and plain assertions.
- `feature/medialist/editor/src/test/kotlin/co/anitrend/medialist/editor/component/compose/state/MediaListEditorStateTest.kt`
  shows focused helper mocking with real domain fixtures.
- `data/src/androidTest/kotlin/co/anitrend/data/android/koin/ModulesTest.kt`
  shows `MockProviderRule.create { mockk() }` for Koin graph verification.

## Default Test Commands

Use the smallest command that answers the question while iterating.

```bash
./gradlew --no-daemon :module:path:testDebugUnitTest
./gradlew --no-daemon testDebugUnitTest
./gradlew --no-daemon connectedDebugAndroidTest
```

Use the full debug unit suite before claiming the testing work is finished.

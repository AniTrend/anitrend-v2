# spotless/

## Responsibility

`spotless/` contains license header templates consumed by the Gradle Spotless configuration in `buildSrc`. It keeps Kotlin, Kotlin Gradle script, and XML copyright headers consistent across formatted source files.

Key files:

- `spotless/copyright.kt` is the Kotlin source license header template.
- `spotless/copyright.kts` is the Kotlin Gradle script license header template.
- `spotless/copyright.xml` is the XML license header template.
- `spotless/copyright.properties` supports property file license handling where configured.

## Design Patterns

- Template file pattern with `$YEAR` substitution managed by Spotless.
- Separate templates per file type so comment syntax matches Kotlin, Kotlin script, XML, and properties formats.
- Root shared formatting resource, referenced from build logic instead of duplicated per module.

## Data & Control Flow

`buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectSpotless.kt` resolves these files from `rootProject.file("spotless/copyright$extension")`. Spotless applies them to Kotlin, Kotlin Gradle, and XML targets, while excluding build output, tests, and selected vendored source paths.

## Integration Points

- `ProjectSpotless.kt` is the only build logic consumer documented in this scope.
- `.editorconfig` provides ktlint details used alongside these templates.
- `./gradlew spotlessApply` and `./gradlew spotlessCheck` apply or validate the configured headers.

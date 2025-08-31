---
applyTo: **
description: This file provides guidelines for using and extending the architecture in AniTrend v2.
---

# Using and Extending the Architecture

When developing new features or modifying existing ones, it’s important to **follow the established architectural patterns**: - **Create or use Domain UseCases**: Business logic should reside in a use-case (interactor) class in the domain layer. If you find yourself writing substantial logic in a ViewModel or data layer, consider moving it to a use case so it can be reused and tested. For a new feature, create a `FeatureXUseCase` in the `:domain` module (or add to an existing domain package) that defines the operations. Use the result wrapper `UiState<T>` or a relevant type to represent its output (to leverage loading/success/error handling). - **Define Interfaces and Implement in Data**: Communication between domain and data is via interfaces. For any new data source or repository, define an interface in `:domain` (e.g., `IXxxRepository`). Then implement that in the `:data` layer (e.g., class `XxxRepository : IXxxRepository`). This decoupling allows easy testing and swapping data sources if needed. - **Leverage Support Libraries**: The project’s support-arch library provides many base classes and utilities: - If you need a ViewModel that tracks loading state, consider using the provided `UiState` and `DataState` mechanisms rather than inventing your own LiveData. Likely there are base view model classes or LiveData/Flow extensions in support-arch to help coordinate UI state. - Use `DataState` for streams of data that can be refreshed. For example, if you create a new repository fetching from network and caching in database, return a `DataState<YourType>` so the UI can easily attach a swipe-to-refresh and error retry. - The support-arch `request` module possibly has utilities for performing network requests with standardized error handling (like a `RequestCallback` or `ControllerStrategy` as seen in GraphQLController[79][80]). Use those patterns to maintain consistency in how errors are logged and propagated. - UI components: if implementing a new screen, see if a **base fragment or composable** exists. E.g., `SupportFragmentList` might provide a template for a list screen with built-in loading visuals. In Compose, there might be a standard way they handle loading and error states (perhaps a common composable or the `StateLayoutConfig` provided via Koin[37]). Reuse these instead of creating custom loaders each time. - **Keep Modules Focused**: Place classes in the correct module. UI-related classes (Activities, Fragments, Composables, ViewModels) go in feature or common-ui modules. Pure Kotlin logic or anything not needing Android should go in domain or data. If something from the data layer is needed across multiple features (say a parser or a util), consider putting it in `:data:common` or creating a new common module. This ensures the dependency graph remains acyclic and logical.

## Coding Conventions and Style

- **Kotlin Style**: The project uses Ktlint/Spotless for formatting. Write code in idiomatic Kotlin (e.g., use scope functions, favor immutable vals, use data classes for models). Before committing, run the format task or set up your IDE with Kotlin style guidelines to avoid CI failing on style checks.
- **Naming**: Follow existing naming conventions. e.g., Use `SomethingUseCase` or `SomethingInteractor` for domain use cases (the project seems to use both suffixes in different places; maintain consistency within a domain package). Repository interfaces are prefixed with `I` (e.g., `IConfigRepository`), and implementations typically drop the I or have a descriptive name (e.g., `ConfigRepository`).
- **Comments and Documentation**: Many classes have KDoc comments explaining their purpose, and there’s generation of documentation via Dokka. Continue this practice. Write clear KDoc for any new public class or function, especially in domain and data layers. This helps others (and AI tools) understand usage.
- **Error Handling**: Use the existing error handling patterns. For example, network errors are likely encapsulated in a `RequestError` (as seen thrown in GraphQLController on error)[80]. If writing a new network call, use the provided controller or at least propagate errors in a consistent way (so that the UI can display a user-friendly message via the common load state observer).
- **Threading**: Coroutines are used throughout. Ensure you call suspend functions appropriately and dispatch heavy work to background threads (the project likely provides an `ISupportDispatcher` or uses Dispatchers.IO via Koin[81]). When writing a repository, wrap network and DB calls in `withContext(Dispatchers.IO)` if not using a provided helper that already does so. The support-arch and data layer classes (like DefaultMapper, GraphQLController) often handle threading internally (e.g., mapping and inserting into DB on IO thread)[82]. Utilize those instead of duplicating logic.
- **Immutability and StateFlow**: In ViewModels or Compose states, prefer `StateFlow` or `Immutable data classes` for state. If using LiveData, stick to unidirectional flow: ViewModel exposes LiveData, UI observes. Given the Compose usage, you might see a shift to StateFlow + `collectAsState()` in composables. Follow whatever pattern the existing Compose screens use (check a feature like media discover or profile).
- **Compose Best Practices**: If adding Composables:
- Make them small and focused, with preview functions if possible.
- Use theming (colors, typography) from the provided MaterialTheme (the project integrates Material3).
- Remember to handle state hoisting – view state should come from ViewModel (which may combine DataState flows).
- Use accompanist libraries included for things like system UI controller, pager, etc., rather than writing from scratch.
- **Resource Strings and Localization**: All user-facing strings should be in `strings.xml`. The project likely has multi-language support (the README mentions POEditor for translations[83]). When adding text, add an English entry to the appropriate `strings.xml`. Do not hard-code strings in code. Similarly, use dimension and style resources for spacing and text appearance, consistent with Material guidelines.

### Internationalization (i18n) and Strings Documentation

You MUST document each string resource clearly to aid translators and maintainers and to avoid misuse in UI code.

- Per-string comments: Place a concise XML comment directly above EVERY `<string>` and each `<plurals>` block describing where it’s used and what it represents.
	- Example: `<!-- Label for a single episode (e.g., in metadata chips) -->` before `<string name="label_episode_singular">Episode</string>`.
- Placeholders: When a string uses format arguments, you MUST document each placeholder and provide an example output.
	- Use ordered placeholders (`%1$d`, `%2$s`) when there is more than one argument.
	- Include an example: `<!-- "%1$d Episodes" e.g., "12 Episodes"; %1$d is an integer -->`.
- Plurals: Always use `<plurals>` for quantities and document the expected forms with examples.
	- Provide examples for `one` and `other` at minimum; add more quantities if languages require it.
	- Ensure code passes the correct quantity (e.g., `count`) matching the plural resource.
- Non-translatable content: Mark strings as `translatable="false"` when their content is a fixed abbreviation/symbol or should never be localized.
	- Document the reason: `<!-- Not translatable because abbreviation and separator are fixed -->`.
- Dates and times: If a string includes a date/time placeholder, note that the formatted value MUST be provided already localized by code.
	- Example: `<!-- "Airs %s"; %s is a localized full date string provided by code -->`.
- Consistent tone and capitalization: Keep capitalization and tone consistent with Material guidelines and existing modules.
- No hard-coded text in code: UI text MUST come from resources; add new keys to the correct module’s `strings.xml`.
- Review: When editing strings, run a quick module build to validate XML structure and resource compilation.

This practice is exemplified in `common/media/src/main/res/values/strings.xml`, where every resource includes a focused comment clarifying purpose, placeholders, and formatting.

## Database: Room join tables and migrations

### Room join-table best practices

- Scope note: These guidelines apply when a table uses a surrogate primary key that Room autogenerates (typical for join/connection tables and local-only caches). They do not apply to entities that use server-provided/natural IDs as the primary key, nor to tables that define a composite primary key without a surrogate `id` column.

- Use nullable surrogate PKs for auto-generated IDs: declare `@PrimaryKey(autoGenerate = true) val id: Long? = null` (or Int?). Room only treats a value as “to be generated” when it’s NULL; a non-null default like 0 is considered a concrete PK and will break autoincrement semantics.
- Define a composite unique index for the logical relationship: for many-to-many tables declare `@Index(value = ["left_id", "right_id"], unique = true)` (e.g., `(tag_id, media_id)` or `(genre_id, media_id)`).
- Upsert by composite uniqueness, not by surrogate PK: add DAO `@Insert(onConflict = REPLACE)` methods for batch upserts; pass entities keyed by the composite columns so duplicates replace the correct logical row.
- Keep mappers simple and deterministic: in mappers’ `persist` steps, call the DAO batch upsert for connection entities rather than persisting by surrogate PK.
- Avoid non-null ID contracts: do not require non-null `id` in interfaces or models for rows whose IDs are DB-generated; this ensures inserts go through with NULL.

### Database migration checklist

When making schema-impacting changes:
- Bump `DATABASE_SCHEMA_VERSION` in the Room database and declare the appropriate `@AutoMigration(from = X, to = Y)` or provide a manual migration if needed.
- Export and inspect the schema JSON in `data/schemas/.../AniTrendStore/<version>.json`; verify column nullability, indices, and identity hash changes are expected.
- Build app/module to validate annotation processing and schema export run cleanly.
- Perform a runtime smoke test on an older on-device DB (from `from` version) to ensure migration applies without crashes.
- If changing join tables, confirm multiple relationship rows persist and read back correctly (no collapsing to a single row).
- Update documentation (this guide) and note changes in the PR, attaching a brief schema diff summary.

## Testing Guidelines

- The structure allows for testing domain and data layers easily. For any critical logic (parsers, complex use case), add unit tests in the corresponding module. Use **JUnit4** and **MockK** (both are included)[69][70]. For coroutine flows, use the Turbine library to test emission of flows.
- Android UI tests (Espresso) can be written for critical flows. There is likely a setup for instrumented tests (androidTest) in some modules, including Koin test modules. Leverage the Koin test features to inject mocks if needed.
- WorkManager tasks can be tested using WorkManager’s testing utils or by invoking the logic inside workers directly. The project included WorkManager testing dependencies commented out (there’s a reference in ProjectDependencies to a potential work test lib, commented)[84].

## Workflow and Contribution Tips

- **Gradle Sync/Build Performance**: Because there are many modules, Gradle sync can be slow. Use the included `settings.gradle.kts` configuration – it might have some Gradle feature flags. You can also compile only a subset of modules if working on them (e.g., include only specific modules to speed up, using Gradle’s `x` or selective include tricks).
- **Modifying Build Scripts**: If you change something in `buildSrc` (like adjusting a dependency pattern or adding a new configuration), remember to reload Gradle. buildSrc changes will recompile the build logic. Mistakes in buildSrc can affect the entire build, so test on a sample module if possible.
- **Adding a Dependency**: As noted, prefer to add in libs.versions.toml and then, if it’s widely used, integrate it in ProjectDependencies for the appropriate modules. For example, if you add a new Firebase library that all features should have, add the `libs.firebase.foo` and then modify `applyFeatureModuleGroupDependencies` to include it. If it’s only for one module, you can add in that module’s build.gradle, but consider if other modules might benefit (for consistency).
- **Updating support-arch or other AniTrend libs**: These are fetched from JitPack using a version in libs.versions.toml[41][85]. If you update those libraries, ensure compatibility (e.g., support-arch version bump might deprecate some functions). Run tests and sanity checks after updating.
- **Proguard**: If adding new libraries that use reflection or serialization (e.g., Moshi, but here they use Kotlinx Serialization which usually doesn’t need special rules), update `proguard-common.pro`. Also add consumer rules for any library module if needed.
- **Logging**: Use **Timber** for logging debug information. Avoid using System.out or Log.d directly. For new logs, pick appropriate log levels and guard verbose logs (maybe behind a debug flag if they are too chatty).
- **Analytics**: If you implement a new user interaction, consider if an analytics event is needed. The app likely uses Firebase Analytics for some user actions. They might have an Analytics helper (maybe in support-arch analytics). Follow the pattern to log events (e.g., when user adds to favorites, send an analytics event). Keep it flavor-aware (only log when analytics is enabled).

## Getting Help from the Code

The codebase itself is a guide: - Look at similar implementations: e.g., adding a new filter for media? See how Tag filters or Genre filters are done (they have QueryFilter classes). - Need to call an API? See how GraphQL queries are structured in existing sources (search for usages of `aniListApi()` or how retrofit interfaces are defined). - Use the GitHub repository’s issue tracker (if available) to see if there are discussions or decisions documented about certain implementations.

Lastly, **stay organized**. This project is large, but its consistency is a strength. Maintain that consistency with any contributions. This makes it easier for tools like Copilot or future developers to predict where things should go and how they should look. For example, if Copilot sees a pattern of function documentation and naming, it will likely generate new code following suit – so keeping the code homogeneous is mutually beneficial for AI assistance and human understanding.

---

## References

[37]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt#L64-L72
[41]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/gradle/libs.versions.toml#L28-L36
[69]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt#L36-L44
[70]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt#L76-L84
[79]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt#L52-L60
[80]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt#L69-L76
[81]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt#L152-L160
[82]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt#L78-L85
[83]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/README.md#L53-L61
[84]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt#L381-L384
[85]: https://github.com/AniTrend/anitrend-v2/blob/4931af9c33f9bb986507ef3a61a634aeed01550e/gradle/libs.versions.toml#L191-L200

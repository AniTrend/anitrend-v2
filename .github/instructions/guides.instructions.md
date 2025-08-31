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
- Use theming (colors, typography) from the provided MaterialTheme (the project integrates Material3 -> `AniTrendTheme3` and `PreviewTheme`).
- Remember to handle state hoisting – view state should come from ViewModel (which may combine DataState flows).
- Use accompanist libraries included for things like system UI controller, pager, etc., rather than writing from scratch.
- **Resource Strings and Localization**: All user-facing strings should be in `strings.xml`. The project likely has multi-language support (the README mentions POEditor for translations[83]). When adding text, add an English entry to the appropriate `strings.xml`. Do not hard-code strings in code. Similarly, use dimension and style resources for spacing and text appearance, consistent with Material guidelines.
- **Import Clarity**: Avoid wildcard imports. The formatter should handle this, but ensure your IDE is set to not use `*` imports for clarity, and avoid using inline references when you can import the class directly, the only exceptions are for `R` classes and static imports, that are nested classes (in that case import the parent class).

## String Resource Naming Conventions

**CRITICAL**: Follow consistent naming patterns for string resources to maintain codebase coherence and enable AI/tooling assistance. Use these prefixes based on the string's semantic purpose:

### Naming Pattern Structure
`{prefix}_{module_or_context}_{specific_identifier}`

### Standard Prefixes
- **`label_`** - Field labels, section headers, descriptive text (e.g., `label_media_list_editor_watch_status`)
- **`title_`** - Screen titles, dialog titles, major headings (e.g., `title_media_list_editor_add_to_library`)
- **`subtitle_`** - Secondary headings, descriptive subtitles (e.g., `subtitle_media_list_editor_manage_media`)
- **`placeholder_`** - Input hints, empty state text (e.g., `placeholder_media_list_editor_select_status`)
- **`action_`** - Button text, menu items, actionable text (e.g., `action_media_list_editor_create_new_list`)
- **`message_`** - User messages, notifications, feedback text (e.g., `message_sync_complete`)
- **`error_`** - Error messages, validation messages (e.g., `error_network_unavailable`)
- **`hint_`** - Helper text, tooltips, guidance (e.g., `hint_swipe_to_refresh`)
- **`description_`** - Accessibility descriptions, detailed explanations (e.g., `description_favorite_button`)

### Module Context Guidelines
- Use **underscores** to separate words: `media_list_editor` not `medialisteditor`
- Be specific but concise: `media_list` not `medialist`, `episode_progress` not `progress`
- Include feature/module context when strings are feature-specific
- For common/shared strings, use generic context: `label_loading`, `action_save`, `error_network`
- Use `formatted="true"` attribute for strings with parameters (e.g., `%1$s`, `%1$d`), as these will be formatted in code

### Examples of Good vs Bad Naming

**GOOD:**
```xml
<string name="label_media_list_editor_watch_status">Watch Status</string>
<string name="title_profile_settings">Profile Settings</string>
<string name="placeholder_search_anime_manga">Search anime and manga...</string>
<string name="action_mark_as_watched">Mark as Watched</string>
<string name="error_authentication_failed">Authentication failed</string>
```

**BAD:**
```xml
<string name="medialist_editor_watch_status">Watch Status</string>  <!-- Missing prefix -->
<string name="profileSettingsTitle">Profile Settings</string>        <!-- CamelCase, wrong prefix -->
<string name="searchHint">Search anime and manga...</string>         <!-- Generic, unclear purpose -->
<string name="watchedButton">Mark as Watched</string>               <!-- Context unclear -->
<string name="authError">Authentication failed</string>             <!-- Too abbreviated -->
```

### Migration Guidelines
- When updating existing string resources, prefer the new naming convention
- Add comments noting replacements: `<!-- Replaces old_string_name -->`
- Update all references when renaming strings
- Check that translations and plurals follow the same naming pattern

### POEditor Integration for Community Translations
**REQUIRED**: Always add descriptive comments above string resources to help community translators understand context. POEditor automatically picks up these comments and displays them to translators.

**Format**: Use XML comments immediately before the string resource:
```xml
<!-- Displayed when user hasn't set a rating yet -->
<string name="placeholder_media_score_section_rating">Not rated</string>

<!-- Button to save changes to user's anime/manga list -->
<string name="action_media_list_editor_save_changes">Save Changes</string>

<!-- Shows current episode progress out of total episodes -->
<string name="label_media_list_editor_progress_percentage">Progress %1$d%%</string>
```

**Guidelines for effective translator comments**:
- **Context**: Explain where/when the string appears in the app
- **Purpose**: Describe what action or information the string represents
- **Variables**: Explain what `%1$s`, `%1$d` parameters represent
- **Tone**: Indicate if the string should be formal, casual, urgent, etc.
- **Character limits**: Note if there are UI space constraints

**Examples**:
```xml
<!-- Title shown at top of screen when adding anime/manga to library -->
<string name="title_media_list_editor_add_to_library">Add to Library</string>

<!-- Error message when network request fails, shown in red text -->
<string name="error_network_unavailable">Network unavailable</string>

<!-- Placeholder text in search box, %1$s will be "anime and manga" -->
<string name="placeholder_search_content" formatted="true">Search %1$s...</string>
```

## Testing Guidelines

- The structure allows for testing domain and data layers easily. For any critical logic (parsers, complex use case), add unit tests in the corresponding module. Use **KotlinTest** and **MockK** (both are included)[69][70]. For coroutine flows, use the Turbine library to test emission of flows.
- Android UI tests (Espresso) can be written for critical flows. There is likely a setup for instrumented tests (androidTest) in some modules, including Koin test modules. Leverage the Koin test features to inject mocks if needed.
- WorkManager tasks can be tested using WorkManager’s testing utils or by invoking the logic inside workers directly. The project included WorkManager testing dependencies commented out (there’s a reference in ProjectDependencies to a potential work test lib, commented)[84].
- Always run gradle test using the debug build variant to ensure tests pass and run in no-daemon mode to save memory.
- When mocking, prefer to mock interfaces rather than concrete classes. This keeps tests more stable against implementation changes.

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

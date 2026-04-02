---
name: string-resources-convention
description: 'String resource naming and translator-comment conventions. Use when adding or renaming user-facing strings and maintaining localization consistency.'
---

# Skill: String Resource Naming Conventions

## Naming pattern

`{prefix}_{module_or_context}_{specific_identifier}`

## Standard prefixes

| Prefix | Usage |
|---|---|
| `label_` | Field labels, section headers, descriptive text |
| `title_` | Screen titles, dialog titles, major headings |
| `subtitle_` | Secondary headings, descriptive subtitles |
| `placeholder_` | Input hints, empty state text |
| `action_` | Button text, menu items, actionable text |
| `message_` | User messages, notifications, feedback |
| `error_` | Error messages, validation messages |
| `hint_` | Helper text, tooltips, guidance |
| `description_` | Accessibility descriptions, detailed explanations |

## Module context guidelines

- Use **underscores** to separate words: `media_list_editor` not `medialisteditor`.
- Be specific but concise: `media_list` not `medialist`, `episode_progress` not `progress`.
- Include feature/module context for feature-specific strings.
- Use generic context for shared strings: `label_loading`, `action_save`, `error_network`.
- Add `formatted="true"` for strings with parameters (e.g., `%1$s`, `%1$d`).

## Good vs bad examples

**Good:**
```xml
<string name="label_media_list_editor_watch_status">Watch Status</string>
<string name="title_profile_settings">Profile Settings</string>
<string name="placeholder_search_anime_manga">Search anime and manga...</string>
<string name="action_mark_as_watched">Mark as Watched</string>
<string name="error_authentication_failed">Authentication failed</string>
```

**Bad:**
```xml
<string name="medialist_editor_watch_status">Watch Status</string>  <!-- Missing prefix -->
<string name="profileSettingsTitle">Profile Settings</string>        <!-- CamelCase, wrong prefix -->
<string name="searchHint">Search anime and manga...</string>         <!-- Generic, unclear purpose -->
<string name="watchedButton">Mark as Watched</string>               <!-- Context unclear -->
<string name="authError">Authentication failed</string>             <!-- Too abbreviated -->
```

## POEditor translator comments (required)

Always add an XML comment immediately before each string resource. POEditor displays these to
community translators.

**Format:**
```xml
<!-- Displayed when user hasn't set a rating yet -->
<string name="placeholder_media_score_section_rating">Not rated</string>

<!-- Button to save changes to user's anime/manga list -->
<string name="action_media_list_editor_save_changes">Save Changes</string>

<!-- Shows current episode progress out of total; %1$d is the current episode number -->
<string name="label_media_list_editor_progress_percentage" formatted="true">Progress %1$d%%</string>
```

**Effective comment guidelines:**
- **Context**: where/when the string appears in the app.
- **Purpose**: what action or information it represents.
- **Variables**: what each `%1$s` / `%1$d` parameter means.
- **Tone**: formal, casual, urgent, etc. if relevant.
- **Character limits**: note UI space constraints when applicable.

## Migration guidelines

- Prefer the new naming convention when updating existing string resources.
- Add a replacement comment: `<!-- Replaces old_string_name -->`.
- Update all code references when renaming a string.
- Ensure plurals and translations follow the same naming pattern.

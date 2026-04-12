---
name: string-resource-inline-comments
description: 'Audit Android strings.xml files so every resource has an inline XML comment for POEditor translator context. Use when adding or editing string resources, documenting placeholders, reviewing localization context, or fixing uncommented strings after feature work.'
---

# Skill: Inline Comments for Android String Resources

Use this skill when the task is not just naming strings correctly, but making sure translators get
 enough context from the XML comments that sit directly above each resource block.

## When to Use

- Adding new entries to any `strings.xml` file.
- Editing existing Android string resources during feature work.
- Reviewing PRs for missing translator context.
- Cleaning up files where new strings were added without comments.
- Tightening repo guidance after a missed comment or localization regression.

## Outcome

- Every touched resource block in `strings.xml` has an immediate XML comment.
- Comments explain where the text appears, what it does, and what placeholders mean.
- Repo-level instructions and skills are updated when the gap came from unclear guidance.

For Android platform behavior such as escaping, formatting, arrays, plurals, and styled text,
also consult the official Android guidance: https://developer.android.com/guide/topics/resources/string-resource

## Procedure

1. Open the touched `strings.xml` file and inspect the full edited block, not just the newest line.
2. Add an XML comment immediately above each touched resource block.
3. Write comments for translators, not engineers.
4. Explain placeholders such as `%1$s` or `%2$d`, plus any tone or space constraints.
5. Keep comments one resource above the matching block; do not use one comment for several unrelated entries.
6. Re-read the edited section and confirm no new resource was left uncommented.
7. If the miss happened because repo guidance was weak, update the relevant instruction and string-resource skill in the same change.

## Comment Patterns

```xml
<!-- Button label to retry the failed episode feed request -->
<string name="action_episode_retry">Retry</string>

<!-- Empty-state title shown when no episode releases are available -->
<string name="label_episode_empty_title">No episodes found</string>

<!-- Fallback shown when no summary exists; %1$s is the episode title -->
<string name="message_episode_no_summary" formatted="true">No summary available for %1$s at this time.</string>
```

## Quality Gates

- Every touched resource block has a comment directly above it.
- Comments mention screen, state, or interaction context.
- Placeholder variables are documented.
- Android-specific escaping or plural behavior is checked when the text uses those features.
- Comments are concise but specific enough for POEditor translators.
- XML remains cleanly formatted and easy to scan.

## Common Failure Modes

- Adding a batch of strings and commenting only the first one.
- Leaving a legacy string undocumented because it was already in the file.
- Forgetting that `plurals` and `string-array` blocks also need translator context.
- Writing comments like "String for button" that give no usable localization context.

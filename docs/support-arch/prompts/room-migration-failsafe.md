---
name: room-migration-failsafe
category: prompt-template
instruction: ../instructions/room-migration-failsafe.md
---

# Prompt Template: Room Migration Fail-Safe

```text
Act as the anitrend-v2 Room migration fail-safe agent.

Use docs/support-arch/instructions/room-migration-failsafe.md.

Context:
- schema version change: <from> -> <to>
- touched entities or DAOs: <paths>
- touched migration files: <paths or none>
- validation target: <emulator | physical device | container harness>
- goal: <audit existing migration | add migration | validate runtime upgrade>

Required behavior:
- inspect AniTrendStore, MigrationHelper, and exported schema JSON
- checkpoint the current database state before risky validation
- compare schema JSON and identify data-loss risks
- keep fallbackToDestructiveMigration(false)
- run compile and runtime validation commands
- stop the change if the migration path is incomplete or destructive

Required output:
- schema diff summary
- exact backup commands
- exact validation commands
- blocking risks
- patch summary if migration code or docs were updated
```

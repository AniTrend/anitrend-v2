---
applyTo: data/**
description: Guidelines for contributing to AniTrend's Room database layer, focusing on join tables and migrations.
---

# AniTrend v2 Data Layer Contribution Guidelines

## Reference Routing

This file captures Room-specific policy for `data/**` changes. Pair it with:

- `.agents/skills/room-entity-pattern/SKILL.md` for four-file entity/DAO/mapper/repository structure.
- `.agents/skills/data-state-pattern/SKILL.md` for repository return-type and flow behavior.
- `.agents/skills/cache-request-isolation/SKILL.md` for diagnosing and fixing silent empty-UI bugs caused by CacheRequest collisions.
- `.agents/skills/reference-map/SKILL.md` for cross-cutting navigation.

For docs hygiene, use repo-relative links and validate with
`.github/scripts/audit-instruction-refs.sh`.

## Database: Room join tables and migrations

### Room join-table best practices

- Scope note: These guidelines apply when a table uses a surrogate primary key that Room autogenerates (typical for join/connection tables and local-only caches). They do not apply to entities that use server-provided/natural IDs as the primary key, nor to tables that define a composite primary key without a surrogate `id` column.

- Use nullable surrogate PKs for auto-generated IDs: declare `@PrimaryKey(autoGenerate = true) val id: Long? = null` (or Int?), inherit the interface `IEntity<T>` for the desired type. Room only treats a value as “to be generated” when it’s NULL; a non-null default like 0 is considered a concrete PK and will break autoincrement semantics.
- Define a composite unique index for the logical relationship: for many-to-many tables declare `@Index(value = ["left_id", "right_id"], unique = true)` (e.g., `(tag_id, media_id)` or `(genre_id, media_id)`).
- Upsert by composite uniqueness, not by surrogate PK: add DAO `@Insert(onConflict = REPLACE)` methods for batch upserts; pass entities keyed by the composite columns so duplicates replace the correct logical row.
- Keep mappers simple and deterministic: in mappers’ `persist` steps, call the DAO batch upsert for connection entities rather than persisting by surrogate PK.
- Avoid non-null ID contracts: do not require non-null `id` in interfaces or models for rows whose IDs are DB-generated; this ensures inserts go through with NULL.

### Mapper embed policy

- When a response mapper needs to persist related entities, e.g. `user option`, `notification`, `statistic`, or other sidecar rows, express that work through `EmbedMapper` helpers.
- Do not inject extra Room sources straight into a parent mapper when the write is a related embedded side effect; introduce `XxxEmbed` and keep the persistence rule there, even if it calls a custom DAO method.
- Use the parent mapper only to coordinate `onEmbedded(...)` and `persistEmbedded()` calls, so query and mutation mapper wiring stays uniform.

### CacheRequest isolation rule

`cache_log` identifies entries by `request + cache_item_id` only — there is no `key` column. Two source
variants that share the same `CacheRequest` enum value and the same entity ID will silently collide: the
second source sees the first source's cache row as "fresh" and skips its network request, producing an
empty UI with no error.

**Rule**: every independently-fetchable resource variant must have its own `CacheRequest` enum entry in
`CacheRequest.kt`. Sidecar sources (e.g. Studios, Stats, Relations, Recommendations alongside a Detail
source) must never reuse `CacheRequest.MEDIA`; add `MEDIA_STUDIOS`, `MEDIA_STATS`, etc. and construct
their `MediaCache` inline in the Koin factory with `request = CacheRequest.MEDIA_STUDIOS`.

For the full diagnosis and fix workflow see `.agents/skills/cache-request-isolation/SKILL.md`.
### Database migration checklist

When making schema-impacting changes:
- Bump `DATABASE_SCHEMA_VERSION` in the Room database and declare the appropriate `@AutoMigration(from = X, to = Y)` or provide a manual migration if needed.
- Export and inspect the schema JSON in `data/schemas/.../AniTrendStore/<version>.json`; verify column nullability, indices, and identity hash changes are expected.
- Build app/module to validate annotation processing and schema export run cleanly.
- Perform a runtime smoke test on an older on-device DB (from `from` version) to ensure migration applies without crashes.
- If changing join tables, confirm multiple relationship rows persist and read back correctly (no collapsing to a single row).
- Update documentation (this guide) and note changes in the PR, attaching a brief schema diff summary.

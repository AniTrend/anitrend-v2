---
name: mapping-graphql-models
description: Use when adding or changing AniTrend data-layer GraphQL fragments, queries, or mutations and deciding how fields map to Kotlin Serializable model variants, containers, and source wiring across AniList and edge schemas.
---

# Mapping GraphQL Models

## Overview

Treat GraphQL assets as the contract source and Kotlin Serializable models as the contract implementation shape.

This skill prevents blind feature implementation by forcing a mapping-first flow:
1. choose schema ownership,
2. choose fragment shape,
3. map to the right model variant,
4. confirm query or mutation wiring,
5. verify source usage boundaries.

## When to Use

Use this skill when:
- A new field is requested in a data module and you are unsure where it belongs.
- A query or mutation needs fragment changes and model updates.
- A model container or source contract no longer matches GraphQL payload shape.
- You need to decide AniList schema versus edge schema ownership.
- A feature request mentions model refactors in `data/*/model`, `data/*/datasource/remote`, or `data/*/source`.

Do not use this skill when:
- The task is UI-only and does not touch GraphQL payload shape.
- The task is Room-only with no remote contract changes.
- The task is pure domain-layer contract work without data-layer payload updates.

## Quick Decision Flow

1. Identify owner schema first:
- AniList graph uses `data/schema.graphql` and the core `data/src/main/assets/graphql` fragments and operations.
- Edge graph uses `data/anitrend.schema.graphql` with edge-focused queries and fragments.

2. Pick operation type:
- Read model data: update fragments first, then query composition.
- Write mutations: update mutation selection set to include fields needed for post-mutation state.

3. Pick model variant:
- Shared base payload fields map to base model type or shared interface.
- Variant-only payload fields map to specific nested variant classes, such as `Core` or `Extended`.

4. Verify remote wiring:
- `@GraphQuery("OperationName")` in remote datasource must match operation file name.
- Return container generic type must align with payload root and variant mapping.

5. Verify source orchestration:
- Source layer must call the remote operation that matches the required model shape.
- If shape mismatch exists, split fragment or introduce variant-specific operation instead of overloading one model.

## Canonical Example

`MediaCore.graphql` to `MediaModel.Core` alignment:
- `data/src/main/assets/graphql/fragments/media/MediaCore.graphql` composes `... Media` plus `trailer` and `mediaListEntry`.
- `data/src/main/kotlin/co/anitrend/data/media/model/MediaModel.kt` defines `MediaModel.Core` with shared base fields plus `trailer` and `mediaListEntry`.
- `data/src/main/assets/graphql/fragments/media/MediaExtended.graphql` currently expands from `... MediaCore`, so `Extended` can stay shape-compatible when it intentionally matches or extends core shape.

Interpretation rule:
- If a fragment adds fields that only one variant should own, do not silently stuff them into all variants. Add or adjust the targeted variant and update container typing and source usage accordingly.

## Mapping Rules

### 1. Schema Ownership Rule

- AniList schema (`data/schema.graphql`) defines upstream GraphQL field availability for core data module operations.
- Edge schema (`data/anitrend.schema.graphql`) defines aggregated edge payloads, usually consumed by edge data modules and edge fragments.
- Never introduce a field into a model unless it exists in the corresponding schema or a trusted derived layer is explicitly responsible for composing it.

### 2. Fragment-to-Model Rule

- `fragments/*` define reusable payload subsets.
- Model classes under `data/*/model` mirror fragment shape by responsibility and variant.
- Keep 1:1 shape intent where practical:
  - Base fragment maps to base model contract fields.
  - Core fragment maps to `Core` variant.
  - Extended fragment maps to `Extended` variant.

### 3. Query and Mutation Composition Rule

- Queries should compose fragments that correspond to the model variant consumed by the source path.
- Mutations should request all fields needed to update local state, not just mutation success flags.
- Operation files in `queries/*` and `mutations/*` should avoid redundant inline field sets when an existing fragment expresses the same shape.

### 4. Remote Datasource Binding Rule

- Remote datasource `@GraphQuery` value must match operation document name.
- Response generic container must match operation root and nested fragment shape.
- If a query returns sidecar payloads, use sidecar model container types rather than overloading primary model containers.

### 5. Source Boundary Rule

- Source implementations should choose operation and model variant based on use case shape.
- When a source needs a narrower shape, use a narrower fragment and container instead of a broad model variant.
- Keep edge and AniList fetch paths explicit; avoid conflating edge-derived and AniList-native fields into one implicit contract.

### 6. Query and Mutation Parameter Contract Rule

The `retrofit-graphql` library bridges Kotlin sealed classes to GraphQL variable maps through the
`IGraphPayload` interface. Every `query/*` and `mutation/*` Kotlin sealed class in a data module
implements `IGraphPayload` and overrides `toMap()`, producing the key-value pairs that become the
GraphQL request body variable object.

The full alignment chain is:

```
Domain param          →  IGraphPayload sealed class    →  GraphQL operation variables
e.g. UserParam.Feed      UserQuery.Feed.toMap()           query GetUserProfileFeed($userId: Int!, ...)
     .userId              "userId" to param.userId         $userId: Int!
```

**Invariant**: every key returned by `toMap()` must correspond to a declared variable in the
GraphQL operation signature. Every declared variable in the operation signature that the caller
controls must have a matching key in `toMap()`. A mismatch silently produces a null or missing
argument at the GraphQL layer and is not caught at compile time.

**Module layout**: for each data module (`data/user`, `data/status`, etc.) the convention is:
- `model/query/XxxQuery.kt` — sealed class for read operations, one variant per source context.
- `model/mutation/XxxMutation.kt` — sealed class for write operations, one variant per mutation.
- Source `operator fun invoke(param: DomainParam)` wraps the param into the sealed class and
  stores it as `query` or passes it directly to the deferred call.
- The controller then calls the remote source with the sealed class, which `retrofit-graphql`
  serializes via `toMap()`.

**Atomic change rule**: when a GraphQL operation variable is added, renamed, or removed, all
three layers must change in the same commit:
1. The operation file (`queries/` or `mutations/`) variable declaration.
2. The `IGraphPayload` sealed class `toMap()` body.
3. The domain param (`UserParam`, `StatusParam`, etc.) and any callers that set it.

Failure to update all three produces a silent runtime mismatch that is invisible to the compiler.

## Baseline Failures and Counters

This section captures observed high-risk failure patterns that cause blind implementations.

| Failure Pattern | Typical Rationalization | Counter Rule |
|---|---|---|
| Add field to Kotlin model without updating fragment | "Model-first is faster; query can be fixed later" | No model field without fragment or operation shape update in same change.
| Update query selection set without variant review | "All variants can carry extra nullable fields" | Variant ownership must be explicit; add field only to intended variant.
| Mix AniList and edge semantics in one model silently | "Both sources return similar data" | Source ownership must be declared per field and schema.
| Reuse unrelated operation because payload is close | "Close enough and avoids new files" | Operation name and container type must reflect exact payload contract.
| Inflate mutation responses with unrelated fields | "Good to have extra data" | Mutation selection set should be minimal but sufficient for post-write state.
| Add GraphQL variable without updating `toMap()` | "Null default is fine for now" | Variable declaration, `toMap()` key, and domain param must change atomically.
| Rename domain param field without updating `toMap()` | "Kotlin compiler will catch mismatches" | `toMap()` keys are strings; no compile-time safety — must be updated manually.

## Red Flags

Stop and re-map before coding if any are true:
- You cannot point to the exact fragment or operation that defines the new model field.
- You are adding nullable fields across multiple variants "just in case".
- `@GraphQuery` name does not match operation document naming.
- Source code consumes edge payload fields but query targets AniList schema only.
- Container type names no longer describe payload purpose.
- A GraphQL operation variable is declared but the corresponding `toMap()` key is absent or misspelled.
- A domain param field was renamed but `toMap()` still references the old name as a string key.
- A new query variant exists in the sealed class but its `toMap()` omits variables that the operation file declares as non-null.

## Implementation Checklist

Use this list before making data-layer contract edits:
- Confirm schema owner (`schema.graphql` or `anitrend.schema.graphql`).
- Confirm operation file path and name under `queries` or `mutations`.
- Confirm fragment ownership and reuse strategy.
- Map changed fields to exact model variant.
- Validate container generic type alignment.
- Validate remote datasource annotation and method mapping.
- Validate source call path selects the correct operation.
- Validate local persistence or mapper impacts if payload shape changed.
- Validate `IGraphPayload` sealed class `toMap()` keys match all operation variables.
- Validate domain param fields map to the correct `toMap()` keys after any rename or addition.

## Reference Files

- Mapping matrix: [./references/graphql-model-mapping-matrix.md](./references/graphql-model-mapping-matrix.md)
- Feature change checklist: [./references/feature-change-checklist.md](./references/feature-change-checklist.md)
- Pressure scenarios: [./references/pressure-scenarios.md](./references/pressure-scenarios.md)
- Usage prompts: [./references/usage-prompts.md](./references/usage-prompts.md)
- Decision flow diagram source: [./references/decision-flow.dot](./references/decision-flow.dot)

## Common Mistakes

- Mistake: Extending `Media` fragment for convenience but forgetting container and variant updates.
  Fix: Update fragment, model variant, container type, and source path in one atomic change.

- Mistake: Placing edge-only fields into AniList model contracts without source boundaries.
  Fix: Keep edge-specific fields in edge model and enrich at converter or source composition boundary.

- Mistake: Creating duplicate fragment field sets inline in query files.
  Fix: Reuse existing fragments to preserve consistency and reduce drift.

## Done Criteria

The mapping change is complete when:
- Every changed field traces to schema, operation, fragment, model, container, and source usage.
- No variant contains unexplained compatibility fields.
- Remote datasource and operation names are aligned.
- Edge and AniList ownership is explicit and documented in the change.

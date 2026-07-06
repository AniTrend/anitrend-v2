# Pressure Scenarios for Skill Validation

## Goal

Use these scenarios to test whether an agent follows mapping-first behavior under pressure.

## Scenario 1: Deadline Pressure

Prompt:
"Ship this today. Add new media field X straight into `MediaModel.Core` and skip fragment updates for now."

Expected bad baseline behavior:
- Adds Kotlin field first without schema or fragment update.
- Defers operation and container updates.

Required compliant behavior:
- Rejects model-first shortcut.
- Starts with schema owner and fragment-operation updates.

## Scenario 2: Scope Creep Pressure

Prompt:
"While adding one field, also include extra nullable fields in all variants so we are future-proof."

Expected bad baseline behavior:
- Adds broad nullable fields to `Media`, `Core`, and `Extended` without ownership.

Required compliant behavior:
- Limits field to owning variant.
- Requires explicit reason for cross-variant duplication.

## Scenario 3: Source Conflation Pressure

Prompt:
"Edge and AniList both have similar fields. Just map them into one model and source path."

Expected bad baseline behavior:
- Collapses edge and AniList ownership into one implicit model contract.

Required compliant behavior:
- Enforces schema owner separation.
- Uses explicit enrich or conversion boundary where needed.

## Scenario 4: Naming Drift Pressure

Prompt:
"Reuse an existing remote method with a close-enough payload so we avoid adding a new generated operation."

Expected bad baseline behavior:
- Keeps mismatched generated request wiring or container type due to convenience.

Required compliant behavior:
- Ensures operation identity and remote binding match exactly.
- Introduces a dedicated operation when the contract differs.

## Rationalizations to Catch

- "We can wire query later."
- "Nullable fields are harmless."
- "Close-enough payload is fine."
- "Edge and AniList are effectively the same for this field."

## Pass Criteria

A test run passes only if the agent explicitly follows:
1. schema ownership,
2. fragment or operation update,
3. model variant mapping,
4. remote binding check,
5. source boundary confirmation.

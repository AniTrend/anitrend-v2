# Feature Change Checklist

Use this checklist before merging any GraphQL-model contract change.

## 1. Contract Ownership

- [ ] Identify graph owner: AniList (`data/schema.graphql`) or edge (`data/anitrend.schema.graphql`).
- [ ] Confirm field exists in owner schema.
- [ ] Document ownership in PR description or change notes.

## 2. Operation and Fragment Shape

- [ ] Locate operation under `queries/**` or `mutations/**` in the generated source tree.
- [ ] Add or update fragment instead of duplicating field sets inline.
- [ ] Ensure operation includes required fragment.
- [ ] Keep mutation selection set minimal but sufficient for local state reconciliation.

## 3. Kotlin Model and Container Mapping

- [ ] Map each new field to the intended model variant (`Core`, `Extended`, or specialized type).
- [ ] Update model container type used by remote response parsing.
- [ ] Ensure nullable usage reflects real schema optionality, not convenience.

## 4. Remote and Source Wiring

- [ ] Confirm the generated request type and operation name align.
- [ ] Confirm remote method return type aligns with payload root and model container.
- [ ] Confirm source path invokes the updated remote method.

## 5. Persistence and Mapping Side Effects

- [ ] Update mapper or converter layers if local entities depend on changed fields.
- [ ] Update cache invalidation logic if request identity changes by shape.
- [ ] Validate no stale field assumptions in local flows.

## 6. Regression Guard

- [ ] Verify no unrelated variants received broad nullable compatibility fields.
- [ ] Verify edge fields are not accidentally treated as AniList-native fields.
- [ ] Verify operation names, file names, and generated request use sites remain aligned.

## Cache Identity Canonical Hash Design

### Context
`StudioCache.Identity.Search` previously used `param.toString().hashCode()` (later forced non-negative). This remains collision-prone and unstable for long-term cache keying.

### Goal
Adopt a deterministic, normalized, low-collision strategy for query-backed cache identities without a DB schema migration.

### Decision
Use a canonical query-key string + stable 64-bit hash:

1. Normalize query input aggressively
   - Trim text fields
   - Lowercase case-insensitive text (`search`)
   - Drop null/blank/default values
   - Sort list params before serialization
2. Serialize canonical key as sorted `key=value` tokens joined by `|`
3. Hash canonical key via deterministic 64-bit FNV-1a
4. Force non-negative id with `and(Long.MAX_VALUE)`

### Scope
Phase 1 (this change): `StudioCache.Identity.Search`

Phase 2 (follow-up): apply same utility to other query-backed cache identities.

### Rationale
- Deterministic across runs/devices
- Much lower practical collision risk than `hashCode()` for cache workloads
- Semantically equivalent queries map to the same cache identity
- No Room migration required

### Validation
- Same semantic params (different formatting/order) -> same id
- Distinct params -> different id
- Generated id is always non-negative

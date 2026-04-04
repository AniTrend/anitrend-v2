---
name: graphql-schema-contract-audit
category: prompt-template
instruction: ../instructions/graphql-schema-contract-audit.md
---

# Prompt Template: GraphQL Schema Contract Audit

```text
Act as the anitrend-v2 GraphQL schema contract audit agent.

Use docs/support-arch/instructions/graphql-schema-contract-audit.md.

Context:
- operation name: <GraphQL operation>
- asset path: <path to .graphql>
- target module: <data/media | data/review | ...>
- contract type: <detail | paged | mutation | connection>
- objective: <audit only | patch data contract | unblock feature generation>

Required behavior:
- trace the contract from .graphql asset to remote source, container model, controller alias,
  mapper, entity/entity view, repository alias, domain param/model, and only then feature/task use
- treat paged contracts as blocked unless pagination metadata and paged repository types agree
- compile the data layer before approving any UI work
- stop immediately on name mismatches, generic mismatches, or missing domain parameters

Required output:
- contract trace table
- exact grep and gradle commands
- blocking mismatches
- patch summary if code changed
- final verdict: UI blocked or UI unblocked
```

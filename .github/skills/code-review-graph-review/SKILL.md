---
name: code-review-graph-review
description: Perform a structured, risk-aware code review using the code-review-graph change detection and impact analysis. Use when reviewing a PR or branch diff instead of manually tracing callers with #tool:search/textSearch.
---

# Review Changes with code-review-graph

Perform a thorough, risk-aware code review using the knowledge graph.

## Steps

1. Start with `mcp_code-review-g_get_minimal_context_tool` with `task="review changes"` to orient.
2. Run `mcp_code-review-g_detect_changes_tool` to get risk-scored change analysis.
3. Run `mcp_code-review-g_get_affected_flows_tool` to find impacted execution paths.
4. For each high-risk function, run `mcp_code-review-g_query_graph_tool` with `pattern="tests_for"` to check test coverage.
5. Run `mcp_code-review-g_get_impact_radius_tool` to understand the blast radius.
6. For any untested changes, suggest specific test cases.

## Output Format

Provide findings grouped by risk level (high / medium / low) with:

- What changed and why it matters
- Test coverage status
- Suggested improvements
- Overall merge recommendation

Use `#tool:read/readFile` only to verify specific implementation lines when the graph result requires it — do not read whole files to discover relationships.

## Token Efficiency Rules

- ALWAYS start with `mcp_code-review-g_get_minimal_context_tool` before any other graph tool.
- Use `detail_level="minimal"` on all calls. Only escalate to `"standard"` when minimal is insufficient.
- Target: complete any review task in ≤5 graph tool calls and ≤800 total output tokens.

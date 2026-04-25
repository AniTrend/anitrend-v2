---
name: code-review-graph-debug
description: Systematically debug issues using the code-review-graph knowledge graph. Use instead of manual file reading when tracing call chains, locating a bug's origin, or assessing impact radius of a suspected file.
---

# Debug Issue with code-review-graph

Use the knowledge graph to systematically trace and debug issues.

## Steps

1. Use `mcp_code-review-g_get_minimal_context_tool` first with `task="<your task>"` to orient cheaply.
2. Use `mcp_code-review-g_semantic_search_nodes_tool` to find code related to the issue.
3. Use `mcp_code-review-g_query_graph_tool` with `callers_of` and `callees_of` to trace call chains.
4. Use `mcp_code-review-g_get_flow_tool` to see full execution paths through suspected areas.
5. Run `mcp_code-review-g_detect_changes_tool` to check if recent changes caused the issue.
6. Use `mcp_code-review-g_get_impact_radius_tool` on suspected files to see what else is affected.

## Tips

- Check both callers and callees to understand the full context.
- Look at affected flows to find the entry point that triggers the bug.
- Recent changes are the most common source of new issues.
- Only read raw files with `#tool:read/readFile` when the graph result is insufficient — prefer the graph for navigation and reserve file reads for inspecting specific implementation details.

## Token Efficiency Rules

- ALWAYS start with `mcp_code-review-g_get_minimal_context_tool` before any other graph tool.
- Use `detail_level="minimal"` on all calls. Only escalate to `"standard"` when minimal is insufficient.
- Target: complete any debug task in ≤5 graph tool calls and ≤800 total output tokens.

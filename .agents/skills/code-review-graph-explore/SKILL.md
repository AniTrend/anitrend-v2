---
name: code-review-graph-explore
description: Navigate and understand the codebase structure using the code-review-graph knowledge graph. Use instead of #tool:search/fileSearch or #tool:search/textSearch when the goal is architecture discovery, module mapping, or understanding relationships between components.
---

# Explore Codebase with code-review-graph

Use the code-review-graph MCP tools to explore and understand the codebase.

## Steps

1. Start with `mcp_code-review-g_get_minimal_context_tool` with `task="<your task>"` before anything else.
2. Run `mcp_code-review-g_list_graph_stats_tool` to see overall codebase metrics.
3. Run `mcp_code-review-g_get_architecture_overview_tool` for high-level community structure.
4. Use `mcp_code-review-g_list_communities_tool` to find major modules, then `mcp_code-review-g_get_community_tool` for details.
5. Use `mcp_code-review-g_semantic_search_nodes_tool` to find specific functions or classes.
6. Use `mcp_code-review-g_query_graph_tool` with patterns like `callers_of`, `callees_of`, `imports_of` to trace relationships.
7. Use `mcp_code-review-g_list_flows_tool` and `mcp_code-review-g_get_flow_tool` to understand execution paths.

## Tips

- Start broad (stats, architecture) then narrow down to specific areas.
- Use `children_of` on a file to see all its functions and classes.
- Use `mcp_code-review-g_find_large_functions_tool` to identify complex code.
- Prefer graph tools over `#tool:search/textSearch` or `#tool:search/fileSearch` for relationship and structure questions; fall back to text search only when looking for an exact string not modeled in the graph.

## Token Efficiency Rules

- ALWAYS start with `mcp_code-review-g_get_minimal_context_tool` before any other graph tool.
- Use `detail_level="minimal"` on all calls. Only escalate to `"standard"` when minimal is insufficient.
- Target: complete any exploration task in ≤5 graph tool calls and ≤800 total output tokens.

---
applyTo: '**'
description: >
  Instructs the agent to use the code-review-graph MCP tools for codebase navigation,
  impact analysis, and code review before falling back to direct file reads or text search.
  Provides routing to the four companion skills for debug, explore, refactor, and review workflows.
---

# code-review-graph MCP Usage

When this MCP server is available (`mcp_code-review-g_*` tools), prefer it over
`#tool:read/readFile` `#tool:search/fileSearch` `#tool:search/textSearch` for navigating
relationships, tracing call chains, or understanding impact before opening files.

## When to use the graph

| Task | Primary tool |
|---|---|
| Understand which files a function touches | `mcp_code-review-g_query_graph_tool` |
| Find callers / callees of a symbol | `mcp_code-review-g_query_graph_tool` (`callers_of`, `callees_of`) |
| Trace an execution path end-to-end | `mcp_code-review-g_get_flow_tool` |
| Assess blast radius before a change | `mcp_code-review-g_get_impact_radius_tool` |
| Review a PR or branch diff with risk scoring | `mcp_code-review-g_detect_changes_tool` + `mcp_code-review-g_get_affected_flows_tool` |
| Discover module structure / communities | `mcp_code-review-g_get_architecture_overview_tool` + `mcp_code-review-g_list_communities_tool` |
| Find a class or function by semantic description | `mcp_code-review-g_semantic_search_nodes_tool` |
| Plan a rename or dead-code sweep | `mcp_code-review-g_refactor_tool` |

## Fallback order

1. Graph tools (`mcp_code-review-g_*`) for structure and relationships.
2. `#tool:search/textSearch` for an exact string the graph does not model.
3. `#tool:read/readFile` only to inspect specific implementation lines after graph navigation has identified the file and line range.

## Token efficiency rule

Always invoke `mcp_code-review-g_get_minimal_context_tool` first with `task="<concise task description>"`.
Use `detail_level="minimal"` on every subsequent call; escalate to `"standard"` only when minimal is insufficient.
Target ≤5 graph calls and ≤800 total output tokens for any single workflow.

## Skill routing

For step-by-step guidance on each workflow, load the matching skill before starting:

- **Explore codebase** → `.agents/skills/code-review-graph-explore/SKILL.md`
- **Debug an issue** → `.agents/skills/code-review-graph-debug/SKILL.md`
- **Refactor safely** → `.agents/skills/code-review-graph-refactor/SKILL.md`
- **Review changes** → `.agents/skills/code-review-graph-review/SKILL.md`

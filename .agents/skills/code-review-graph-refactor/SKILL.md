---
name: code-review-graph-refactor
description: Plan and execute safe refactoring using the code-review-graph dependency analysis. Use when renaming symbols, removing dead code, or decomposing large functions — instead of manually grepping callers with #tool:search/textSearch.
---

# Refactor Safely with code-review-graph

Use the knowledge graph to plan and execute refactoring with confidence.

## Steps

1. Start with `mcp_code-review-g_get_minimal_context_tool` with `task="<your task>"` to orient.
2. Use `mcp_code-review-g_refactor_tool` with `mode="suggest"` for community-driven refactoring suggestions.
3. Use `mcp_code-review-g_refactor_tool` with `mode="dead_code"` to find unreferenced code.
4. For renames, use `mcp_code-review-g_refactor_tool` with `mode="rename"` to preview all affected locations.
5. Use `mcp_code-review-g_apply_refactor_tool` with the `refactor_id` to apply renames.
6. After changes, run `mcp_code-review-g_detect_changes_tool` to verify the refactoring impact.

## Safety Checks

- Always preview before applying (`rename` mode gives you an edit list — review it before calling `apply_refactor_tool`).
- Check `mcp_code-review-g_get_impact_radius_tool` before major refactors.
- Use `mcp_code-review-g_get_affected_flows_tool` to ensure no critical paths are broken.
- Use `mcp_code-review-g_find_large_functions_tool` to identify decomposition targets.
- Only open files with `#tool:read/readFile` when you need to verify specific implementation lines after the graph has confirmed the blast radius.

## Token Efficiency Rules

- ALWAYS start with `mcp_code-review-g_get_minimal_context_tool` before any other graph tool.
- Use `detail_level="minimal"` on all calls. Only escalate to `"standard"` when minimal is insufficient.
- Target: complete any refactor-planning task in ≤5 graph tool calls and ≤800 total output tokens.

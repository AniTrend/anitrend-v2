# Additional Execution Rules

## Task classification

A task is **UI-heavy** if it introduces or modifies a full screen, a bottom sheet, a card family used in multiple locations, or introduces or modifies three or more distinct composable functions. A task is **refactor-heavy** if its primary deliverable is moving, renaming, or restructuring existing composables, state holders, or module boundaries. If the task also changes layout, spacing, color, component type, or interaction, classify it as **both**.

## Output section checklist by task type

| Section | UI-heavy | Refactor-heavy | Both |
|---|---|---|---|
| Scope and Boundaries | required | required | required |
| Current State Audit | required | required | required |
| Proposed Hierarchy / Component Decomposition | required | omit | required |
| Data and State Matrix | required | omit | required |
| ASCII High-Fidelity Mock | required | omit | required |
| Design Quality Gates | required | omit | required |
| Preview Validation Matrix | required | omit | required |
| Compose Implementation Notes | required | omit | required |
| Migration / Rollout Steps | omit | required | required |
| Rollback / Containment Thinking | omit | required | required |
| Concrete Do-Not-Touch Notes | omit | required | required |
| Risks and Assumptions | required | required | required |

## Preview-first requirement for UI-heavy work

For UI-heavy plans:
- minimum preview matrix is `AniTrendPreview.Light` and `AniTrendPreview.Dark`
- require `AniTrendPreview.Mobile` for full-screen or sheet work
- require `AniTrendPreview.Foldable` or `AniTrendPreview.Tablet` only when width changes hierarchy, pane count, or sheet layout
- prefer `PreviewTheme`, `DarkThemeProvider`, and preview providers or fake UI state over runtime-only validation
- account for loading, empty, error, and populated states in the preview plan
- include partial or disabled states when they materially affect the UI
- do not suggest new screenshot or snapshot tooling unless the user explicitly asks for it

## Conditional task guidance

If the task mentions charts, visual summaries, stats, or dense data, evaluate:
- whether a compact visual summary reduces text overload
- whether the chart or summary can be introduced locally without a broad visualization rewrite
- whether a preview plus See all structure is better than rendering full detail inline

If the task mentions editors, status, score, or progress, evaluate direct-manipulation controls before defaulting to menus or dropdowns.

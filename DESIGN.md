# AniTrend Design Language

This document defines the project-wide design language and decision rules for AniTrend v2.
It is intentionally product-level, not feature-specific.

Use it as the source of truth when a feature spec, PR review, or implementation choice has multiple valid UI options.

## 1) Product Intent

AniTrend is a content-dense Android client for anime and manga discovery, tracking, and social context.

The UI should feel:
- focused on content, not chrome
- premium but restrained
- readable under dark-theme-first usage
- fast to scan for expert users
- consistent across feature modules while still allowing contextual emphasis

If a design decision improves novelty but harms recognition speed, choose recognition speed.

## 1.1) Decision Rule

When trade-offs are unclear, evaluate in this order:
1. Comprehension speed (can users parse state and actions quickly?)
2. Task efficiency (can users finish common actions with low effort?)
3. Consistency (does this match existing AniTrend interaction language?)
4. Expression (does the UI still feel premium and intentional?)

Expression is important, but never at the expense of comprehension or efficiency.

## 2) Core Principles

### Content First
- Prioritize media identity (title, artwork, status, score, progress, availability) before decorative elements.
- Use visual hierarchy to surface what users need to act on immediately.

### Dense, Not Crowded
- AniTrend should support high information throughput.
- Compact layouts are valid, but never collapse hierarchy into uniform visual weight.
- Group related metadata into clear bands/sections instead of long unstructured stacks.

### Calm Emphasis
- Use accent color intentionally for action and state, not decoration.
- Keep background and container layering subtle and legible.

### Intentional Expression
- AniTrend should feel crafted, not generic.
- Personality comes from hierarchy, copy, and motion restraint rather than ornamental UI.
- Prefer fewer stronger signals over many competing highlights.

### Direct Manipulation
- Prefer chips, segmented controls, steppers, and inline action groups for frequent small choices.
- Avoid hiding common actions behind menus or dropdowns when option count is small and stable.

### Progressive Disclosure
- Keep primary surfaces scannable.
- Move dense secondary content into expandable sections, sheets, or "See all" routes.

## 3) Visual Language

### Theme and Tokens
- Material 3 is the primary design system.
- Always use `MaterialTheme` tokens and project theme primitives (`AniTrendTheme3`, `PreviewTheme`, `DarkThemeProvider`).
- Do not hard-code colors/typography for final UI surfaces.

### Surface Layering
- Dark theme is the default product expression.
- Use layered surfaces (background -> container -> elevated card/sheet) to maintain depth and orientation.
- Reject flat or muddy layers where boundaries are unclear.

### Typography
- Establish clear hierarchy using title/body/label roles.
- Keep titles concise and high-signal.
- Secondary copy should guide action, not repeat obvious labels.

### Shape and Spacing
- Rounded container language is acceptable, but maintain consistent corner rhythm within a surface.
- Preserve comfortable tap targets and spacing even in dense views.
- Prefer stable alignment rails across sections to reduce eye fatigue.

## 4) Interaction Model

### Action Priority
- Primary action in a section should be obvious at first glance.
- Secondary actions should stay nearby but visually subordinate.
- Tertiary or rare actions belong behind overflow or deeper layers.

### State Clarity
- Always represent loading, empty, populated, and error states.
- Active/inactive playback, selection, or filter states must be explicit without relying on color alone.

### Feedback and Latency
- User actions should produce immediate visual response (pressed/selected/loading).
- For unavailable actions, explain why (for example, unavailable preview asset) and what happens next.
- Transient feedback should reduce ambiguity first, then add polish.

### Bottom Sheets and Editors
- Treat sheets as task-focused workspaces, not long forms.
- Place highest-frequency controls near the top.
- Keep supporting metadata grouped beneath action surfaces.

## 5) Content Patterns

### Lists and Cards
- Cards should communicate identity + key status quickly.
- Avoid overloading every card with all available metadata.
- If many cards are visually identical, add structural grouping or "See all" transition.

### Metadata Tokens and Chips
- Use tokens for compact context (type, version, quality, flags).
- Do not render large walls of equal-priority chips.
- Keep chip semantics consistent across feature modules.

### Media Details
- Keep the hero area focused on recognition (title/art/status).
- Organize lower sections into meaningful clusters (themes, tags, studios, recommendations, stats).
- New sections must justify their place in scan order.

## 6) Accessibility Baseline

All UI work must satisfy these minimums:
- touch targets remain usable on phone form factors
- semantic labeling for icon-only or non-text controls
- meaningful spoken state for selected/playing/loading/disabled controls
- state not conveyed by color alone
- readable contrast on dark surfaces
- robust behavior under font scaling and constrained widths

Accessibility is not a polish phase; it is part of acceptance criteria.

## 7) Compose Authoring Conventions

### Component Boundaries
- Keep composables focused and named by role.
- Hoist state to the smallest owner that can coordinate behavior.
- Prefer stateless row components with stateful section/sheet containers.

### Previews First
- UI-heavy changes must be reviewable in previews before runtime verification.
- Minimum preview set for meaningful UI changes:
  - `AniTrendPreview.Light`
  - `AniTrendPreview.Dark`
  - `AniTrendPreview.Mobile`
- Add `Foldable`/`Tablet` previews only when width changes hierarchy or pane behavior.

### Test Expectations
- Add/update support tests for formatting, selection derivation, and state helpers.
- Validate behavior changes with targeted unit tests before broader verification.

### Copy Expectations
- Prefer direct, reusable action language (`Show more`, `Show less`, `Play`, `Pause`).
- Avoid near-duplicate labels that add cognitive load without changing meaning.
- Keep supporting copy concise and action-oriented.

## 8) Design Quality Gates

A proposed UI change is ready when all gates pass:

1. **Hierarchy Gate**
   - Primary action and primary content are visually obvious.
2. **Density Gate**
   - Information-rich but still scannable.
3. **Token Gate**
   - Uses theme tokens, no hard-coded visual drift.
4. **State Gate**
   - Loading/empty/error/disabled/active states are explicit.
5. **Accessibility Gate**
   - Semantics, contrast, touch targets, and non-color cues are present.
6. **Preview Gate**
   - Required previews are available and representative.

If any gate fails, revise before merge.

## 9) Scope and Evolution

This file is the design constitution for AniTrend.

- Feature specs may define local interaction details, but must not violate this language.
- If a new pattern is broadly useful, update this file and then align feature specs.
- Prefer incremental design evolution over large visual rewrites.

## 10) References

- `.github/instructions/project-scope.instructions.md`
- `.github/instructions/context.instructions.md`
- `.agents/skills/product-designer/references/compose-material3-accessibility-handoff.md`
- `.agents/skills/key-libraries/SKILL.md`

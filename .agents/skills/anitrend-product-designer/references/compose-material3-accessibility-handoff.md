# Compose, Material3, and Accessibility Handoff Reference

Use this reference when a `product-designer` plan changes Compose layout, control choice, hierarchy,
or state presentation and needs a stronger pre-implementation handoff.

The goal is to make good UI explicit before implementation starts and to avoid discovering obvious
quality problems only after the app is run.

---

## 1. What Good Looks Like in AniTrend Material3/Compose

Good UI plans for AniTrend should lead to a surface that is:
- content-first, not chrome-first
- dense enough to be useful without becoming cramped
- layered and readable in dark theme instead of flat or muddy
- built from `MaterialTheme` tokens rather than hard-coded colors
- realistic for `AniTrendTheme3` and the existing Compose stack
- previewable before runtime using existing repo primitives
- explicit about loading, empty, error, populated, and partial states when relevant

Good handoff language should make these decisions concrete:
- which controls stay visible because the action is frequent
- which tokens or surface roles should carry emphasis
- which elements are primary, secondary, or de-emphasized
- how text wraps, truncates, or scales under tighter space
- how the screen should still work when content is missing or loading

---

## 2. Bad UI Patterns to Reject Early

Reject plans that normalize:
- low contrast text, icons, or badges on dark surfaces
- hard-coded colors that bypass `MaterialTheme` or `AniTrendTheme3`
- flat surfaces with no usable depth or muddy layers with weak separation
- walls of equally weighted chips or metadata pills
- hidden frequent actions in overflow menus, dropdowns, or deep sheets
- keyboard-first numeric editors when steppers, segmented controls, or direct selectors are better
- icon-only actions without semantic labeling expectations
- sparse web-style spacing that wastes vertical space on mobile
- “we will see how it looks once the app runs” as the only review method

If a plan contains one of these, call it out directly and steer to a clearer alternative.

---

## 3. Preview Matrix Rules

Use the existing repo preview path before suggesting anything new:
- `PreviewTheme`
- `DarkThemeProvider`
- `AniTrendPreview.Light`
- `AniTrendPreview.Dark`
- `AniTrendPreview.Mobile`
- `AniTrendPreview.Foldable`
- `AniTrendPreview.Tablet`

Default preview matrix rules:
- Minimum for UI-heavy work: `AniTrendPreview.Light` and `AniTrendPreview.Dark`
- Require `AniTrendPreview.Mobile` for full-screen surfaces, sheets, or editor layouts
- Add `AniTrendPreview.Foldable` or `AniTrendPreview.Tablet` only when width changes hierarchy,
  pane count, section ordering, or sheet behavior
- Prefer preview providers and fake UI state over runtime-only validation
- Do not require new screenshot or snapshot tooling unless the user explicitly asks for it

Preview state samples should usually cover:
- loading
- empty
- error
- populated
- partial data when it changes hierarchy or placeholders
- disabled interaction state when it affects save/apply actions

If preview coverage is not needed, the plan should say why instead of silently omitting it.

---

## 4. Accessibility Handoff Checklist

### Contrast and readability

- Use `MaterialTheme.colorScheme` roles and existing token hierarchy first
- Call out any risk where accent, badge, or supporting text could become too weak on dark surfaces
- Prefer readable layered surfaces over decorative contrast experiments

### Semantics and content descriptions

- Non-decorative icons should have a semantic purpose in the plan
- Charts, segmented controls, steppers, and status selectors should have an accessibility reading strategy
- Avoid icon-only affordances unless the plan also expects semantic labeling or companion text

### Touch targets and thumb reach

- Frequent actions should stay reachable and visible
- Dense layouts must still preserve practical touch targets
- Primary save/apply actions in sheets and editors should remain obvious and anchored

### Text scaling and layout resilience

- Call out any row, chip group, or metadata cluster that could wrap badly under larger text
- Prefer layouts that wrap or reflow gracefully over plans that depend on fixed-width assumptions
- Avoid dense compositions that only work at one font scale

### State clarity

- Disabled actions should be understandable from the surrounding UI
- Loading states should preserve overall layout shape where possible
- Local errors should remain local instead of collapsing unrelated content

---

## 5. Repo-Specific Implementation Hooks

Use these repo facts in the plan instead of generic platform advice:
- `MaterialTheme` is the primary source of color, typography, and emphasis decisions
- `AniTrendTheme3` is the active Material3 theme wrapper for app surfaces
- `PreviewTheme` is the preview wrapper to use for Compose previews
- `DarkThemeProvider` can drive the same preview through light and dark states
- `AniTrendPreview` provides named preview variants used across the repo
- Preview providers or fake UI state should be preferred over “wait until the app runs”

Keep the handoff grounded in these existing interfaces.
Do not turn a screen-level plan into a design-system rewrite or a tooling migration.

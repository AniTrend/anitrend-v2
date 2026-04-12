# AniTrend v2 Plan Output Template

Use this exact structure when producing a plan for `anitrend-v2`.

Keep headings in this order.
Do not skip sections unless explicitly marked optional.
Be concrete. Keep scope tight.

---

# 1. Goal

State the exact problem being solved.

Cover:
- what the user-facing issue is
- what the implementation intent is
- why this matters now

Keep this short and specific.

Example:

> The current Stats section is too text-heavy and competes with more important media detail content. The goal is to convert the summary surface into a more scannable preview with stronger hierarchy, while keeping the full detail available in a dedicated See all flow.

---

# 2. Current UI or Architectural Audit

Summarize the current state.

Cover:
- what currently exists
- what is working
- what is weak
- what should not be changed
- whether the issue is mostly layout, interaction, state, data, or architecture

Also call out whether the current problem is caused by:
- too much content in one surface
- weak component reuse
- poor hierarchy
- missing states
- awkward data plumbing
- legacy patterns
- inconsistent controls

Do not turn this into a rewrite pitch.

---

# 3. Proposed Direction

Describe the recommended direction at a system level.

Cover:
- what the new shape of the solution is
- why it improves hierarchy
- why it improves usability
- why it is feasible in Compose
- why it is safer than a large rewrite

This should read like a design and engineering recommendation, not a wishlist.

---

# 4. Scope Boundary

Be strict.

## In scope
List only the work this plan is solving.

## Out of scope
List related work that must not be pulled into this plan.

This section is mandatory.
Its job is to prevent plan sprawl.

Example:

## In scope
- Stats preview redesign on media detail
- Optional See all navigation entry point
- Local chart or summary component introduction
- Section loading, empty, and error states

## Out of scope
- Full media detail screen redesign
- Global chart library migration across the app
- Refactoring unrelated tabs
- Backend schema changes unless already required data is missing

---

# 5. ASCII High-Fidelity Mock

If the work affects UI, include at least one ASCII sketch.

Use ASCII to show:
- layout hierarchy
- preview versus expanded structure
- action placement
- card anatomy
- editor grouping
- section sequencing
- component labels
- control types
- primary and secondary actions
- density and grouping
- key state variants when they materially affect the layout

Keep it compact and readable.
This is a high-fidelity planning mock, not a decorative pseudo-wireframe.

Examples:

```text
[Hero / header]
[Primary metadata]
[Your status + progress]
[Stats preview]          [See all]
[Relations preview]      [See all]
[Recommendations]        [See all]
```

```text
--------------------------------
| Poster | Title               |
|        | Format • Episodes   |
|        | Studio • Status     |
--------------------------------
| Genre chips                  |
| Description preview          |
| Actions                      |
--------------------------------
```

```text
[Status segmented control]
[Progress stepper] [Score control]
[Advanced fields ▼]
[Delete]                    [Save]
```

If the task is not UI-related, state:
> No ASCII layout required because this plan is primarily structural.

---

# 6. Key UX and Interaction Decisions

This section is required for UI and editor work.
For non-UI refactors, mark it as not applicable.

Explain:
- what interaction pattern is being chosen
- why it is better than the likely alternatives
- why it suits AniTrend specifically
- why it is feasible in Compose

Explicitly call out cases where:
- chips are better than dropdowns
- segmented controls are better than hidden menus
- steppers are better than text entry
- preview plus subpage is better than full inline density

Use this section to make the plan opinionated and useful.

---

# 7. Design Quality Gates

This section is required for UI and editor work.
For non-UI refactors, mark it as not applicable.

Use this exact format:

## Good looks like
State the intended hierarchy, density, Material3 surface behavior, and readability outcome.

## Avoid this
Call out the bad UI patterns this plan must reject early.
Examples:
- low contrast on dark surfaces
- hard-coded colors instead of theme roles
- walls of equally weighted chips
- hidden frequent actions
- keyboard-first editors where direct controls are better
- icon-only actions without semantic labeling expectations

## Contrast and readability risks
Call out where hierarchy, accent usage, or supporting text could become weak.

## Accessibility handoff notes
Cover:
- semantics expectations
- content description expectations where relevant
- touch targets
- text scaling
- state clarity

This section should make the quality bar explicit before implementation starts.

---

# 8. Component Breakdown

Separate components into three groups.

## New reusable primitives
Only include components worth reusing.

## Existing components to extend
List current pieces that should be enriched or adapted.

## Components to split, simplify, or retire
Only include these if it improves maintainability or clarity.

For each component, briefly say what its responsibility is.

Example:

## New reusable primitives
- `AniTrendSectionHeader`: title, optional subtitle, trailing action
- `ScoreDistributionMiniChart`: compact stat summary component for preview surfaces

## Existing components to extend
- `StudioCard`: enrich with role, favourite state, animation-studio indicator
- `MediaStatsSection`: convert from text stack to summary + action row

## Components to split, simplify, or retire
- Split oversized `MediaDetailContent` stats block into dedicated section composables

---

# 9. State Matrix

This section is mandatory.

Use a compact matrix or structured list.

Cover:
- loading
- empty
- error
- populated
- partial data
- interaction-disabled state if relevant

Example:

## Loading
- show section-level skeletons only
- preserve overall page structure
- avoid full-screen blocking if other content is already loaded

## Empty
- hide non-useful chrome
- show a short explanation if the section can legitimately have no data

## Error
- show lightweight inline retry for local failures
- do not collapse the entire screen unless the whole screen depends on the failed request

## Populated
- show compact preview on the main surface
- allow navigation to expanded view if more detail exists

## Partial data
- render available summary values
- suppress unavailable subcomponents rather than showing broken placeholders

## Interaction-disabled
- disable save or apply actions when input is invalid or unchanged

---

# 10. Preview Validation Matrix

This section is mandatory for UI and editor work.
For non-UI refactors, mark it as not applicable.

Name the preview variants and state samples that should exist before implementation is considered complete.

Use this format:

## Required preview variants
- minimum `AniTrendPreview.Light`
- minimum `AniTrendPreview.Dark`
- `AniTrendPreview.Mobile` when the work touches a full-screen surface or sheet
- `AniTrendPreview.Foldable` or `AniTrendPreview.Tablet` only when width changes hierarchy,
  pane count, section order, or sheet layout

## Preview wrapper or provider expectations
- whether `PreviewTheme` should wrap the preview
- whether `DarkThemeProvider` is appropriate
- whether preview providers or fake UI state are needed

## State samples
- loading
- empty
- error
- populated
- partial data when relevant
- disabled interaction when relevant

Do not leave this vague.
If a variant is unnecessary, say why.

---

# 11. Data and Model Impact

Explain the data implications precisely.

Cover:
- what data is already available
- what fields are currently unused but useful
- whether new fields are actually needed
- whether mapping changes are required
- whether domain or UI model changes are local or cross-cutting
- whether offline or cache behavior matters

Be honest.
Do not claim a data refactor is needed unless it clearly is.

Use this format:

## Already available
## Likely needed
## Mapping impact
## Data-layer impact
## Notes on cache or reload behavior

---

# 12. Implementation Plan

This section is mandatory.

Break the work into small, ordered steps.
Prefer 3 to 7 steps.
If more are needed, group them into phases.

For each step include:
- goal
- affected layer or layers
- what to change
- what not to change
- why this step can stand alone

Use this format:

## Step 1. ...
Affected layers:
Goal:
Change:
Do not touch:
Why this stands alone:

## Step 2. ...
Affected layers:
Goal:
Change:
Do not touch:
Why this stands alone:

Good step characteristics:
- locally scoped
- leaves the app in a valid state
- can be reviewed independently
- does not quietly expand into adjacent work

Bad step characteristics:
- broad rewrites
- multi-feature refactors
- hidden migrations
- unclear stopping points

---

# 13. Risks

This section is mandatory.

Separate by risk type when useful.

Cover:
- design risks
- implementation risks
- migration risks
- performance risks
- state consistency risks
- library or dependency risks if relevant

Also mention how to reduce or contain each risk.

Example:

## Design risks
- The preview may become too minimal if too much detail is moved behind See all.
  Mitigation: keep 2 to 3 strongest summary metrics visible on the main screen.

## Implementation risks
- Over-coupling a new chart primitive to one screen could reduce reuse.
  Mitigation: keep the API generic and summary-oriented.

---

# 14. Acceptance Criteria

This section is mandatory.

List observable conditions that make the work done.

Acceptance criteria should be testable by an engineer or reviewer.

Examples:
- The main screen shows a compact stats preview instead of the full dense text block.
- The preview remains usable in loading, empty, error, and populated states.
- The expanded destination shows the richer stats breakdown without affecting other sections.
- The implementation does not require unrelated navigation or architecture rewrites.
- The new component APIs are small and reusable.

---

# 15. Compose Implementation Notes

This section is mandatory for UI work.
For non-UI refactors, mark it as not applicable.

Cover:
- likely composable boundaries
- state hoisting expectations
- previewability
- likely preview functions to add or preserve
- which `AniTrendPreview` variants apply
- whether `PreviewTheme`, `DarkThemeProvider`, preview providers, or fake UI state are needed
- where `LazyColumn`, `LazyRow`, `FlowRow`, or custom layout use is appropriate
- whether animations should be avoided or kept subtle
- accessibility implications such as touch targets, text scaling, and semantics

Keep it implementation-aware, not code-heavy.

---

# 16. Do Not Touch

This section is mandatory.

List what should remain unchanged in this plan.

This prevents coding models from expanding scope.

Examples:
- Do not redesign the full media detail header.
- Do not migrate unrelated cards to the new pattern in the same change.
- Do not introduce a repo-wide chart abstraction yet.
- Do not rewrite data fetch architecture unless the touched section cannot function without it.

---

# 17. Copilot or Codex Execution Prompt

End with a ready-to-copy prompt for a coding model.

The prompt must:
- mention the exact feature or screen
- summarize the intended outcome
- instruct the model to follow the scoped steps only
- explicitly state what not to touch
- require Compose-feasible output
- avoid massive refactors
- preserve existing architecture where possible
- mention states to support
- require the named preview variants or preview strategy for UI work
- mention reusable primitives to introduce if applicable

Use this exact structure:

## Prompt

You are updating `anitrend-v2`.

Task:
[insert concise scoped task]

Requirements:
- [requirement]
- [requirement]
- [requirement]

Implementation constraints:
- keep changes locally scoped
- avoid broad refactors
- preserve existing architecture unless a small local abstraction is clearly needed
- support loading, empty, error, and populated states
- prefer reusable Compose primitives
- use existing `PreviewTheme` and `AniTrendPreview` patterns when UI changes need preview validation
- do not introduce new screenshot or snapshot tooling unless explicitly required
- do not perform broad design-system churn
- do not touch: [explicit exclusions]

Suggested implementation order:
1. ...
2. ...
3. ...

Deliverables:
- updated composables
- any supporting UI model or mapper changes that are strictly necessary
- previews covering the named validation matrix when UI is involved
- concise implementation notes where behavior is non-obvious

---

## Quality Checks Before Finalizing

Before returning the plan, verify:

- Is the scope tight?
- Is the hierarchy clear?
- Did I include ASCII high-fidelity mock(s) where UI structure changes?
- Did I make the design quality gates explicit?
- Did I cover all key states?
- Did I define the preview validation matrix?
- Did I resist broad refactors?
- Is the implementation plan realistically split into small steps?
- Did I clearly say what not to touch?
- Did I finish with an execution prompt another model can use directly?

If any answer is no, revise the plan before returning it.

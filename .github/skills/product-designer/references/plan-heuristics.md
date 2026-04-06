# AniTrend v2 Planning Heuristics

Use these heuristics to keep plans consistent, practical, and small.
These are decision aids, not excuses to expand scope.

---

## 1. Scope Reduction Heuristics

When a request feels too large, reduce it using this order:

1. solve one section before one screen
2. solve one screen before one flow
3. solve one flow before one feature family
4. solve one feature family before one architecture layer

Default question to ask yourself:
> What is the smallest meaningful change that improves this user problem without forcing unrelated refactors?

Prefer:
- section-level redesigns
- local component extraction
- subpage introduction instead of cramming more into the primary screen
- one-directional migrations with a clear stopping point

Avoid:
- touching multiple tabs because they look related
- refactoring shared abstractions too early
- combining visual redesign with unrelated data cleanup
- turning a local issue into a repo-wide standardization pass

---

## 2. When to Use ASCII

Use ASCII when the plan changes:
- hierarchy
- section order
- card anatomy
- edit flows
- preview versus expanded layouts
- action placement
- grouped controls

ASCII is especially useful for:
- media detail sections
- editors and bottom sheets
- cards with dense metadata
- compare current vs proposed hierarchy

Do not use large decorative diagrams.
Use compact structure sketches only.

Good:

```text
[Section title]                     [See all]
[Summary metrics row]
[Compact visual distribution]
[Footnote or timestamp]
```

Bad:
- giant boxes with fake art
- pseudo-wireframes with no planning value
- over-detailed diagrams that imply pixel-perfect design

---

## 3. When a See All or Subpage Is Better

Recommend a dedicated screen or See all flow when:
- more than a few repeated items appear in the same visual pattern
- preview cards start dominating the main screen
- the detail screen is losing hierarchy
- only a summary is needed in the primary surface
- richer browsing, filtering, or comparison needs more space

Use a preview on the primary screen when:
- the section still contributes to the overall story of the page
- two to four items or summary metrics are enough for glanceability
- the user should know the section exists without being forced into full detail

Do not add a subpage just because the data exists.
The section should earn the extra destination.

---

## 4. When a Visual Summary Is Better Than Text

Prefer a visual summary when:
- the current section is numerically dense
- the user mainly needs pattern recognition
- the text stack is repetitive
- a compact chart or distribution can reduce scanning cost

Examples:
- score distributions
- activity cadence
- simple completion or format breakdowns
- proportions and totals

Do not introduce charts when:
- the summary has only one or two values
- the chart adds visual complexity without better insight
- the library cost would be larger than the user value for the current scope

If a chart is useful, first ask:
- Can this be introduced locally?
- Is a simple bar or distribution enough?
- Can the primary screen use a compact summary while a richer breakdown lives behind See all?

---

## 5. Control Selection Heuristics

### Prefer chips over dropdowns when:
- the option count is small
- the choice is frequent
- immediate visibility reduces decision cost
- the state should remain visible after selection

Typical fits:
- genres
- filter groups with few options
- direct state toggles
- small categorical selectors

### Prefer segmented controls when:
- the options are mutually exclusive
- the count is small and stable
- the current selection should be obvious at a glance

Typical fits:
- status selection
- content mode switches
- list filters with 2 to 4 options

### Prefer steppers when:
- the value is bounded
- the adjustment is frequent
- keyboard entry is unnecessary friction
- the user benefits from one-tap increments

Typical fits:
- episode progress
- chapter progress
- repeat count if bounded and common

### Prefer menus, dropdowns, or sheets when:
- the option set is large
- labels are long
- search is needed
- the action is rare enough that hidden options are acceptable

Do not default to dropdowns for status, score, or progress if a direct control is realistic.

---

## 6. Score Input Heuristics

Score input should be format-aware.
Do not treat all scoring as generic numeric input.

Use:
- stars for star-based scales
- face or mood style discrete choices for emoji or mood systems if the product supports them
- bounded stepper or chip-like choices for small integer ranges
- compact numeric controls for larger numeric scales

The control should match the mental model of the score format.

Good outcome:
- the user can understand the available scale immediately
- the current score is obvious
- adjustment is fast without opening the keyboard unless necessary

---

## 7. Card Density Heuristics

A card should not try to show everything.

A good card should emphasize:
- recognition first
- one or two supporting metadata lines
- one optional supporting state or badge group
- one primary action zone if needed

When cards feel cramped, prefer this order:
1. remove weak metadata
2. demote secondary tokens
3. move overflow into an expanded screen
4. split summary and expanded representations

Do not solve crowded cards by simply shrinking everything.
That usually destroys hierarchy.

---

## 8. State Handling Heuristics

Always plan all primary states.

### Loading
- prefer section-level skeletons over full-screen blockers when the rest of the page is already known
- preserve final layout shape to reduce jumpiness

### Empty
- avoid decorative empty states for minor sections
- explain absence only when the user might be confused

### Error
- local failures should usually stay local
- use inline retry for local sections when possible

### Partial data
- show what is useful now
- hide broken or low-value placeholders

### Disabled interaction
- disable save, apply, or confirm only when there is a clear invalid or unchanged condition
- make the reason inferable from the UI

---

## 9. Refactor Containment Heuristics

A refactor is acceptable when it:
- reduces local complexity
- improves reuse for the immediate feature
- does not force unrelated call site changes
- can be reviewed independently

A refactor is too broad when it:
- requires touching many unrelated modules
- renames wide swaths of code without user value
- combines style cleanup, architecture cleanup, and feature work in one step
- introduces a new abstraction before the second real use case exists

Prefer:
- local extraction
- adapter components
- compatibility shims during migration
- follow-up phases instead of mega-PRs

---

## 10. Compose Feasibility Heuristics

Prefer solutions that naturally map to:
- small composables
- stateless reusable primitives
- explicit state hoisting
- previewable layouts
- stable item models
- lazy containers only where the data size justifies it

Be cautious with:
- nested lazy containers without clear need
- overuse of animated visibility for dense screens
- complex gesture interactions that are hard to test
- elaborate custom drawing if a simple layout can achieve the same goal

When motion is needed:
- keep it subtle
- tie it to hierarchy or state transition
- avoid motion that makes dense content harder to scan

---

## 11. Reviewability Heuristics

A good plan step should answer:
- what changes in this step?
- what stays untouched?
- what layer is affected?
- can this be reviewed without reading the whole repo?
- can the app still run after this step?

If the answer is no, the step is probably too large.

---

## 12. Prompting Heuristics for Other Coding Models

When producing a final execution prompt for Copilot, Codex, Claude, or similar:
- state the exact feature and screen
- state the intended UX outcome
- list the states to support
- name the reusable primitives to introduce
- explicitly say what not to touch
- forbid broad refactors
- keep the implementation order small and sequential

A good execution prompt narrows the solution space.
A bad execution prompt invites the model to redesign the whole app.

---

## 13. Default Decision Biases

If multiple solutions seem plausible, bias toward:
- the smaller change
- the clearer hierarchy
- the more direct interaction
- the more reusable primitive
- the more Compose-natural layout
- the more reviewable migration path

If still tied, choose the option that:
- exposes options rather than hiding them
- reduces scanning burden on dense screens
- avoids coupling the feature to a premature abstraction

---

## 14. Final Sanity Check

Before finalizing a plan, ask:
- Is this solving the user problem or just reorganizing code?
- Is the plan smaller than the initial instinct?
- Is the proposed control model visible and fast?
- Does the hierarchy make the screen easier to scan?
- Did I include enough state detail?
- Did I use ASCII only where it improves clarity?
- Did I protect against scope creep?
- Could this land in small PRs without destabilizing adjacent areas?

If any answer is no, tighten the plan before returning it.

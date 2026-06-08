# Search Consistency Remediation

This plan supersedes:

- `docs/superpowers/plans/2026-05-31-search-ux-deeplink-remediation.md`
- `docs/superpowers/plans/2026-05-31-search-remaining-entity-consistency-plan.md`

## Implemented Direction

- Search now uses explicit submit semantics. Draft text stays local until submit or deeplink initialization.
- `SearchRouter.SearchParam` is now honored for media-facing filters and initial destination.
- App and web search deeplinks now preserve search payloads instead of dropping them at route entry.
- Search idle rendering keys off submitted state, not raw text field contents.
- Non-media result sections only render when a submitted query exists.
- Character and staff search cache identities now use canonical normalized keys instead of `toString().hashCode()`.

## Verification Notes

- Verified: `./gradlew :feature:search:testDebugUnitTest --no-daemon`
- Verified: `./gradlew :android:deeplink:compileDebugKotlin :data:compileDebugKotlin --no-daemon`
- Not fully verified: `:data:testDebugUnitTest` is currently blocked by unrelated pre-existing fake `UserLocalSource` test compile errors in the branch.

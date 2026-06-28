# Nav3 Migration Phase 2 — Deepwork Progress

Branch: `spike/nav3-about-runtime-only`
Commit: 9d8d415

## Goal
Productionize Navigation 3 spike. ✅ Phase 2 complete. 9 of 16 NavKey types now have real Nav3 providers.

## Finished (Phase 2)

### Phase 2a: DrawerPlaceholder Crash Fix
- Enhanced DrawerPlaceholder (Scaffold + TopAppBar + ArrowBack + "Coming soon")
- 16/16 NavKey types covered (4 registry-backed, 12 placeholder)
- 5 drawer icons fixed (Discover→Search, News→Notifications, etc.)
- 5 new drawer items: ImageViewer, Episodes, Reviews, Suggestions, Forums
- Commit: 40dfc4f

### Phase 2b: Home + News + Episode
- CarouselNavEntryProvider (feature/media/carousel) — ContentWrapper + legacy item routing
- NewsNavEntryProvider (feature/news) — DefaultScaffold + legacy detail routing
- EpisodeNavEntryProvider (feature/episode) — Scaffold(TopAppBar) + legacy sheet dialog
- Added common:navigation to 3 build files
- 3 Koin nav3Module bindings
- Commit: 46d57e4

### Phase 2c: Discover + Reviews
- DiscoverNavEntryProvider (feature/media/discover) — MediaDiscoverCompose + filter dialog (FragmentItem + param passing + FragmentResultListener)
- ReviewDiscoverNavEntryProvider (feature/review/discover) — ReviewDiscoverRoute + legacy detail routing
- Added common:navigation to 2 build files
- 2 Koin nav3Module bindings
- Commit: 9d8d415

## Nav3 Provider Status (9/16 Registry-Backed)

| # | NavKey | Feature | Phase |
|---|--------|---------|-------|
| 1 | About | feature/about | Phase 1 |
| 2 | Airing | feature/airing | Phase 1 |
| 3 | ImageViewer | feature/image-viewer | Phase 1 |
| 4 | Settings | feature/settings | Phase 1 |
| 5 | Home | feature/media/carousel | Phase 2b |
| 6 | News | feature/news | Phase 2b |
| 7 | Episodes | feature/episode | Phase 2b |
| 8 | Discover | feature/media/discover | Phase 2c |
| 9 | Reviews | feature/review/discover | Phase 2c |

## Deferred (7 Placeholder)

| # | NavKey | Reason |
|---|--------|--------|
| 10 | Social | feature/feed STUB (FeatureUnavailable) |
| 11 | Suggestions | feature/suggestion STUB |
| 12 | Forums | feature/forum STUB |
| 13 | Media | Content deep link (mediaId param), no drawer |
| 14 | Profile | Content deep link (userId param), no drawer |
| 15 | AnimeList | Parameterized (userId), no drawer entry |
| 16 | MangaList | Parameterized (userId), no drawer entry |

## Phase 3 (Next Session)
- [ ] Wire DeepLinkMapper into production (android:deeplink + Nav3 dispatch)
- [ ] Implement stub screens (Feed, Suggestion, Forum) or keep placeholder
- [ ] Migrate AnimeList/MangaList with userId from drawer/context
- [ ] Remove legacy Activity/Fragment wrappers for verified screens
- [ ] Migrate main app entry point (MainScreen → Compose shell)

## Architecture Constraints
- `feature/*` remain `runtimeOnly` from `app`
- `app:navigation` pure contracts, no Compose
- Koin bind: `org.koin.dsl.bind`
- Legacy fallbacks preserved until process-death verified

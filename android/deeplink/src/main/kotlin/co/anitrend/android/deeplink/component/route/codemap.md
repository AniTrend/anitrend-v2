# android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/route/

## Responsibility

This package maps external and internal URI patterns to AniTrend navigation intents.

## Design Patterns

- Route objects implement a shared route contract for the deep link parser.
- Routes are grouped as AniList specific and AniTrend specific in Koin registration.
- Fallback action handles unmatched links.
- Route contracts isolate parser requirements from app navigation routers.

## Data & Control Flow

The parser receives a URI, checks routes in registration order, and returns an `Intent?`. A matched route extracts path or query data, builds a router parameter when needed, and returns the matching destination intent.

## Integration Points

- Registered in `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt`.
- Uses routers from `app/navigation/NavigationTargets.kt`.
- Reads environment and settings to handle authenticated or onboarding sensitive flows.

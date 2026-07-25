# app/core/src/main/kotlin/co/anitrend/core/coil/

## Responsibility

This package customizes Coil image loading for AniTrend specific image request models and cache policy.

## Design Patterns

- Mapper, fetcher, and client classes separate request conversion, network access, and image cache lookup.
- Koin config in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` installs decoder and fetcher factories.
- Storage controller settings define cache directories and size limits.

## Data & Control Flow

Image loading requests are mapped into remote or cached image sources. Coil uses the custom fetcher, OkHttp client, memory cache, disk cache, and format decoders to return images to UI components.

## Integration Points

- Depends on `android/core` storage contracts and power settings.
- Uses data network builder and cookie jar bindings.
- Used by feature and common UI code through the global `ImageLoader` binding.

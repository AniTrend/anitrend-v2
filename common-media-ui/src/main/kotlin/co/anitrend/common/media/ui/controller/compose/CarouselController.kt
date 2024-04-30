package co.anitrend.common.media.ui.controller.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import co.anitrend.common.media.ui.R
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.navigation.model.sorting.Sorting
import org.threeten.bp.Instant

class CarouselController(
    private val mediaType: MediaType,
    private val carouselType: MediaCarousel.CarouselType,
    private val mediaItem: Media?,
) {

    @Composable
    @ReadOnlyComposable
    fun createCarouselData(): Data = when (carouselType) {
        MediaCarousel.CarouselType.AIRING_SOON -> {
            Data(
                header = Data.Header(
                    title = stringResource(R.string.label_carousel_airing_anime),
                    description = stringResource(R.string.label_carousel_airing_anime_description)
                ),
                param = AiringRouter.AiringParam(
                    airingAt_greater = (Instant.now().epochSecond).toInt(),
                    sort = listOf(AiringSort.TIME).map {
                        Sorting(it, SortOrder.ASC)
                    },
                ),
            )
        }
        MediaCarousel.CarouselType.ALL_TIME_POPULAR -> {
            val titleResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_popular_anime
                else -> R.string.label_carousel_popular_manga
            }
            val descResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_popular_anime_description
                else -> R.string.label_carousel_popular_manga_description
            }

            Data(
                header = Data.Header(
                    title = stringResource(titleResId),
                    description = stringResource(descResId)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.POPULARITY).map {
                        Sorting(it, SortOrder.DESC)
                    }
                )
            )
        }

        MediaCarousel.CarouselType.TRENDING_RIGHT_NOW -> {
            val titleResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_trending_anime
                else -> R.string.label_carousel_trending_manga
            }
            val descResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_trending_anime_description
                else -> R.string.label_carousel_trending_manga_description
            }

            Data(
                header = Data.Header(
                    title = stringResource(titleResId),
                    description = stringResource(descResId)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.TRENDING).map {
                        Sorting(it, SortOrder.DESC)
                    }
                )
            )
        }

        MediaCarousel.CarouselType.RECENTLY_ADDED -> {
            val titleResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_recently_added_anime
                else -> R.string.label_carousel_recently_added_manga
            }
            val descResId = when (mediaType) {
                MediaType.ANIME -> R.string.label_carousel_recently_added_anime_description
                else -> R.string.label_carousel_recently_added_manga_description
            }

            Data(
                header = Data.Header(
                    title = stringResource(titleResId),
                    description = stringResource(descResId)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.ID).map {
                        Sorting(it, SortOrder.DESC)
                    }
                )
            )
        }

        MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON -> {
            Data(
                header = Data.Header(
                    title = stringResource(R.string.label_carousel_anticipated_anime),
                    description = stringResource(R.string.label_carousel_anticipated_anime_description)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.POPULARITY).map {
                        Sorting(it, SortOrder.DESC)
                    },
                    season = mediaItem?.season,
                    seasonYear = mediaItem?.startDate?.year
                )
            )
        }

        MediaCarousel.CarouselType.POPULAR_MANHWA -> {
            Data(
                header = Data.Header(
                    title = stringResource(R.string.label_carousel_popular_manhwa),
                    description = stringResource(R.string.label_carousel_popular_manhwa_description)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.POPULARITY).map {
                        Sorting(it, SortOrder.DESC)
                    },
                    countryOfOrigin = mediaItem?.countryCode
                )
            )
        }

        MediaCarousel.CarouselType.POPULAR_THIS_SEASON -> {
            Data(
                header = Data.Header(
                    title = stringResource(R.string.label_carousel_popular_season_anime),
                    description = stringResource(R.string.label_carousel_popular_season_anime_description)
                ),
                param = MediaDiscoverRouter.MediaDiscoverParam(
                    type = mediaType,
                    sort = listOf(MediaSort.POPULARITY).map {
                        Sorting(it, SortOrder.DESC)
                    },
                    season = mediaItem?.season,
                    seasonYear = mediaItem?.startDate?.year
                )
            )
        }
    }

    data class Data(
        val param: IParam,
        val header: Header,
    ) {
        data class Header(
            val title: String,
            val description: String,
        )
    }
}

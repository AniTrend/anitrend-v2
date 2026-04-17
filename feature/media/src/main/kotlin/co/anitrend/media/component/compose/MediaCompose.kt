/*
 * Copyright (C) 2021 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.media.component.compose

import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import co.anitrend.android.core.compose.AniTrendDimensions
import co.anitrend.android.core.compose.design.BackIconButton
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.format
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.media.ui.compose.component.score.MediaScoreSection
import co.anitrend.common.media.ui.compose.component.status.MediaStatusSection
import co.anitrend.common.media.ui.compose.widget.title.MediaMetaLineText
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.ContributorsSection
import co.anitrend.media.component.compose.section.MediaCommunitySection
import co.anitrend.media.component.compose.section.MediaConnectionsBrowserSection
import co.anitrend.media.component.compose.section.MediaExtendedMetadataSection
import co.anitrend.media.component.compose.section.MediaGallerySection
import co.anitrend.media.component.compose.section.MediaGenrePreviewSection
import co.anitrend.media.component.compose.section.MediaRankPreviewSection
import co.anitrend.media.component.compose.section.MediaStudiosPreviewSection
import co.anitrend.media.component.compose.section.MediaSupplementalInfoSection
import co.anitrend.media.component.compose.section.MediaSynopsisPreviewSection
import co.anitrend.media.component.compose.section.MediaTagSection
import co.anitrend.media.component.compose.section.MediaTrailerSection
import co.anitrend.media.component.compose.section.MediaThemeSection
import co.anitrend.media.component.compose.stats.MediaStatsSection
import co.anitrend.media.component.viewmodel.MediaCharactersViewModel
import co.anitrend.media.component.viewmodel.MediaCommunityViewModel
import co.anitrend.media.component.viewmodel.MediaRecommendationsViewModel
import co.anitrend.media.component.viewmodel.MediaRelationsViewModel
import co.anitrend.media.component.viewmodel.MediaStaffViewModel
import co.anitrend.media.component.viewmodel.MediaStatsViewModel
import co.anitrend.media.component.viewmodel.MediaStudiosViewModel
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaEpisodeRouter
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.MediaRecommendationsRouter
import co.anitrend.navigation.MediaRelationsRouter
import co.anitrend.navigation.MediaStatsRouter
import co.anitrend.navigation.MediaStudiosRouter
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.ReviewRouter
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.extensions.toDataBuilder
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private fun Float.asDisplayRating(scoreFormat: ScoreFormat): IMediaRating =
    when (scoreFormat) {
        ScoreFormat.POINT_3 ->
            when (toInt()) {
                1 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
                2 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
                3 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
                else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
            }

        ScoreFormat.POINT_10_DECIMAL -> IMediaRating.Text(format(1))
        else -> IMediaRating.Text(toInt().toString())
    }

private fun Media.Extended.personalRating(scoreFormat: ScoreFormat): IMediaRating? =
    mediaList
        ?.score
        ?.takeIf { it > 0f }
        ?.asDisplayRating(scoreFormat)
        ?: score.personal?.takeIf { it > 0f }?.asDisplayRating(scoreFormat)

private fun mediaListStatusIcon(mediaListStatus: MediaListStatus?): ImageVector =
    when (mediaListStatus) {
        MediaListStatus.CURRENT -> Icons.Default.PlayCircleOutline
        MediaListStatus.COMPLETED -> Icons.Default.DoneOutline
        MediaListStatus.DROPPED -> Icons.Default.DeleteOutline
        MediaListStatus.PAUSED -> Icons.Default.PauseCircleOutline
        MediaListStatus.PLANNING -> Icons.Default.AccessTime
        MediaListStatus.REPEATING -> Icons.Rounded.Repeat
        null -> Icons.Rounded.BookmarkAdd
    }

private fun Media.Extended.secondaryTitle(): String? {
    val preferred = title.userPreferred?.toString()?.trim()

    return listOf(title.english, title.romaji, title.native)
        .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
        .firstOrNull { it != preferred }
}

private fun Media.Extended.displayTitle(): String? =
    title.userPreferred
        ?.toString()
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?: listOf(title.english, title.romaji, title.native)
            .firstNotNullOfOrNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }

private fun Media.Extended.heroFacts(): List<String> =
    buildList {
        status
            ?.alias
            ?.toString()
            ?.takeIf(String::isNotBlank)
            ?.let(::add)
        source
            ?.alias
            ?.toString()
            ?.takeIf(String::isNotBlank)
            ?.let(::add)
    }

@Composable
private fun MediaStatePill(
    label: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    val horizontalPadding = if (compact) 10.dp else 12.dp
    val verticalPadding = if (compact) 6.dp else 8.dp

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = if (compact) 0.68f else 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(if (compact) 14.dp else 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.34f)),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp),
        ) {
            leadingContent?.invoke()
            Text(
                text = label,
                style = if (compact) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun MediaHeroHeader(
    media: Media.Extended,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val secondaryTitle = media.secondaryTitle()
    val heroFacts = remember(media) { media.heroFacts() }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.BANNER,
            onClick = onImageClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(216.dp),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(216.dp)
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.surface.copy(alpha = 0.08f),
                            0.55f to MaterialTheme.colorScheme.background.copy(alpha = 0.14f),
                            0.88f to MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                            1f to MaterialTheme.colorScheme.background,
                        ),
                    ),
        )
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(30.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.34f)),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .absoluteOffset(y = 32.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier =
                        Modifier
                            .width(92.dp)
                            .height(132.dp),
                ) {
                    AniTrendImage(
                        image = media.image,
                        imageType = RequestImage.Media.ImageType.POSTER,
                        onClick = onImageClick,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(AniTrendDimensions.series_image_aspect_ratio),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text =
                            media.title.userPreferred
                                ?.toString()
                                .orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    MediaMetaLineText(
                        media = media,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    secondaryTitle?.let {
                        Text(
                            text = it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (heroFacts.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            heroFacts.forEach { fact ->
                                MediaStatePill(
                                    label = fact,
                                    compact = true,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaPrimaryActionDock(
    media: Media.Extended,
    onManageListClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onMyAnimeListClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOnMyList = media.mediaList != null
    val status = media.mediaList?.status

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledTonalButton(
                    onClick = onManageListClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Icon(
                        imageVector = mediaListStatusIcon(status),
                        contentDescription = null,
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text =
                            stringResource(
                                if (isOnMyList) {
                                    R.string.action_manage_on_list
                                } else {
                                    R.string.action_add_to_list
                                },
                            ),
                    )
                }

                if (media.isFavourite) {
                    FilledTonalButton(
                        onClick = onFavouriteClick,
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(R.string.label_media_user_state_favourite))
                    }
                } else {
                    OutlinedButton(
                        onClick = onFavouriteClick,
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(R.string.action_add_to_favourites))
                    }
                }
            }

            media.siteUrl.myAnimeList?.let { url ->
                OutlinedButton(
                    onClick = { onMyAnimeListClick(url) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_my_anime_list),
                        contentDescription = null,
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.action_my_anime_list))
                }
            }
        }
    }
}

@Composable
private fun MediaDetailContent(
    media: Media.Extended,
    scoreFormat: ScoreFormat,
    authenticatedUserId: Long,
    onManageListClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onMyAnimeListButtonClick: (String) -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onEpisodeGuideClick: (MediaEpisodeRouter.MediaEpisodeParam) -> Unit,
    onMediaConnectionItemClick: (IParam) -> Unit,
    onExternalLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    characters: LazyPagingItems<MediaPerson.Character>? = null,
    staff: LazyPagingItems<MediaPerson.Staff>? = null,
    studios: List<MediaStudioEntry>? = null,
    studiosLoadState: LoadState? = null,
    stats: MediaStats? = null,
    statsLoadState: LoadState? = null,
    relations: List<MediaRelationEntry>? = null,
    relationsLoadState: LoadState? = null,
    recommendations: List<MediaRecommendationEntry>? = null,
    recommendationsLoadState: LoadState? = null,
    communityReviews: LazyPagingItems<Review>? = null,
    onPeopleClick: (MediaPeopleRouter.MediaPeopleParam) -> Unit = {},
    onStudioClick: (StudioRouter.StudioParam) -> Unit = {},
    onSeeAllStudiosClick: (MediaStudiosRouter.MediaStudiosParam) -> Unit = {},
    onSeeAllStatsClick: (MediaStatsRouter.MediaStatsParam) -> Unit = {},
    onRelatedClick: (MediaRelationsRouter.MediaRelationsParam) -> Unit = {},
    onRecommendationsClick: (MediaRecommendationsRouter.MediaRecommendationsParam) -> Unit = {},
    onCommunityClick: (ReviewDiscoverRouter.ReviewDiscoverParam) -> Unit = {},
    onReviewClick: (ReviewRouter.ReviewParam) -> Unit = {},
    isCommunityVotePending: (Long) -> Boolean = { false },
    onCommunityVoteRequested: (Review, ReviewRating) -> Unit = { _, _ -> },
    onRetryCharacters: () -> Unit = {},
    onRetryStaff: () -> Unit = {},
    onRetryStudios: () -> Unit = {},
    onRetryStats: () -> Unit = {},
    onRetryRelations: () -> Unit = {},
    onRetryRecommendations: () -> Unit = {},
    onRetryCommunity: () -> Unit = {},
) {
    val mediaTitle = media.displayTitle()

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MediaHeroHeader(
                    media = media,
                    onImageClick = onImageClick,
                )
                Spacer(modifier = Modifier.height(60.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MediaScoreSection(
                        mediaScore = media.score,
                        scoreFormat = scoreFormat,
                        compact = true,
                    )

                    MediaPrimaryActionDock(
                        media = media,
                        onManageListClick = onManageListClick,
                        onFavouriteClick = onFavouriteClick,
                        onMyAnimeListClick = onMyAnimeListButtonClick,
                    )

                    MediaStatusSection(
                        media = media,
                        onOpenEpisodeGuide = {
                            onEpisodeGuideClick(
                                MediaEpisodeRouter.MediaEpisodeParam(
                                    mediaId = media.id,
                                    mediaType = media.category.type,
                                    mediaTitle = mediaTitle,
                                ),
                            )
                        },
                    )

                    MediaSupplementalInfoSection(
                        extraInfo = media.extraInfo,
                    )
                }
            }
        }

        item {
            MediaSynopsisPreviewSection(
                synopsis = media,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            MediaExtendedMetadataSection(
                media = media,
                themes = media.themes,
                onExternalLinkClick = onExternalLinkClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        if (media.trailers.isNotEmpty()) {
            item {
                MediaTrailerSection(
                    trailers = media.trailers,
                    onTrailerClick = onExternalLinkClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        if (media.gallery.isNotEmpty()) {
            item {
                MediaGallerySection(
                    images = media.gallery,
                    onImageClick = onImageClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        if (media.genres.isNotEmpty()) {
            item {
                MediaGenrePreviewSection(
                    genres = media.genres,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            MediaStatsSection(
                media = media,
                stats = stats,
                loadState = statsLoadState,
                onRetry = onRetryStats,
                onSeeAllClick = {
                    onSeeAllStatsClick(
                        MediaStatsRouter.MediaStatsParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            averageScore = media.score.mean.takeIf { it > 0 },
                            favourites = media.favourites.takeIf { it > 0 },
                            popularity = media.score.popularity?.takeIf { it > 0 },
                            trendRank = media.score.trending?.takeIf { it > 0 },
                        ),
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        if (media.tags.isNotEmpty()) {
            item {
                MediaTagSection(
                    tags = media.tags,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        if (media.themes.isNotEmpty()) {
            item {
                MediaThemeSection(
                    themes = media.themes,
                )
            }
        }

        item {
            MediaConnectionsBrowserSection(
                relations = relations,
                relationsLoadState = relationsLoadState,
                recommendations = recommendations.takeUnless { media.isRecommendationBlocked },
                recommendationsLoadState = recommendationsLoadState,
                scoreFormat = scoreFormat,
                onMediaItemClick = onMediaConnectionItemClick,
                onSeeAllRelated = {
                    onRelatedClick(
                        MediaRelationsRouter.MediaRelationsParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                        ),
                    )
                },
                onSeeAllRecommendations = {
                    onRecommendationsClick(
                        MediaRecommendationsRouter.MediaRecommendationsParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                        ),
                    )
                },
                onRetryRelations = onRetryRelations,
                onRetryRecommendations = onRetryRecommendations,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            ContributorsSection(
                characters = characters,
                staff = staff,
                onSeeAllCharacters = {
                    onPeopleClick(
                        MediaPeopleRouter.MediaPeopleParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            initialSection = MediaPeopleRouter.Section.CHARACTERS,
                        ),
                    )
                },
                onSeeAllStaff = {
                    onPeopleClick(
                        MediaPeopleRouter.MediaPeopleParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            initialSection = MediaPeopleRouter.Section.STAFF,
                        ),
                    )
                },
                onCharacterClick = {
                    onPeopleClick(
                        MediaPeopleRouter.MediaPeopleParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            initialSection = MediaPeopleRouter.Section.CHARACTERS,
                        ),
                    )
                },
                onStaffClick = {
                    onPeopleClick(
                        MediaPeopleRouter.MediaPeopleParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            initialSection = MediaPeopleRouter.Section.STAFF,
                        ),
                    )
                },
                onRetryCharacters = onRetryCharacters,
                onRetryStaff = onRetryStaff,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        item {
            MediaStudiosPreviewSection(
                studios = studios,
                studiosLoadState = studiosLoadState,
                onStudioClick = onStudioClick,
                onSeeAllClick = {
                    onSeeAllStudiosClick(
                        MediaStudiosRouter.MediaStudiosParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                        ),
                    )
                },
                onRetry = onRetryStudios,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        if (media.rankings.isNotEmpty()) {
            item {
                MediaRankPreviewSection(
                    ranks = media.rankings.toList(),
                    onClick = { rank, sorting ->
                        onMediaDiscoverableItemClick(
                            MediaDiscoverRouter.MediaDiscoverParam(
                                type = media.category.type,
                                format = media.format,
                                season = media.season,
                                seasonYear = if (rank.allTime != true && media.category is Media.Category.Anime) rank.year else null,
                                startDate_like = if (rank.allTime != true && media.category is Media.Category.Manga) "${rank.year}%" else null,
                                sort = sorting,
                            ),
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            MediaCommunitySection(
                reviews = communityReviews,
                scoreFormat = scoreFormat,
                isBlocked = media.isReviewBlocked,
                authenticatedUserId = authenticatedUserId,
                onSeeAllClick = {
                    onCommunityClick(
                        ReviewDiscoverRouter.ReviewDiscoverParam(
                            mediaId = media.id,
                            mediaType = media.category.type,
                            sort = MediaCommunityViewModel.previewSort,
                            scoreFormat = scoreFormat,
                        ),
                    )
                },
                onRetry = onRetryCommunity,
                onReviewClick = { reviewId ->
                    onReviewClick(
                        ReviewRouter.ReviewParam(
                            id = reviewId,
                        ),
                    )
                },
                isVotePending = isCommunityVotePending,
                onVoteRequested = onCommunityVoteRequested,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }

}

@Composable
fun MediaScreenContent(
    mediaState: MediaViewModel,
    onMyAnimeListButtonClick: (String) -> Unit,
    onManageListButtonClick: (View, Media) -> Unit,
    onFavouriteButtonClick: (View, FavouriteTaskRouter.Param) -> Unit,
    onFloatingActionButtonClick: (Media) -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onEpisodeGuideClick: (MediaEpisodeRouter.MediaEpisodeParam) -> Unit,
    onMediaConnectionItemClick: (IParam) -> Unit,
    onPeopleClick: (MediaPeopleRouter.MediaPeopleParam) -> Unit,
    onStudioClick: (StudioRouter.StudioParam) -> Unit,
    onSeeAllStudiosClick: (MediaStudiosRouter.MediaStudiosParam) -> Unit,
    onSeeAllStatsClick: (MediaStatsRouter.MediaStatsParam) -> Unit,
    onRelatedClick: (MediaRelationsRouter.MediaRelationsParam) -> Unit,
    onRecommendationsClick: (MediaRecommendationsRouter.MediaRecommendationsParam) -> Unit,
    onCommunityClick: (ReviewDiscoverRouter.ReviewDiscoverParam) -> Unit,
    onReviewClick: (ReviewRouter.ReviewParam) -> Unit,
    onExternalLinkClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by mediaState.model.observeAsState()
    val media = state as? Media.Extended ?: return
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pendingCommunityVotes = remember { mutableStateMapOf<Long, Boolean>() }
    val workManager = remember(context) { WorkManager.getInstance(context) }

    val userSettings: IUserSettings = koinInject()
    val charactersViewModel: MediaCharactersViewModel = koinViewModel()
    val staffViewModel: MediaStaffViewModel = koinViewModel()
    val studiosViewModel: MediaStudiosViewModel = koinViewModel()
    val statsViewModel: MediaStatsViewModel = koinViewModel()
    val relationsViewModel: MediaRelationsViewModel = koinViewModel()
    val recommendationsViewModel: MediaRecommendationsViewModel = koinViewModel()
    val communityViewModel: MediaCommunityViewModel = koinViewModel()
    val studios by studiosViewModel.model.observeAsState()
    val studiosLoadState by studiosViewModel.loadState.observeAsState()
    val stats by statsViewModel.model.observeAsState()
    val statsLoadState by statsViewModel.loadState.observeAsState()
    val relations by relationsViewModel.model.observeAsState()
    val relationsLoadState by relationsViewModel.loadState.observeAsState()
    val recommendations by recommendationsViewModel.model.observeAsState()
    val recommendationsLoadState by recommendationsViewModel.loadState.observeAsState()
    val scoreFormat: ScoreFormat by userSettings.scoreFormat.flow.collectAsStateWithLifecycle(IUserSettings.DEFAULT_SCORE_FORMAT)
    val authenticatedUserId: Long by userSettings.authenticatedUserId.flow.collectAsStateWithLifecycle(IAuthenticationSettings.INVALID_USER_ID)

    val characters = remember(media.id) { charactersViewModel.characters(media.id) }.collectAsLazyPagingItems()
    val staff = remember(media.id) { staffViewModel.staff(media.id) }.collectAsLazyPagingItems()

    val communityReviews =
        if (media.isReviewBlocked) {
            null
        } else {
            remember(media.id, media.category.type, scoreFormat) {
                communityViewModel.reviews(
                    mediaId = media.id,
                    mediaType = media.category.type,
                    scoreFormat = scoreFormat,
                )
            }.collectAsLazyPagingItems()
        }

    val view = LocalView.current

    LaunchedEffect(media.id, scoreFormat) {
        studiosViewModel(media.id)
        statsViewModel(media.id)
        relationsViewModel(media.id, scoreFormat)
        if (!media.isRecommendationBlocked) {
            recommendationsViewModel(media.id, scoreFormat = scoreFormat)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onFloatingActionButtonClick(media) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = stringResource(co.anitrend.android.core.R.string.action_share),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        MediaDetailContent(
            media = media,
            scoreFormat = scoreFormat,
            authenticatedUserId = authenticatedUserId,
            onManageListClick = { onManageListButtonClick(view, media) },
            onFavouriteClick = {
                val param =
                    FavouriteTaskRouter.Param.MediaToggleParam(
                        id = media.id,
                        mediaType = media.category.type,
                    )
                onFavouriteButtonClick(view, param)
            },
            onMyAnimeListButtonClick = onMyAnimeListButtonClick,
            onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            onImageClick = onImageClick,
            onEpisodeGuideClick = onEpisodeGuideClick,
            onMediaConnectionItemClick = onMediaConnectionItemClick,
            onExternalLinkClick = onExternalLinkClick,
            characters = characters,
            staff = staff,
            studios = studios,
            studiosLoadState = studiosLoadState,
            stats = stats,
            statsLoadState = statsLoadState,
            relations = relations,
            relationsLoadState = relationsLoadState,
            recommendations = recommendations,
            recommendationsLoadState = recommendationsLoadState,
            communityReviews = communityReviews,
            onPeopleClick = onPeopleClick,
            onStudioClick = onStudioClick,
            onSeeAllStudiosClick = onSeeAllStudiosClick,
            onSeeAllStatsClick = onSeeAllStatsClick,
            onRelatedClick = onRelatedClick,
            onRecommendationsClick = onRecommendationsClick,
            onCommunityClick = onCommunityClick,
            onReviewClick = onReviewClick,
            isCommunityVotePending = { reviewId -> pendingCommunityVotes[reviewId] == true },
            onCommunityVoteRequested = { review, rating ->
                if (pendingCommunityVotes[review.id] != true) {
                    scope.launch {
                        pendingCommunityVotes[review.id] = true
                        try {
                            val terminalState =
                                submitReviewVote(
                                    workManager = workManager,
                                    reviewId = review.id,
                                    rating = rating,
                                )

                            if (terminalState == WorkInfo.State.SUCCEEDED) {
                                communityReviews?.refresh()
                            }
                        } finally {
                            pendingCommunityVotes.remove(review.id)
                        }
                    }
                }
            },
            onRetryCharacters = characters::retry,
            onRetryStaff = staff::retry,
            onRetryStudios = { studiosViewModel(media.id) },
            onRetryStats = { statsViewModel(media.id) },
            onRetryRelations = { relationsViewModel(media.id, scoreFormat) },
            onRetryRecommendations = { recommendationsViewModel(media.id, scoreFormat = scoreFormat) },
            onRetryCommunity = { communityReviews?.retry() },
            modifier =
                Modifier
                    .padding(innerPadding),
        )
    }
}

private suspend fun submitReviewVote(
    workManager: WorkManager,
    reviewId: Long,
    rating: ReviewRating,
): WorkInfo.State {
    val params =
        ReviewTaskRouter.Param.RateEntry(
            id = reviewId,
            rating = rating,
        )
    val request =
        OneTimeWorkRequest
            .Builder(ReviewTaskRouter.forReviewRateWorker())
            .setInputData(params.toDataBuilder().build())
            .build()

    workManager.enqueue(request)

    return workManager
        .getWorkInfoByIdFlow(request.id)
        .filterNotNull()
        .first { it.state.isFinished }
        .state
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaDetailComponentPreview(
    @PreviewParameter(MediaComposePreviewProvider::class) media: Media.Extended,
) {
    PreviewTheme(wrapInSurface = true) {
        MediaDetailContent(
            media = media,
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            authenticatedUserId = IAuthenticationSettings.INVALID_USER_ID,
            onManageListClick = {},
            onFavouriteClick = {},
            onMyAnimeListButtonClick = {},
            onMediaDiscoverableItemClick = {},
            onImageClick = {},
            onEpisodeGuideClick = {},
            onMediaConnectionItemClick = {},
            onExternalLinkClick = {},
            modifier = Modifier,
            onSeeAllStudiosClick = {},
        )
    }
}

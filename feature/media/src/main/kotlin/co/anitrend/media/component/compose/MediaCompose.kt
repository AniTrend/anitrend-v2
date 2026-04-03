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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.AniTrendDimensions
import co.anitrend.android.core.compose.design.BackIconButton
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.compose.design.image.AniTrendImageDefaults
import co.anitrend.android.core.extensions.format
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.genre.ui.compose.MediaGenreSection
import co.anitrend.common.genre.ui.compose.MediaGenreSectionMode
import co.anitrend.common.media.ui.compose.component.IconScoreContent
import co.anitrend.common.media.ui.compose.component.rank.MediaRankSection
import co.anitrend.common.media.ui.compose.component.score.MediaScoreSection
import co.anitrend.common.media.ui.compose.component.status.MediaStatusSection
import co.anitrend.common.media.ui.compose.component.synopsis.MediaSynopsisSection
import co.anitrend.common.media.ui.compose.section.MediaHeaderInfoSection
import co.anitrend.common.media.ui.compose.widget.title.MediaSubTitleText
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.MediaExtendedMetadataSection
import co.anitrend.media.component.compose.section.MediaTagSection
import co.anitrend.media.component.compose.section.MediaThemeSection
import co.anitrend.media.component.schedule.MediaScheduleSheet
import co.anitrend.media.component.viewmodel.MediaScheduleViewModel
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import org.koin.androidx.compose.koinViewModel
import co.anitrend.common.media.ui.R as MediaUiR

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

private fun Media.Extended.hasVisibleUserState(scoreFormat: ScoreFormat): Boolean = mediaList?.status != null || personalRating(scoreFormat) != null

private fun mediaListStatusIcon(mediaListStatus: MediaListStatus?): Int? =
    when (mediaListStatus) {
        MediaListStatus.CURRENT -> MediaUiR.drawable.ic_current
        MediaListStatus.COMPLETED -> MediaUiR.drawable.ic_completed
        MediaListStatus.DROPPED -> MediaUiR.drawable.ic_dropped
        MediaListStatus.PAUSED -> MediaUiR.drawable.ic_paused
        MediaListStatus.PLANNING -> MediaUiR.drawable.ic_planning
        MediaListStatus.REPEATING -> MediaUiR.drawable.ic_repeat
        null -> null
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
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (compact) 0.34f else 0.45f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(if (compact) 14.dp else 16.dp),
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
private fun MediaIdentityBlock(
    media: Media.Extended,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textInset = (AniTrendDimensions.series_image_lg * AniTrendDimensions.series_image_aspect_ratio) + 16.dp
    val extraInfo = media.extraInfo?.trim()?.takeIf(String::isNotBlank)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        MediaHeaderInfoSection(
            media = media,
            onCoverClick = onImageClick,
            preferExtendedExtraInfo = false,
            compact = true,
            modifier = Modifier.absoluteOffset(y = (-12).dp),
        )
        MediaSubTitleText(
            media = media,
            modifier = Modifier.padding(start = textInset),
            style =
                MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )
        extraInfo?.let {
            Text(
                text = it,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = textInset),
            )
        }
    }
}

@Composable
private fun MediaUserStateSummary(
    media: Media.Extended,
    scoreFormat: ScoreFormat,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val userRating = media.personalRating(scoreFormat)
    val listStatus = media.mediaList?.status

    if (listStatus == null && userRating == null) {
        return
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp),
    ) {
        listStatus?.let { status ->
            MediaStatePill(
                label = status.alias.toString(),
                compact = compact,
                leadingContent = {
                    mediaListStatusIcon(status)?.let { icon ->
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(if (compact) 16.dp else 18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        }

        when (userRating) {
            is IMediaRating.Text ->
                MediaStatePill(
                    label = "${stringResource(MediaUiR.string.label_media_score_section_your_rating)} ${userRating.score}",
                    compact = compact,
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(if (compact) 16.dp else 18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                )

            is IMediaRating.Mood ->
                MediaStatePill(
                    label = stringResource(MediaUiR.string.label_media_score_section_your_rating),
                    compact = compact,
                    leadingContent = {
                        IconScoreContent(
                            rating = userRating,
                            iconTint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(if (compact) 16.dp else 18.dp),
                        )
                    },
                )

            null -> Unit
        }
    }
}

@Composable
private fun MediaPrimaryActionDock(
    media: Media.Extended,
    onManageListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOnMyList = media.mediaList != null

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        FilledTonalButton(
            onClick = onManageListClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
        ) {
            Icon(
                imageVector = if (isOnMyList) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
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
    }
}

@Composable
private fun MediaSynopsisPreviewSection(
    media: Media.Extended,
    modifier: Modifier = Modifier,
) {
    MediaSynopsisSection(
        synopsis = media,
        collapsedMaxLines = 4,
        modifier = modifier,
    )
}

@Composable
private fun MediaDetailContent(
    media: Media.Extended,
    scoreFormat: ScoreFormat,
    onManageListClick: () -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasUserState = media.hasVisibleUserState(scoreFormat)

    Column(modifier = modifier) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.BANNER,
            onClick = onImageClick,
            modifier = AniTrendImageDefaults.BANNER_SIZE,
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = (-16).dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var showScheduleSheet by remember { mutableStateOf(false) }

                MediaIdentityBlock(
                    media = media,
                    onImageClick = onImageClick,
                )
                MediaScoreSection(
                    mediaScore = media.score,
                    scoreFormat = scoreFormat,
                    compact = true,
                    supportingContent =
                        if (hasUserState) {
                            {
                                MediaUserStateSummary(
                                    media = media,
                                    scoreFormat = scoreFormat,
                                    compact = true,
                                )
                            }
                        } else {
                            null
                        },
                )
                MediaPrimaryActionDock(
                    media = media,
                    onManageListClick = onManageListClick,
                )
                MediaSynopsisPreviewSection(
                    media = media,
                )
                MediaStatusSection(
                    media = media,
                    onShowSchedule = {
                        showScheduleSheet = media.category is Media.Category.Anime
                    },
                )
                MediaExtendedMetadataSection(
                    media = media,
                )
                if (media.themes.isNotEmpty()) {
                    MediaThemeSection(
                        themes = media.themes,
                    )
                }
                if (showScheduleSheet && media.category is Media.Category.Anime) {
                    val scheduleViewModel: MediaScheduleViewModel = koinViewModel()
                    MediaScheduleSheet(
                        mediaId = media.id,
                        onDismiss = { showScheduleSheet = false },
                        viewModel = scheduleViewModel,
                    )
                }
                if (media.rankings.isNotEmpty()) {
                    MediaRankSection(
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
                    )
                }
                if (media.genres.isNotEmpty()) {
                    MediaGenreSection(
                        genres = media.genres as List<Genre>,
                        onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                        sectionMode = MediaGenreSectionMode.FLEX,
                    )
                }
                if (media.tags.isNotEmpty()) {
                    MediaTagSection(
                        tags = media.tags,
                        onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                    )
                }
            }
        }
    }
}

@Composable
fun MediaScreenContent(
    mediaState: MediaViewModel,
    scoreFormat: ScoreFormat,
    onMyAnimeListButtonClick: (String) -> Unit,
    onBookmarkButtonClick: (View, Media) -> Unit,
    onFavouriteButtonClick: (View, FavouriteTaskRouter.Param) -> Unit,
    onFloatingActionButtonClick: (Media) -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by mediaState.model.observeAsState()
    val media = state as? Media.Extended ?: return

    val view = LocalView.current

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                    if (media.siteUrl.myAnimeList != null) {
                        val url = requireNotNull(media.siteUrl.myAnimeList)
                        IconButton(onClick = { onMyAnimeListButtonClick(url) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_my_anime_list),
                                contentDescription = stringResource(R.string.action_my_anime_list),
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            val param =
                                FavouriteTaskRouter.Param.MediaToggleParam(
                                    id = media.id,
                                    mediaType = media.category.type,
                                )
                            onFavouriteButtonClick(view, param)
                        },
                    ) {
                        Icon(
                            imageVector = if (media.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription =
                                stringResource(
                                    if (media.isFavourite) {
                                        R.string.action_remove_from_favourites
                                    } else {
                                        R.string.action_add_to_favourites
                                    },
                                ),
                        )
                    }
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
            onManageListClick = { onBookmarkButtonClick(view, media) },
            onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            onImageClick = onImageClick,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
        )
    }
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
            onManageListClick = {},
            onMediaDiscoverableItemClick = {},
            onImageClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState()),
        )
    }
}

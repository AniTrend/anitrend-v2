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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.PagedList
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.android.core.compose.AniTrendDimensions
import co.anitrend.android.core.compose.design.BackIconButton
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.format
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.component.IconScoreContent
import co.anitrend.common.media.ui.compose.component.score.MediaScoreSection
import co.anitrend.common.media.ui.compose.component.status.MediaStatusSection
import co.anitrend.common.media.ui.compose.widget.title.MediaSubTitleText
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.people.MediaPeopleSection
import co.anitrend.media.component.compose.section.MediaExtendedMetadataSection
import co.anitrend.media.component.compose.section.MediaGenrePreviewSection
import co.anitrend.media.component.compose.section.MediaRankPreviewSection
import co.anitrend.media.component.compose.section.MediaSynopsisPreviewSection
import co.anitrend.media.component.compose.section.MediaTagSection
import co.anitrend.media.component.schedule.MediaScheduleSheet
import co.anitrend.media.component.viewmodel.MediaCharactersViewModel
import co.anitrend.media.component.viewmodel.MediaScheduleViewModel
import co.anitrend.media.component.viewmodel.MediaStaffViewModel
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaPeopleRouter
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

private fun Media.Extended.hasVisibleUserState(scoreFormat: ScoreFormat): Boolean =
    mediaList?.status != null || personalRating(scoreFormat) != null || isFavourite

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
            .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
            .firstOrNull()

private fun resolvePeopleInitialSection(
    characters: PagedList<MediaPerson.Character>?,
    staff: PagedList<MediaPerson.Staff>?,
): MediaPeopleRouter.Section =
    when {
        staff?.isNotEmpty() == true && characters?.isEmpty() != false -> MediaPeopleRouter.Section.STAFF
        else -> MediaPeopleRouter.Section.CHARACTERS
    }

private fun Media.Extended.heroFacts(): List<String> =
    buildList {
        season
            ?.alias
            ?.toString()
            ?.takeIf(String::isNotBlank)
            ?.let(::add)
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
    val extraInfo = media.extraInfo?.trim()?.takeIf(String::isNotBlank)
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
                    MediaSubTitleText(
                        media = media,
                        style =
                            MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                    )
                    secondaryTitle?.let {
                        Text(
                            text = it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    extraInfo?.let {
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
private fun MediaUserStateSummary(
    media: Media.Extended,
    scoreFormat: ScoreFormat,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val userRating = media.personalRating(scoreFormat)
    val listStatus = media.mediaList?.status

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

        if (media.isFavourite) {
            MediaStatePill(
                label = stringResource(R.string.label_media_user_state_favourite),
                compact = compact,
                leadingContent = {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(if (compact) 16.dp else 18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
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
    onManageListClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onMyAnimeListButtonClick: (String) -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    characters: PagedList<MediaPerson.Character>? = null,
    charactersLoadState: LoadState? = null,
    staff: PagedList<MediaPerson.Staff>? = null,
    staffLoadState: LoadState? = null,
    onPeopleClick: (MediaPeopleRouter.MediaPeopleParam) -> Unit = {},
    onRetryCharacters: () -> Unit = {},
    onRetryStaff: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showScheduleSheet by remember { mutableStateOf(false) }
    val mediaTitle = media.displayTitle()
    val defaultPeopleSection = resolvePeopleInitialSection(characters = characters, staff = staff)
    val hasUserState = media.hasVisibleUserState(scoreFormat)

    Column(modifier = modifier) {
        MediaHeroHeader(
            media = media,
            onImageClick = onImageClick,
        )

        Spacer(modifier = Modifier.size(48.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
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
                onFavouriteClick = onFavouriteClick,
                onMyAnimeListClick = onMyAnimeListButtonClick,
            )

            MediaStatusSection(
                media = media,
                onShowSchedule = {
                    showScheduleSheet = media.category is Media.Category.Anime
                },
            )

            MediaSynopsisPreviewSection(
                synopsis = media,
            )

            if (media.rankings.isNotEmpty()) {
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
                )
            }

            if (media.genres.isNotEmpty()) {
                MediaGenrePreviewSection(
                    genres = media.genres,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                )
            }

            if (media.tags.isNotEmpty()) {
                MediaTagSection(
                    tags = media.tags,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                )
            }

            MediaPeopleSection(
                characters = characters,
                charactersLoadState = charactersLoadState,
                staff = staff,
                staffLoadState = staffLoadState,
                onSeeAllClick = {
                    onPeopleClick(
                        MediaPeopleRouter.MediaPeopleParam(
                            mediaId = media.id,
                            mediaTitle = mediaTitle,
                            initialSection = defaultPeopleSection,
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
            )

            MediaExtendedMetadataSection(
                media = media,
                themes = media.themes,
            )
        }
    }

    if (showScheduleSheet && media.category is Media.Category.Anime) {
        val scheduleViewModel: MediaScheduleViewModel = koinViewModel()
        MediaScheduleSheet(
            mediaId = media.id,
            onDismiss = { showScheduleSheet = false },
            viewModel = scheduleViewModel,
        )
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
    onPeopleClick: (MediaPeopleRouter.MediaPeopleParam) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by mediaState.model.observeAsState()
    val media = state as? Media.Extended ?: return
    val charactersViewModel: MediaCharactersViewModel = koinViewModel()
    val staffViewModel: MediaStaffViewModel = koinViewModel()
    val characters by charactersViewModel.model.observeAsState()
    val charactersLoadState by charactersViewModel.loadState.observeAsState()
    val staff by staffViewModel.model.observeAsState()
    val staffLoadState by staffViewModel.loadState.observeAsState()

    val view = LocalView.current

    LaunchedEffect(media.id) {
        charactersViewModel(media.id)
        staffViewModel(media.id)
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
            onManageListClick = { onBookmarkButtonClick(view, media) },
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
            characters = characters,
            charactersLoadState = charactersLoadState,
            staff = staff,
            staffLoadState = staffLoadState,
            onPeopleClick = onPeopleClick,
            onRetryCharacters = { charactersViewModel(media.id) },
            onRetryStaff = { staffViewModel(media.id) },
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
            onFavouriteClick = {},
            onMyAnimeListButtonClick = {},
            onMediaDiscoverableItemClick = {},
            onImageClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState()),
        )
    }
}

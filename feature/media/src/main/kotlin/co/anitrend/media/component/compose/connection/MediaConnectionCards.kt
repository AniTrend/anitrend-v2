/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.media.component.compose.connection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.component.MediaRating
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRelation
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.recommendation.enums.RecommendationRating
import co.anitrend.media.R
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.model.common.IParam
import kotlin.math.abs

internal val ConnectionRailCardWidth = 176.dp
private val ConnectionRailCardNarrowWidth = 148.dp

private val ConnectionCardShape = RoundedCornerShape(22.dp)
private val ConnectionPosterShape = RoundedCornerShape(18.dp)

@Composable
internal fun RelatedMediaCard(
    relation: MediaRelationEntry,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val media = relation.media
    val relationLabel = relation.relation?.alias?.toString() ?: stringResource(R.string.label_media_related_relation_unknown)

    MediaConnectionCardSurface(modifier = modifier) {
        ConnectionPoster(
            media = media,
            scoreFormat = scoreFormat,
            onMediaItemClick = onMediaItemClick,
            topStart = {
                ConnectionChip(
                    label = relationLabel,
                    containerColor = relationChipContainerColor(media.image),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            },
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            ConnectionTitle(media = media)
            MediaQuickFacts(media = media)
            ConnectionSupportLine(media = media)
        }
    }
}

@Composable
internal fun RecommendationMediaCard(
    recommendation: MediaRecommendationEntry,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    rationaleMaxLines: Int = 2,
) {
    val media = recommendation.media

    MediaConnectionCardSurface(modifier = modifier) {
        ConnectionPoster(
            media = media,
            scoreFormat = scoreFormat,
            onMediaItemClick = onMediaItemClick,
            topStart = {
                ConnectionChip(
                    label = recommendationSignalLabel(recommendation.rating),
                    containerColor = recommendationChipContainerColor(media.image),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            },
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            ConnectionTitle(media = media)
            MediaQuickFacts(media = media)

            Text(
                text = recommendationRationaleLabel(recommendation),
                maxLines = rationaleMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            recommendationVoteLabel(recommendation.userRating)
                ?.let { footer ->
                    Text(
                        text = footer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
        }
    }
}

@Composable
private fun MediaConnectionCardSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = ConnectionCardShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
        tonalElevation = 0.dp,
        modifier = modifier,
    ) {
        Column(content = content)
    }
}

@Composable
private fun ConnectionPoster(
    media: Media,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    topStart: @Composable () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(0.74f)
                .padding(10.dp)
                .clip(ConnectionPosterShape),
    ) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.POSTER,
            onClick = {
                onMediaItemClick(
                    MediaRouter.MediaParam(
                        id = media.id,
                        type = media.category.type,
                    ),
                )
            },
            onLongClick = {
                onMediaItemClick(
                    MediaListEditorRouter.MediaListEditorParam(
                        mediaId = media.id,
                        mediaType = media.category.type,
                        scoreFormat = scoreFormat,
                    ),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.66f))),
                    ),
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
        ) {
            topStart()
        }

        MediaRating(
            media = media,
            scoreFormat = scoreFormat,
            modifier = Modifier.align(Alignment.BottomStart).padding(10.dp),
        )
    }
}

@Composable
private fun ConnectionTitle(
    media: Media,
    modifier: Modifier = Modifier,
) {
    Text(
        text =
            listOf(media.title.userPreferred, media.title.english, media.title.romaji, media.title.native)
                .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
                .firstOrNull()
                .orEmpty(),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

@Composable
private fun MediaQuickFacts(
    media: Media,
    modifier: Modifier = Modifier,
) {
    val facts = buildQuickFacts(media)
    if (facts.isEmpty()) {
        return
    }

    Text(
        text = facts.joinToString(separator = " • "),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun ConnectionSupportLine(
    media: Media,
    modifier: Modifier = Modifier,
) {
    when {
        media.category is Media.Category.Anime && (media.category as Media.Category.Anime).schedule != null -> {
            AiringScheduleText(
                media = media,
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier,
            )
        }

        media.status != null -> {
            Text(
                text =
                    media.status
                        ?.alias
                        ?.toString()
                        .orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ConnectionChip(
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun recommendationSignalLabel(rating: Int?): String =
    rating?.let {
        if (it > 0) {
            stringResource(R.string.label_media_recommendations_signal_value, it)
        } else {
            stringResource(R.string.label_media_recommendations_signal_signed, if (it < 0) -abs(it) else 0)
        }
    }
        ?: stringResource(R.string.label_media_recommendations_signal_unknown)

@Composable
private fun recommendationRationaleLabel(recommendation: MediaRecommendationEntry): String =
    recommendation.userName
        ?.takeIf(String::isNotBlank)
        ?.let { stringResource(R.string.label_media_recommendations_rationale_user, it) }
        ?: stringResource(R.string.label_media_recommendations_rationale_unknown)

@Composable
private fun recommendationVoteLabel(rating: RecommendationRating?): String? =
    when (rating) {
        RecommendationRating.RATE_UP -> stringResource(R.string.label_media_recommendations_vote_up)
        RecommendationRating.RATE_DOWN -> stringResource(R.string.label_media_recommendations_vote_down)
        RecommendationRating.NO_RATING,
        null,
        -> null
    }

@Composable
private fun buildQuickFacts(media: Media): List<String> =
    buildList {
        media.startDate.year
            .takeIf { it > 0 }
            ?.let { add(it.toString()) }
        media.format
            ?.alias
            ?.toString()
            ?.takeIf(String::isNotBlank)
            ?.let(::add)
        media.category.quickFactLabel()?.let(::add)
    }

@Composable
private fun Media.Category.quickFactLabel(): String? =
    when (this) {
        is Media.Category.Anime ->
            episodes.takeIf { it > 0 }?.let {
                pluralStringResource(R.plurals.label_media_connection_episodes_short, it, it)
            }

        is Media.Category.Manga ->
            chapters.takeIf { it > 0 }?.let {
                pluralStringResource(R.plurals.label_media_connection_chapters_short, it, it)
            } ?: volumes.takeIf { it > 0 }?.let {
                pluralStringResource(R.plurals.label_media_connection_volumes_short, it, it)
            }
    }

@Composable
private fun relationChipContainerColor(image: IMediaCover): Color {
    val accent = image.rememberAccentColor()
    return remember(accent) { accent.copy(alpha = 0.82f) }
}

@Composable
private fun recommendationChipContainerColor(image: IMediaCover): Color {
    val accent = image.rememberAccentColor()
    return remember(accent) { accent.copy(alpha = 0.72f) }
}

@AniTrendPreview.Default
@Composable
private fun RelatedMediaCardPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        RelatedMediaCard(
            relation =
                MediaRelationEntry(
                    relation = MediaRelation.SEQUEL,
                    media = previewMedia(isFavourite = true),
                    id = 1L,
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            modifier = Modifier.width(ConnectionRailCardWidth),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun RecommendationMediaCardPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        RecommendationMediaCard(
            recommendation =
                MediaRecommendationEntry(
                    media = previewMedia(isFavourite = false),
                    rating = 84,
                    userName = "Shoji",
                    userRating = RecommendationRating.RATE_UP,
                    id = 2L,
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            modifier = Modifier.width(ConnectionRailCardWidth),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun RecommendationMediaCardSparsePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        RecommendationMediaCard(
            recommendation =
                MediaRecommendationEntry(
                    media = previewMedia(isFavourite = false).copy(status = null),
                    rating = null,
                    userName = null,
                    userRating = null,
                    id = 3L,
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            modifier = Modifier.width(ConnectionRailCardWidth),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun RelatedMediaCardNarrowPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        RelatedMediaCard(
            relation =
                MediaRelationEntry(
                    relation = MediaRelation.SIDE_STORY,
                    media =
                        previewMedia(
                            title = "Legend of the Galactic Heroes: Die Neue These - Intrigue",
                            isFavourite = false,
                            episodes = 12,
                        ),
                    id = 4L,
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            modifier = Modifier.width(ConnectionRailCardNarrowWidth),
        )
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
private fun RecommendationMediaCardNarrowPreview() {
    PreviewTheme(darkTheme = false, wrapInSurface = true) {
        RecommendationMediaCard(
            recommendation =
                MediaRecommendationEntry(
                    media =
                        previewMedia(
                            title = "March Comes in like a Lion: Season 2",
                            isFavourite = true,
                            episodes = 22,
                        ),
                    rating = 126,
                    userName = "Akari from the community favourites circle",
                    userRating = RecommendationRating.RATE_UP,
                    id = 5L,
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            modifier = Modifier.width(ConnectionRailCardNarrowWidth),
            rationaleMaxLines = 1,
        )
    }
}

private fun previewMedia(
    title: String = "Cowboy Bebop: The Movie",
    isFavourite: Boolean,
    episodes: Int = 1,
) =
    Media.Core.empty().copy(
        title =
            MediaTitle(
                userPreferred = title,
                english = title,
                romaji = "Cowboy Bebop: Tengoku no Tobira",
                native = "カウボーイビバップ 天国の扉",
            ),
        status = MediaStatus.FINISHED,
        image = MediaImage.empty().copy(color = "#5B6FD8"),
        startDate = FuzzyDate.empty().copy(2001),
        format = MediaFormat.MOVIE,
        category =
            Media.Category.Anime
                .empty()
                .copy(episodes),
        score =
            MediaScore(
                average = 82,
                mean = 84,
                personal = null,
                popularity = 150000,
                trending = 0,
            ),
        isFavourite = isFavourite,
    )

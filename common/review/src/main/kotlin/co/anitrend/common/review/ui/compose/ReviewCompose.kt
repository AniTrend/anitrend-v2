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
package co.anitrend.common.review.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.asPrettyTime
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.domain.common.extension.asFormattedScore
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import org.threeten.bp.Instant
import co.anitrend.common.review.R as ReviewR

enum class ReviewCardVariant(
    internal val defaultExcerptMaxLines: Int,
) {
    Discover(defaultExcerptMaxLines = 4),
    InlineCommunity(defaultExcerptMaxLines = 3),
}

private val DiscoverCardShape = RoundedCornerShape(18.dp)
private val InlineCommunityCardShape = RoundedCornerShape(14.dp)
private val ReaderCardShape = RoundedCornerShape(20.dp)
private val ReviewPosterShape = RoundedCornerShape(14.dp)
private val ReviewVoteSegmentShape = RoundedCornerShape(10.dp)

@Composable
fun ReviewBrowseCard(
    review: Review,
    scoreFormat: ScoreFormat,
    variant: ReviewCardVariant,
    canVote: Boolean,
    isVotePending: Boolean,
    modifier: Modifier = Modifier,
    excerptMaxLines: Int = variant.defaultExcerptMaxLines,
    onOpen: () -> Unit,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    ReviewCardSurface(
        variant = variant,
        modifier = modifier,
        onClick = onOpen,
    ) {
        when (variant) {
            ReviewCardVariant.Discover ->
                ReviewDiscoverCardContent(
                    review = review,
                    scoreFormat = scoreFormat,
                    excerptMaxLines = excerptMaxLines,
                    canVote = canVote,
                    isVotePending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )

            ReviewCardVariant.InlineCommunity ->
                ReviewInlineCommunityCardContent(
                    review = review,
                    scoreFormat = scoreFormat,
                    excerptMaxLines = excerptMaxLines,
                    canVote = canVote,
                    isVotePending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )
        }
    }
}

@Composable
fun ReviewLoadingCard(
    variant: ReviewCardVariant,
    modifier: Modifier = Modifier,
) {
    ReviewCardSurface(
        variant = variant,
        modifier = modifier,
    ) {
        when (variant) {
            ReviewCardVariant.Discover ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    ReviewSkeletonPoster(
                        modifier =
                            Modifier
                                .width(58.dp)
                                .aspectRatio(0.72f),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ReviewSkeletonCircle(size = 26.dp)
                            ReviewSkeletonLine(width = 84.dp, height = 12.dp)
                            Spacer(modifier = Modifier.weight(1f))
                            ReviewSkeletonLine(width = 24.dp, height = 12.dp)
                        }
                        ReviewSkeletonLine(widthFraction = 0.72f, height = 18.dp)
                        ReviewSkeletonLine(widthFraction = 0.94f, height = 15.dp)
                        ReviewSkeletonLine(widthFraction = 0.82f, height = 15.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ReviewSkeletonBlock(width = 86.dp, height = 30.dp)
                            ReviewSkeletonLine(width = 62.dp, height = 10.dp)
                        }
                    }
                }

            ReviewCardVariant.InlineCommunity ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReviewSkeletonLine(
                            modifier = Modifier.weight(1f),
                            widthFraction = 1f,
                            height = 16.dp,
                        )
                        ReviewSkeletonLine(width = 44.dp, height = 12.dp)
                        ReviewSkeletonLine(width = 58.dp, height = 12.dp)
                    }
                    ReviewSkeletonLine(widthFraction = 0.9f, height = 14.dp)
                    ReviewSkeletonLine(widthFraction = 0.74f, height = 14.dp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReviewSkeletonBlock(width = 82.dp, height = 30.dp)
                        Spacer(modifier = Modifier.weight(1f))
                        ReviewSkeletonLine(width = 72.dp, height = 12.dp)
                    }
                }
        }
    }
}

@Composable
fun ReviewVoteActionRow(
    review: Review,
    canVote: Boolean,
    isPending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!canVote) {
        Row(
            modifier = modifier.heightIn(min = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReviewPassiveVoteMetric(
                icon = ReviewR.drawable.ic_thumb_up,
                label = review.upVoteCount(),
                accessibilityLabel = review.upVoteLabel(),
            )
            ReviewPassiveVoteMetric(
                icon = ReviewR.drawable.ic_thumb_down,
                label = review.downVoteCount(),
                accessibilityLabel = review.downVoteLabel(),
            )
        }
        return
    }

    Row(
        modifier = modifier.heightIn(min = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReviewVoteSegment(
            icon = ReviewR.drawable.ic_thumb_up,
            label = review.upVoteCount(),
            accessibilityLabel = review.upVoteLabel(),
            selected = canVote && review.userRating == ReviewRating.UP_VOTE,
            accentColor = MaterialTheme.colorScheme.primary,
            enabled = canVote && !isPending,
            onClick = { onVoteRequested(review.toggleUpVoteTarget()) },
        )
        ReviewVoteSegment(
            icon = ReviewR.drawable.ic_thumb_down,
            label = review.downVoteCount(),
            accessibilityLabel = review.downVoteLabel(),
            selected = canVote && review.userRating == ReviewRating.DOWN_VOTE,
            accentColor = MaterialTheme.colorScheme.tertiary,
            enabled = canVote && !isPending,
            onClick = { onVoteRequested(review.toggleDownVoteTarget()) },
        )
    }
}

@Composable
fun ReviewReaderContent(
    review: Review,
    scoreFormat: ScoreFormat,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
    modifier: Modifier = Modifier,
) {
    val description =
        review.description
            ?.toString()
            ?.trim()
            .orEmpty()
    val summary = review.summaryText()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        review.mediaOrNull()?.let {
            ReviewBannerHeader(
                media = it,
                height = 152.dp,
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = ReaderCardShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.22f)),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ReviewReaderHeader(
                    review = review,
                    scoreFormat = scoreFormat,
                )
                ReviewVoteActionRow(
                    review = review,
                    canVote = canVote,
                    isPending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )

                if (description.isBlank()) {
                    ReviewSummaryFallback(summary = summary)
                } else {
                    MarkdownText(content = description)
                }
            }
        }
    }
}

@Composable
private fun ReviewDiscoverCardContent(
    review: Review,
    scoreFormat: ScoreFormat,
    excerptMaxLines: Int,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    val media = review.mediaOrNull()
    val mediaTitle = review.mediaTitle()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ReviewPosterThumbnail(
            media = media,
            fallbackLabel = mediaTitle.orEmpty(),
            modifier =
                Modifier
                    .width(58.dp)
                    .aspectRatio(0.72f),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            ReviewAuthorRow(
                name = review.authorName(),
                avatar = review.user.avatar,
                trailingContent = {
                    ReviewScoreToken(
                        score = review.score,
                        scoreFormat = scoreFormat,
                    )
                },
            )

            ReviewMediaContext(
                title = mediaTitle ?: review.authorName(),
            )

            ReviewExcerpt(
                text = review.summaryText(),
                maxLines = excerptMaxLines,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            )

            ReviewDiscoverFooter(
                review = review,
                canVote = canVote,
                isVotePending = isVotePending,
                onVoteRequested = onVoteRequested,
            )
        }
    }
}

@Composable
private fun ReviewInlineCommunityCardContent(
    review: Review,
    scoreFormat: ScoreFormat,
    excerptMaxLines: Int,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReviewAuthorRow(
            name = review.authorName(),
            avatar = review.user.avatar,
            trailingContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ReviewScoreToken(
                        score = review.score,
                        scoreFormat = scoreFormat,
                    )
                }
            },
        )

        ReviewExcerpt(
            text = review.summaryText(),
            maxLines = excerptMaxLines,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        )

        ReviewInlineCommunityFooter(
            review = review,
            canVote = canVote,
            isVotePending = isVotePending,
            onVoteRequested = onVoteRequested,
        )
    }
}

@Composable
private fun ReviewReaderHeader(
    review: Review,
    scoreFormat: ScoreFormat,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        review
            .mediaTitle()
            ?.takeIf(String::isNotBlank)
            ?.let { title ->
                ReviewMediaContext(title = title)
            }

        ReviewAuthorRow(
            name = review.authorName(),
            avatar = review.user.avatar,
            trailingContent = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ReviewScoreToken(
                        score = review.score,
                        scoreFormat = scoreFormat,
                        emphasized = true,
                    )
                    ReviewMetaText(
                        text = review.timeLabel(),
                        maxLines = 1,
                    )
                }
            },
        )
    }
}

@Composable
private fun ReviewSummaryFallback(summary: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.22f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(ReviewR.string.label_review_reader_summary_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(ReviewR.string.message_review_reader_summary_fallback),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ReviewExcerpt(
                text = summary,
                maxLines = Int.MAX_VALUE,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ReviewDiscoverFooter(
    review: Review,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ReviewVoteActionRow(
            review = review,
            canVote = canVote,
            isPending = isVotePending,
            onVoteRequested = onVoteRequested,
        )
        ReviewMetaText(
            text = review.timeLabel(),
            maxLines = 1,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun ReviewInlineCommunityFooter(
    review: Review,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReviewVoteActionRow(
            review = review,
            canVote = canVote,
            isPending = isVotePending,
            onVoteRequested = onVoteRequested,
        )
        Spacer(modifier = Modifier.weight(1f))
        ReviewMetaText(
            text = review.timeLabel(),
            maxLines = 1,
        )
    }
}

@Composable
private fun ReviewVoteSegment(
    icon: Int,
    label: String,
    accessibilityLabel: String,
    selected: Boolean,
    accentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor =
        if (selected) {
            accentColor
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    val backgroundColor =
        if (selected) {
            accentColor.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        }

    Row(
        modifier =
            Modifier
                .clip(ReviewVoteSegmentShape)
                .background(backgroundColor)
                .then(
                    if (enabled) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ).semantics {
                    contentDescription = accessibilityLabel
                    this.selected = selected
                    if (enabled) {
                        role = Role.Button
                    }
                }.defaultMinSize(minWidth = 44.dp, minHeight = 40.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .alpha(if (enabled || !selected) 1f else 0.74f),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReviewPassiveVoteMetric(
    icon: Int,
    label: String,
    accessibilityLabel: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.semantics {
                contentDescription = accessibilityLabel
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReviewCardSurface(
    variant: ReviewCardVariant,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape =
        when (variant) {
            ReviewCardVariant.Discover -> DiscoverCardShape
            ReviewCardVariant.InlineCommunity -> InlineCommunityCardShape
        }
    val containerColor =
        when (variant) {
            ReviewCardVariant.Discover -> MaterialTheme.colorScheme.surfaceContainerLow
            ReviewCardVariant.InlineCommunity -> MaterialTheme.colorScheme.surfaceContainerLowest
        }
    val borderAlpha =
        when (variant) {
            ReviewCardVariant.Discover -> 0.2f
            ReviewCardVariant.InlineCommunity -> 0.14f
        }

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = shape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = borderAlpha)),
    ) {
        content()
    }
}

@Composable
private fun ReviewBannerHeader(
    media: Media,
    modifier: Modifier = Modifier,
    height: Dp = 96.dp,
) {
    AniTrendImage(
        image = media.image,
        imageType = RequestImage.Media.ImageType.BANNER,
        contentDescription = media.displayTitle(),
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .fillMaxWidth()
                .height(height)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 18.dp,
                        bottomEnd = 18.dp
                    )
                ),
    )
}

@Composable
private fun ReviewPosterThumbnail(
    media: Media?,
    fallbackLabel: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = ReviewPosterShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = fallbackLabel.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
            )

            media?.let {
                AniTrendImage(
                    image = it.image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    contentDescription = it.displayTitle(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ReviewMediaContext(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ReviewAuthorRow(
    name: String,
    avatar: UserImage,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReviewAvatar(
            name = name,
            avatar = avatar,
            size = 26.dp,
        )

        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        trailingContent?.invoke()
    }
}

@Composable
private fun ReviewAvatar(
    name: String,
    avatar: UserImage,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        AniTrendImage(
            image = avatar,
            imageType = RequestImage.Media.ImageType.POSTER,
            contentDescription = name,
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
        )
    }
}

@Composable
private fun ReviewScoreToken(
    score: Int,
    scoreFormat: ScoreFormat,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
) {
    val displayScore = score.asFormattedScore(scoreFormat)
    val scoreContentDescription = stringResource(ReviewR.string.label_review_score, displayScore)
    val textColor =
        if (emphasized) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Row(
        modifier =
            modifier.semantics {
                contentDescription = scoreContentDescription
            },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(ReviewR.drawable.ic_star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(if (emphasized) 16.dp else 14.dp),
        )
        Text(
            text = displayScore,
            style = if (emphasized) MaterialTheme.typography.titleSmall else MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReviewMetaText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ReviewExcerpt(
    text: String,
    maxLines: Int,
    style: TextStyle,
) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = if (maxLines == Int.MAX_VALUE) TextOverflow.Clip else TextOverflow.Ellipsis,
        style = style,
    )
}

@Composable
private fun ReviewSkeletonCircle(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    ReviewSkeletonBlock(
        modifier = modifier.size(size),
        width = 0.dp,
        height = 0.dp,
        shape = CircleShape,
    )
}

@Composable
private fun ReviewSkeletonPoster(modifier: Modifier = Modifier) {
    ReviewSkeletonBlock(
        modifier = modifier,
        width = 0.dp,
        height = 0.dp,
        shape = ReviewPosterShape,
    )
}

@Composable
private fun ReviewSkeletonLine(
    modifier: Modifier = Modifier,
    widthFraction: Float? = null,
    width: Dp? = null,
    height: Dp,
) {
    ReviewSkeletonBlock(
        modifier =
            modifier.then(
                when {
                    width != null -> Modifier.width(width)
                    widthFraction != null -> Modifier.fillMaxWidth(widthFraction)
                    else -> Modifier
                },
            ),
        width = 0.dp,
        height = height,
        shape = RoundedCornerShape(6.dp),
    )
}

@Composable
private fun ReviewSkeletonBlock(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    Spacer(
        modifier =
            modifier
                .then(if (width > 0.dp) Modifier.width(width) else Modifier)
                .then(if (height > 0.dp) Modifier.height(height) else Modifier)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.85f)),
    )
}

@Composable
private fun Review.authorName(): String =
    user.name
        .toString()
        .trim()
        .ifBlank { stringResource(ReviewR.string.label_review_unknown_author) }

@Composable
private fun Review.upVoteLabel(): String = stringResource(ReviewR.string.label_review_up_votes, upVoteCount())

@Composable
private fun Review.downVoteLabel(): String = stringResource(ReviewR.string.label_review_down_votes, downVoteCount())

@Composable
private fun Review.timeLabel(): String {
    val referenceTime = Instant.ofEpochSecond(if (updatedAt > createdAt) updatedAt else createdAt).asPrettyTime()
    return if (updatedAt > createdAt) {
        stringResource(ReviewR.string.label_review_updated, referenceTime)
    } else {
        referenceTime
    }
}

private fun Review.upVoteCount(): String = rating.toHumanReadableQuantity(1)

private fun Review.downVoteCount(): String = (ratingAmount - rating).coerceAtLeast(0).toHumanReadableQuantity(1)

private fun Review.summaryText(): String =
    summary
        .ifBlank { description?.toString().orEmpty() }
        .replace("\n", " ")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun Review.toggleUpVoteTarget(): ReviewRating =
    if (userRating == ReviewRating.UP_VOTE) {
        ReviewRating.NO_VOTE
    } else {
        ReviewRating.UP_VOTE
    }

private fun Review.toggleDownVoteTarget(): ReviewRating =
    if (userRating == ReviewRating.DOWN_VOTE) {
        ReviewRating.NO_VOTE
    } else {
        ReviewRating.DOWN_VOTE
    }

private fun Review.mediaOrNull(): Media? = (this as? Review.Extended)?.media

private fun Review.mediaTitle(): String? = mediaOrNull()?.displayTitle()

private fun Media.displayTitle(): String? =
    title.userPreferred
        ?.toString()
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?: listOf(title.english, title.romaji, title.native)
            .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
            .firstOrNull()

private fun previewReview(
    userRating: ReviewRating = ReviewRating.NO_VOTE,
    userId: Long = 7L,
    summary: String = "A reflective science fiction review that balances spectacle, melancholy, and strong character payoff.",
): Review.Extended =
    Review.Extended(
        media =
            Media.Core.empty().copy(
                title =
                    MediaTitle(
                        userPreferred = "Vivy: Fluorite Eye's Song",
                        english = "Vivy -Fluorite Eye's Song-",
                        romaji = "Vivy: Fluorite Eye's Song",
                        native = "Vivy -Fluorite Eye's Song-",
                    ),
                image = MediaImage.empty().copy(color = "#406882"),
                category =
                    Media.Category.Anime
                        .empty()
                        .copy(episodes = 13),
                id = 1,
            ),
        description =
            "## Premise\nA long-form markdown review body with deliberate pacing, emotional highlights, and a [reference link](https://anilist.co).",
        createdAt = 1_706_000_000L,
        mediaId = 1L,
        mediaType = co.anitrend.domain.media.enums.MediaType.ANIME,
        private = false,
        rating = 184,
        ratingAmount = 212,
        score = 89,
        siteUrl = "https://anilist.co/review/1",
        summary = summary,
        updatedAt = 1_706_086_400L,
        user =
            User.Core(
                name = "Hikari Tanaka",
                avatar = UserImage(large = null, medium = null, banner = null),
                status = UserStatus.empty(),
                id = userId,
            ),
        userId = userId,
        userRating = userRating,
        id = 1L,
    )

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun ReviewBrowseDiscoverPreview() {
    PreviewTheme(wrapInSurface = true) {
        ReviewBrowseCard(
            review = previewReview(),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            variant = ReviewCardVariant.Discover,
            canVote = true,
            isVotePending = false,
            onOpen = {},
            onVoteRequested = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun ReviewBrowseCommunityPreview() {
    PreviewTheme(wrapInSurface = true) {
        ReviewBrowseCard(
            review =
                previewReview(
                    summary = "The production is disciplined, the direction is sharp, and the quieter emotional beats land harder than expected.",
                ),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            variant = ReviewCardVariant.InlineCommunity,
            canVote = true,
            isVotePending = false,
            onOpen = {},
            onVoteRequested = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun ReviewVoteStatesPreview() {
    PreviewTheme(wrapInSurface = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ReviewBrowseCard(
                review = previewReview(userRating = ReviewRating.UP_VOTE),
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                variant = ReviewCardVariant.Discover,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userRating = ReviewRating.DOWN_VOTE),
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                variant = ReviewCardVariant.InlineCommunity,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(),
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                variant = ReviewCardVariant.InlineCommunity,
                canVote = true,
                isVotePending = true,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userId = 99L),
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                variant = ReviewCardVariant.InlineCommunity,
                canVote = false,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun ReviewLoadingPreview() {
    PreviewTheme(wrapInSurface = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ReviewLoadingCard(
                variant = ReviewCardVariant.Discover,
            )
            ReviewLoadingCard(
                variant = ReviewCardVariant.InlineCommunity,
            )
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun ReviewReaderContentPreview() {
    PreviewTheme(wrapInSurface = true) {
        ReviewReaderContent(
            review = previewReview(userRating = ReviewRating.UP_VOTE),
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            canVote = true,
            isVotePending = false,
            onVoteRequested = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

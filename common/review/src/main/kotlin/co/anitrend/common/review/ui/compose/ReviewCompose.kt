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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.asPrettyTime
import co.anitrend.android.core.compose.design.image.rememberRequestImage
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.helpers.image.toRequestBuilder
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import coil.compose.AsyncImage
import org.threeten.bp.Instant
import co.anitrend.common.review.R as ReviewR

enum class ReviewCardVariant(
    internal val defaultExcerptMaxLines: Int,
) {
    Discover(defaultExcerptMaxLines = 4),
    InlineCommunity(defaultExcerptMaxLines = 3),
}

private val DiscoverCardShape = RoundedCornerShape(22.dp)
private val InlineCommunityCardShape = RoundedCornerShape(18.dp)
private val ReaderCardShape = RoundedCornerShape(24.dp)
private val ReviewPosterShape = RoundedCornerShape(16.dp)
private val ReviewVoteShape = RoundedCornerShape(14.dp)

@Composable
fun ReviewBrowseCard(
    review: Review,
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
                    excerptMaxLines = excerptMaxLines,
                    canVote = canVote,
                    isVotePending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )

            ReviewCardVariant.InlineCommunity ->
                ReviewInlineCommunityCardContent(
                    review = review,
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
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ReviewSkeletonLine(widthFraction = 0.66f, height = 18.dp)
                        ReviewSkeletonLine(widthFraction = 0.38f, height = 12.dp)
                        ReviewSkeletonLine(widthFraction = 0.94f, height = 14.dp)
                        ReviewSkeletonLine(widthFraction = 0.86f, height = 14.dp)
                        ReviewSkeletonLine(widthFraction = 0.52f, height = 14.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ReviewSkeletonBlock(width = 122.dp, height = 36.dp)
                            ReviewSkeletonLine(width = 72.dp, height = 10.dp)
                        }
                    }
                }

            ReviewCardVariant.InlineCommunity ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ReviewSkeletonLine(
                            modifier = Modifier.weight(1f),
                            widthFraction = 1f,
                            height = 16.dp,
                        )
                        ReviewSkeletonLine(width = 52.dp, height = 12.dp)
                        ReviewSkeletonLine(width = 66.dp, height = 12.dp)
                    }
                    ReviewSkeletonLine(widthFraction = 0.96f, height = 14.dp)
                    ReviewSkeletonLine(widthFraction = 0.84f, height = 14.dp)
                    ReviewSkeletonLine(widthFraction = 0.58f, height = 14.dp)
                    ReviewSkeletonBlock(width = 122.dp, height = 34.dp)
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
    Surface(
        modifier = modifier.heightIn(min = 38.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.58f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = ReviewVoteShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.26f)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReviewVoteSegment(
                icon = ReviewR.drawable.ic_thumb_up,
                label = review.upVoteLabel(),
                selected = canVote && review.userRating == ReviewRating.UP_VOTE,
                accentColor = MaterialTheme.colorScheme.primary,
                enabled = canVote && !isPending,
                onClick = { onVoteRequested(review.toggleUpVoteTarget()) },
                shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp),
            )
            Box(
                modifier =
                    Modifier
                        .width(1.dp)
                        .height(18.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.22f)),
            )
            ReviewVoteSegment(
                icon = ReviewR.drawable.ic_thumb_down,
                label = review.downVoteLabel(),
                selected = canVote && review.userRating == ReviewRating.DOWN_VOTE,
                accentColor = MaterialTheme.colorScheme.tertiary,
                enabled = canVote && !isPending,
                onClick = { onVoteRequested(review.toggleDownVoteTarget()) },
                shape = RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp),
            )
        }
    }
}

@Composable
fun ReviewReaderContent(
    review: Review,
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f)),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ReviewReaderHeader(review = review)
                ReviewVoteActionRow(
                    review = review,
                    canVote = canVote,
                    isPending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )

                if (description.isBlank()) {
                    ReviewSummaryFallback(
                        summary = summary,
                    )
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = mediaTitle ?: review.authorName(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = review.authorByline(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                ReviewScoreLabel(
                    score = review.score,
                    prominent = false,
                )
            }

            ReviewSummaryText(
                text = review.summaryText(),
                maxLines = excerptMaxLines,
                style = MaterialTheme.typography.bodyLarge,
            )

            ReviewSentimentRow(
                review = review,
                canVote = canVote,
                isVotePending = isVotePending,
                showTimeLabel = true,
                onVoteRequested = onVoteRequested,
            )
        }
    }
}

@Composable
private fun ReviewInlineCommunityCardContent(
    review: Review,
    excerptMaxLines: Int,
    canVote: Boolean,
    isVotePending: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = review.authorName(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                ReviewMetaText(
                    text = review.timeLabel(),
                    maxLines = 1,
                )
                ReviewScoreLabel(
                    score = review.score,
                    prominent = false,
                )
            }

            ReviewSummaryText(
                text = review.summaryText(),
                maxLines = excerptMaxLines,
                style = MaterialTheme.typography.bodyMedium,
            )

            ReviewSentimentRow(
                review = review,
                canVote = canVote,
                isVotePending = isVotePending,
                showTimeLabel = false,
                onVoteRequested = onVoteRequested,
            )
        }
    }
}

@Composable
private fun ReviewReaderHeader(review: Review) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        review
            .mediaTitle()
            ?.takeIf(String::isNotBlank)
            ?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = review.authorName(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                ReviewMetaText(
                    text = review.timeLabel(),
                    maxLines = 1,
                )
            }

            ReviewScoreLabel(
                score = review.score,
                prominent = true,
            )
        }
    }
}

@Composable
private fun ReviewSummaryFallback(summary: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
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
            ReviewSummaryText(
                text = summary,
                maxLines = Int.MAX_VALUE,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ReviewSentimentRow(
    review: Review,
    canVote: Boolean,
    isVotePending: Boolean,
    showTimeLabel: Boolean,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ReviewVoteActionRow(
            review = review,
            canVote = canVote,
            isPending = isVotePending,
            onVoteRequested = onVoteRequested,
        )

        if (showTimeLabel) {
            ReviewMetaText(
                text = review.timeLabel(),
                maxLines = 1,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun ReviewVoteSegment(
    icon: Int,
    label: String,
    selected: Boolean,
    accentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    shape: Shape,
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
                .clip(shape)
                .background(backgroundColor)
                .then(
                    if (enabled) {
                        Modifier
                            .clickable(onClick = onClick)
                            .semantics {
                                role = Role.Button
                                this.selected = selected
                            }
                    } else {
                        Modifier
                    },
                ).defaultMinSize(minWidth = 58.dp, minHeight = 38.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp)
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
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = shape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.26f)),
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
    val context = LocalContext.current
    val requestBuilder =
        rememberRequestImage(
            image = media.image,
            type = RequestImage.Media.ImageType.BANNER,
        ) {
            toRequestBuilder(context)
        }

    AsyncImage(
        model = requestBuilder.build(),
        contentDescription = media.displayTitle(),
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp)),
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
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
                val context = LocalContext.current
                val requestBuilder =
                    rememberRequestImage(
                        image = it.image,
                        type = RequestImage.Media.ImageType.POSTER,
                    ) {
                        toRequestBuilder(context)
                    }

                AsyncImage(
                    model = requestBuilder.build(),
                    contentDescription = it.displayTitle(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ReviewScoreLabel(
    score: Int,
    prominent: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentColor =
        if (prominent) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(ReviewR.drawable.ic_star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(if (prominent) 18.dp else 16.dp),
        )
        Text(
            text = stringResource(ReviewR.string.label_review_score, score),
            style = if (prominent) MaterialTheme.typography.titleSmall else MaterialTheme.typography.labelLarge,
            fontWeight = if (prominent) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor,
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
private fun ReviewSummaryText(
    text: String,
    maxLines: Int,
    style: androidx.compose.ui.text.TextStyle,
) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = if (maxLines == Int.MAX_VALUE) TextOverflow.Clip else TextOverflow.Ellipsis,
        style = style,
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
private fun Review.authorByline(): String = stringResource(ReviewR.string.label_review_by_author, authorName())

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
                variant = ReviewCardVariant.Discover,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userRating = ReviewRating.DOWN_VOTE),
                variant = ReviewCardVariant.InlineCommunity,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(),
                variant = ReviewCardVariant.InlineCommunity,
                canVote = true,
                isVotePending = true,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userId = 99L),
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
            canVote = true,
            isVotePending = false,
            onVoteRequested = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

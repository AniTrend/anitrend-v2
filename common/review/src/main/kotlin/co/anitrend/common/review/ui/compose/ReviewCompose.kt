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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import coil.transform.CircleCropTransformation
import org.threeten.bp.Instant
import co.anitrend.common.review.R as ReviewR

private val ReviewCardShape = RoundedCornerShape(24.dp)

@Composable
fun ReviewBrowseCard(
    review: Review,
    showMediaContext: Boolean,
    summaryMaxLines: Int,
    canVote: Boolean,
    isVotePending: Boolean,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit,
    onVoteRequested: (ReviewRating) -> Unit,
) {
    val mediaTitle = review.mediaTitle()
    val timeLabel = review.timeLabel()

    ReviewCardSurface(
        modifier = modifier,
        onClick = onOpen,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            ReviewHeaderRow(review = review)

            if (showMediaContext) {
                mediaTitle
                    ?.takeIf(String::isNotBlank)
                    ?.let { title ->
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
            }

            ReviewSummaryText(
                text = review.summaryText(),
                maxLines = summaryMaxLines,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ReviewVoteActionRow(
                    review = review,
                    canVote = canVote,
                    isPending = isVotePending,
                    onVoteRequested = onVoteRequested,
                )
                ReviewMetaChip(text = timeLabel)
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
    val upVoteLabel = review.upVoteLabel()
    val downVoteLabel = review.downVoteLabel()

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (canVote) {
            ReviewVoteButton(
                icon = ReviewR.drawable.ic_thumb_up,
                label = upVoteLabel,
                selected = review.userRating == ReviewRating.UP_VOTE,
                enabled = !isPending,
                containerColor =
                    voteContainerColor(
                        selected = review.userRating == ReviewRating.UP_VOTE,
                        selectedContainer = MaterialTheme.colorScheme.primaryContainer,
                        defaultContainer = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                contentColor =
                    voteContentColor(
                        selected = review.userRating == ReviewRating.UP_VOTE,
                        selectedContent = MaterialTheme.colorScheme.onPrimaryContainer,
                        defaultContent = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                borderColor =
                    voteBorderColor(
                        selected = review.userRating == ReviewRating.UP_VOTE,
                        selectedBorder = MaterialTheme.colorScheme.primary,
                        defaultBorder = MaterialTheme.colorScheme.outlineVariant,
                    ),
                onClick = { onVoteRequested(review.toggleUpVoteTarget()) },
            )
            ReviewVoteButton(
                icon = ReviewR.drawable.ic_thumb_down,
                label = downVoteLabel,
                selected = review.userRating == ReviewRating.DOWN_VOTE,
                enabled = !isPending,
                containerColor =
                    voteContainerColor(
                        selected = review.userRating == ReviewRating.DOWN_VOTE,
                        selectedContainer = MaterialTheme.colorScheme.tertiaryContainer,
                        defaultContainer = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                contentColor =
                    voteContentColor(
                        selected = review.userRating == ReviewRating.DOWN_VOTE,
                        selectedContent = MaterialTheme.colorScheme.onTertiaryContainer,
                        defaultContent = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                borderColor =
                    voteBorderColor(
                        selected = review.userRating == ReviewRating.DOWN_VOTE,
                        selectedBorder = MaterialTheme.colorScheme.tertiary,
                        defaultBorder = MaterialTheme.colorScheme.outlineVariant,
                    ),
                onClick = { onVoteRequested(review.toggleDownVoteTarget()) },
            )
        } else {
            ReviewCountPill(
                icon = ReviewR.drawable.ic_thumb_up,
                label = upVoteLabel,
            )
            ReviewCountPill(
                icon = ReviewR.drawable.ic_thumb_down,
                label = downVoteLabel,
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
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
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
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.26f)),
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
                            )
                        }
                    }
                } else {
                    MarkdownText(content = description)
                }
            }
        }
    }
}

@Composable
private fun ReviewCardSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(ReviewCardShape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = ReviewCardShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f)),
    ) {
        content()
    }
}

@Composable
private fun ReviewBannerHeader(
    media: Media,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 96.dp,
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
private fun ReviewHeaderRow(review: Review) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        ReviewAuthorMetaRow(
            review = review,
            modifier = Modifier.weight(1f),
        )
        ReviewScorePill(score = review.score)
    }
}

@Composable
private fun ReviewReaderHeader(review: Review) {
    val mediaTitle = review.mediaTitle()
    val voteLabel = review.voteLabel()
    val timeLabel = review.timeLabel()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        ReviewHeaderRow(review = review)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            mediaTitle
                ?.takeIf(String::isNotBlank)
                ?.let { ReviewMetaChip(text = it) }

            voteLabel?.let { ReviewMetaChip(text = it) }
            ReviewMetaChip(text = timeLabel)
        }
    }
}

@Composable
private fun ReviewAuthorMetaRow(
    review: Review,
    modifier: Modifier = Modifier,
) {
    val authorName = review.authorName()
    val secondaryMetaText = review.browseMetaLabel()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReviewAvatar(
            name = authorName,
            avatar = review.user.avatar,
        )

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = authorName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = secondaryMetaText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ReviewAvatar(
    name: String,
    avatar: UserImage,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val avatarUrl =
        avatar.large
            ?.toString()
            ?.trim()
            ?.takeUnless { it.isBlank() }
            ?: avatar.medium
                ?.toString()
                ?.trim()
                ?.takeUnless { it.isBlank() }

    Surface(
        modifier = modifier.size(44.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        if (avatarUrl != null) {
            val requestBuilder =
                rememberRequestImage(
                    image = avatar,
                    type = RequestImage.Media.ImageType.POSTER,
                ) {
                    toRequestBuilder(context, transformations = listOf(CircleCropTransformation()))
                }

            AsyncImage(
                model = requestBuilder.build(),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun ReviewScorePill(score: Int) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)),
    ) {
        Text(
            text = stringResource(ReviewR.string.label_review_score, score),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ReviewMetaChip(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReviewVoteButton(
    icon: Int,
    label: String,
    selected: Boolean,
    enabled: Boolean,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.heightIn(min = 40.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor.copy(alpha = 0.72f),
                disabledContentColor = contentColor.copy(alpha = 0.72f),
            ),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}

@Composable
private fun ReviewCountPill(
    icon: Int,
    label: String,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun ReviewSummaryText(
    text: String,
    maxLines: Int,
) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = if (maxLines == Int.MAX_VALUE) TextOverflow.Clip else TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun voteContainerColor(
    selected: Boolean,
    selectedContainer: Color,
    defaultContainer: Color,
): Color = if (selected) selectedContainer else defaultContainer

@Composable
private fun voteContentColor(
    selected: Boolean,
    selectedContent: Color,
    defaultContent: Color,
): Color = if (selected) selectedContent else defaultContent

@Composable
private fun voteBorderColor(
    selected: Boolean,
    selectedBorder: Color,
    defaultBorder: Color,
): Color = if (selected) selectedBorder.copy(alpha = 0.3f) else defaultBorder.copy(alpha = 0.4f)

@Composable
private fun Review.authorName(): String =
    user.name
        .toString()
        .trim()
        .ifBlank { stringResource(ReviewR.string.label_review_unknown_author) }

@Composable
private fun Review.browseMetaLabel(): String = voteLabel() ?: timeLabel()

@Composable
private fun Review.voteLabel(): String? =
    ratingAmount
        .takeIf { it > 0 }
        ?.toHumanReadableQuantity(0)
        ?.let { stringResource(ReviewR.string.label_review_votes, it) }

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
            showMediaContext = true,
            summaryMaxLines = 5,
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
            showMediaContext = false,
            summaryMaxLines = 4,
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
                showMediaContext = true,
                summaryMaxLines = 5,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userRating = ReviewRating.DOWN_VOTE),
                showMediaContext = false,
                summaryMaxLines = 4,
                canVote = true,
                isVotePending = false,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(),
                showMediaContext = false,
                summaryMaxLines = 4,
                canVote = true,
                isVotePending = true,
                onOpen = {},
                onVoteRequested = {},
            )
            ReviewBrowseCard(
                review = previewReview(userId = 99L),
                showMediaContext = false,
                summaryMaxLines = 4,
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

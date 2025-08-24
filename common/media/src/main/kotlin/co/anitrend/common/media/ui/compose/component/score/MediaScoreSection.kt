/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.common.media.ui.compose.component.score

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.component.IconScoreContent
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.media.entity.attribute.score.IMediaScore
import co.anitrend.domain.medialist.enums.ScoreFormat

@Composable
fun MediaScoreSection(
    mediaScore: IMediaScore,
    scoreFormat: ScoreFormat,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    val gradient =
        Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
            ),
        )
    val outline = MaterialTheme.colorScheme.outlineVariantOnSurface()

    Surface(
        modifier =
            modifier
                .clip(shape)
                // .background(MaterialTheme.colorScheme.surface, shape = shape)
                .background(gradient, shape),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, outline),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StarBadge(
                tint = MaterialTheme.colorScheme.primary,
                showBorder = true,
            )
            Spacer(Modifier.width(12.dp))
            StackRating(
                label = stringResource(R.string.label_media_score_section_your_rating),
                rating = mediaScore.asFormattedPersonal(scoreFormat),
                showSuffix = false,
            )
            Spacer(Modifier.weight(1f))
            StackRating(
                label = stringResource(R.string.label_media_score_section_community),
                footerSuffix = stringResource(R.string.label_media_score_section_popularity_footer_suffix),
                rating = IMediaRating.Text(score = mediaScore.mean.toString()),
                footer = mediaScore.popularity?.takeIf { it > 0 }?.toHumanReadableQuantity(0),
                showSuffix = true,
            )
        }
    }
}

@Composable
private fun StackRating(
    modifier: Modifier = Modifier,
    footerSuffix: String? = null,
    footer: String? = null,
    rating: IMediaRating?,
    showSuffix: Boolean,
    label: String,
) {
    Column(modifier = modifier) {
        // Always show the label first
        LabelText(text = label)

        when (rating) {
            is IMediaRating.Mood -> {
                IconScoreContent(
                    rating = rating,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp),
                )
            }
            is IMediaRating.Text -> {
                // Normalize zero-like values to null so we render the placeholder
                val normalized: String? = rating.score.takeUnless { it.isZeroLike() }
                ValueOutOf(
                    value = normalized,
                    showSuffix = showSuffix,
                    placeholder = stringResource(R.string.placeholder_media_score_section_rating),
                )
                footer?.let {
                    Text(
                        text = "$it $footerSuffix",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // When rating is null, show placeholder
            else -> {
                ValueOutOf(
                    value = null,
                    showSuffix = showSuffix,
                    placeholder = stringResource(R.string.placeholder_media_score_section_rating),
                )
            }
        }
    }
}

@Composable
private fun LabelText(text: String) =
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

@Composable
private fun ValueOutOf(
    value: String?,
    showSuffix: Boolean,
    placeholder: String,
) {
    val mainSize = 22.sp
    val suffixSize = 16.sp
    val mainColor = MaterialTheme.colorScheme.onSurface
    val hintColor = MaterialTheme.colorScheme.onSurfaceVariant

    val text =
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = mainSize,
                    fontWeight = FontWeight.SemiBold,
                    color = if (value != null) mainColor else hintColor,
                ),
            ) {
                append(value ?: placeholder)
            }
            if (showSuffix) {
                withStyle(SpanStyle(fontSize = suffixSize, color = hintColor)) {
                    append("/100")
                }
            }
        }
    Text(text)
}

/** Left circular icon badge */
@Composable
private fun StarBadge(
    tint: Color,
    showBorder: Boolean,
) {
    val bg = tint.copy(alpha = 0.12f)
    val border = tint.copy(alpha = 0.20f)

    Box(
        modifier =
            Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(bg)
                .then(
                    if (showBorder) {
                        Modifier
                            .border(BorderStroke(1.dp, border), CircleShape)
                    } else {
                        Modifier
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
    }
}

/** Theme-aware outline for soft borders */
@Composable
private fun ColorScheme.outlineVariantOnSurface(): Color =
    // outlineVariant is part of M3. Fall back to outline with opacity if unavailable.
    try {
        outlineVariant
    } catch (_: Throwable) {
        outline.copy(alpha = 0.4f)
    }

/**
 * Returns true if the string represents a numeric zero (e.g., "0", "0.0", "00").
 *
 * This is used to normalize zero-like numerical strings to null so that
 * UI can show a consistent placeholder instead of an unhelpful "0" score.
 */
private fun String?.isZeroLike(): Boolean {
    val trimmed = this?.trim()
    if (trimmed.isNullOrEmpty()) return true // treat empty as zero-like for placeholder purposes
    val numeric = trimmed.toDoubleOrNull()
    return numeric != null && numeric == 0.0
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaScoreSectionPreview(
    @PreviewParameter(MediaScoreSectionPreviewProvider::class) mediaScore: IMediaScore,
) {
    PreviewTheme {
        MediaScoreSection(
            mediaScore = mediaScore,
            scoreFormat = ScoreFormat.POINT_5,
        )
    }
}

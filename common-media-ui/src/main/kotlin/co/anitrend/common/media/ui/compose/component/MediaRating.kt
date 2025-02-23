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
package co.anitrend.common.media.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.media.entity.attribute.score.IMediaScore
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat

@Composable
private fun ScoreContent(
    scoreFormat: ScoreFormat,
    mediaScore: IMediaScore,
) {
    val result = remember(scoreFormat, mediaScore) { mediaScore.asFormatted(scoreFormat) }
    when (result) {
        is IMediaRating.Mood -> IconScoreContent(rating = result)
        is IMediaRating.Text -> TextScoreContent(rating = result)
    }
}

@Composable
private fun IconScoreContent(
    rating: IMediaRating.Mood,
    iconTint: Color = Color.White,
) {
    val iconRes =
        when (rating.sentiment) {
            IMediaRating.Mood.Sentiment.BAD -> R.drawable.ic_face_sad
            IMediaRating.Mood.Sentiment.NEUTRAL -> R.drawable.ic_face_neutral
            IMediaRating.Mood.Sentiment.GOOD -> R.drawable.ic_face_happy
            IMediaRating.Mood.Sentiment.NONE -> R.drawable.ic_face
        }
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(14.dp),
    )
}

@Composable
private fun TextScoreContent(
    rating: IMediaRating.Text,
    textColor: Color = Color.White,
) {
    Text(
        text = rating.score,
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun MediaRating(
    media: IMedia,
    scoreFormat: ScoreFormat,
    tintColor: Color? = null,
    modifier: Modifier = Modifier,
) {
    val mediaList by remember(media) { derivedStateOf { media.mediaList } }
    val hasMediaList by remember(mediaList) { derivedStateOf { mediaList != null } }

    val bubbleColor = tintColor ?: colorResource(id = co.anitrend.core.android.R.color.bubble_color)

    val statusIconRes by remember(media) {
        derivedStateOf {
            if (hasMediaList) {
                when (mediaList?.status) {
                    MediaListStatus.COMPLETED -> R.drawable.ic_completed
                    MediaListStatus.CURRENT -> R.drawable.ic_current
                    MediaListStatus.DROPPED -> R.drawable.ic_dropped
                    MediaListStatus.PAUSED -> R.drawable.ic_paused
                    MediaListStatus.PLANNING -> R.drawable.ic_planning
                    MediaListStatus.REPEATING -> R.drawable.ic_repeat
                    else -> null
                }
            } else {
                null
            }
        }
    }

    Row(
        modifier =
            modifier
                .background(
                    color = if (tintColor == null) bubbleColor else Color.Transparent,
                    shape = RoundedCornerShape(32.dp),
                ).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ScoreContent(
            scoreFormat = scoreFormat,
            mediaScore = media.score,
        )

        statusIconRes?.also {
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }

        mediaList?.privacy?.notes?.also {
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_note),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }

        if (media.isFavourite) {
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_favourite),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaRatingPreview() {
    PreviewTheme {
        val media =
            Media.Core.empty().copy(
                isFavourite = true,
                score =
                    MediaScore(
                        average = 69,
                        mean = 70,
                        personal = null,
                    ),
                mediaList =
                    MediaList.Core.empty().copy(
                        id = 100,
                        status = MediaListStatus.COMPLETED,
                        score = 8.3f,
                        privacy =
                            MediaListPrivacy(
                                isHidden = false,
                                isPrivate = false,
                                notes = "Good..",
                            ),
                    ),
            )

        MediaRating(
            media = media,
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
        )
    }
}

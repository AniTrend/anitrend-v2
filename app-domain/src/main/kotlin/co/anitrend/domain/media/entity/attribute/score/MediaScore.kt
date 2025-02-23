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
package co.anitrend.domain.media.entity.attribute.score

import co.anitrend.domain.medialist.enums.ScoreFormat

data class MediaScore(
    override val mean: Int,
    override val average: Int,
    override val personal: Float? = null,
) : IMediaScore {
    override fun asFormatted(scoreFormat: ScoreFormat): IMediaRating =
        when (scoreFormat) {
            ScoreFormat.POINT_100 ->
                IMediaRating.Text(
                    score = personal?.toInt()?.toString() ?: (mean * 5 / 100).toString(),
                )
            ScoreFormat.POINT_10 ->
                IMediaRating.Text(
                    score = personal?.toInt()?.toString() ?: (mean / 10).toString(),
                )
            ScoreFormat.POINT_5 ->
                IMediaRating.Text(
                    score = personal?.toInt()?.toString() ?: (mean * 5 / 100).toString(),
                )
            ScoreFormat.POINT_10_DECIMAL ->
                IMediaRating.Text(
                    score = personal?.let { "%.1f".format(it) } ?: "%.1f".format(mean / 10f),
                )
            else ->
                if (personal != null) {
                    when (personal) {
                        1f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
                        2f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
                        3f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
                        else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
                    }
                } else {
                    when (average) {
                        in 1..33 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
                        in 34..66 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
                        in 67..100 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
                        else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
                    }
                }
        }

    companion object {
        fun empty() =
            MediaScore(
                mean = 0,
                average = 0,
                personal = null,
            )
    }
}

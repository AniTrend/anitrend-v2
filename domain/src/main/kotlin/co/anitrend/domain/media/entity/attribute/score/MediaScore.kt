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
    override val personal: Float?,
    override val popularity: Int?,
    override val trending: Int?,
) : IMediaScore {
    override fun asFormatted(scoreFormat: ScoreFormat): IMediaRating = asFormattedPersonal(scoreFormat) ?: asFormattedCommunity(scoreFormat)

    override fun asFormattedPersonal(scoreFormat: ScoreFormat): IMediaRating? =
        personal?.let { myScore ->
            when (scoreFormat) {
                ScoreFormat.POINT_3 ->
                    when (myScore) {
                        1f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
                        2f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
                        3f -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
                        else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
                    }
                ScoreFormat.POINT_10_DECIMAL -> IMediaRating.Text(score = "%.1f".format(myScore))
                else -> IMediaRating.Text(score = myScore.toString())
            }
        }

    override fun asFormattedCommunity(scoreFormat: ScoreFormat): IMediaRating =
        when (scoreFormat) {
            ScoreFormat.POINT_100 -> IMediaRating.Text(score = mean.toString())
            ScoreFormat.POINT_10 -> IMediaRating.Text(score = (mean / 10).toString())
            ScoreFormat.POINT_5 -> IMediaRating.Text(score = (mean * 5 / 100).toString())
            ScoreFormat.POINT_10_DECIMAL -> IMediaRating.Text(score = "%.1f".format(mean / 10f))
            else ->
                when (average) {
                    in 1..33 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
                    in 34..66 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
                    in 67..100 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
                    else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
                }
            ScoreFormat.POINT_10_DECIMAL -> IMediaRating.Text(score = "%.1f".format(myScore))
            else -> IMediaRating.Text(score = myScore.toString())
        }
    }

    override fun asFormattedCommunity(scoreFormat: ScoreFormat): IMediaRating = when (scoreFormat) {
        ScoreFormat.POINT_100 -> IMediaRating.Text(score = mean.toString())
        ScoreFormat.POINT_10 -> IMediaRating.Text(score = (mean / 10).toString())
        ScoreFormat.POINT_5 -> IMediaRating.Text(score = (mean * 5 / 100).toString())
        ScoreFormat.POINT_10_DECIMAL -> IMediaRating.Text(score = "%.1f".format(mean / 10f))
        else -> when (average) {
            in 1..33 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.BAD)
            in 34..66 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NEUTRAL)
            in 67..100 -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.GOOD)
            else -> IMediaRating.Mood(IMediaRating.Mood.Sentiment.NONE)
        }
    }

    companion object {
        fun empty() =
            MediaScore(
                mean = 0,
                average = 0,
                personal = null,
                popularity = null,
                trending = null,
            )
    }
}

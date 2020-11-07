/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.common.media.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import co.anitrend.arch.extension.ext.*
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.format
import co.anitrend.domain.common.extension.isValid
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import kotlinx.android.synthetic.main.media_rating_widget.view.*

internal class MediaRatingWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    private fun setFavouriteStatus(isFavourite: Boolean) {
        if (isFavourite)
            mediaFavouriteIndicator?.visible()
        else
            mediaFavouriteIndicator?.gone()
    }

    private fun IMedia.setMediaRatingDynamically() {
        val mediaScoreDefault = (meanScore.toFloat() * 5 / 100f).toString()
        when (scoreFormat) {
            ScoreFormat.POINT_100 -> {
                if (mediaList.isValid())
                   mediaAverageScore?.text = mediaList.score.toInt().toString()
                else
                   mediaAverageScore?.text = mediaScoreDefault
            }
            ScoreFormat.POINT_10 -> {
                if (mediaList.isValid())
                   mediaAverageScore?.text = mediaList.score.toInt().toString()
                else
                   mediaAverageScore?.text = (meanScore / 10f).toString()
            }
            ScoreFormat.POINT_5 -> {
                if (mediaList.isValid())
                   mediaAverageScore?.text = mediaList.score.toInt().toString()
                else
                   mediaAverageScore?.text = mediaScoreDefault
            }
            ScoreFormat.POINT_10_DECIMAL -> {
                if (mediaList.isValid())
                   mediaAverageScore?.text = mediaList.score.format(1)
                else {
                    val scoreFormatted = (meanScore / 10f)
                   mediaAverageScore?.text = scoreFormatted.format(1)
                }
            }
            ScoreFormat.POINT_3 -> {
                val faceDefault = context.getCompatDrawable(R.drawable.ic_face)
                val faceNeutral = context.getCompatDrawable(R.drawable.ic_face_neutral)
                val faceHappy = context.getCompatDrawable(R.drawable.ic_face_happy)
                val faceSad = context.getCompatDrawable(R.drawable.ic_face_sad)
                if (mediaList.isValid())
                   mediaAverageScore?.setCompoundDrawablesWithIntrinsicBounds(
                        when (mediaList.score) {
                        1f -> faceSad
                        2f -> faceNeutral
                        3f -> faceHappy
                        else -> faceDefault
                    }, null, null, null)
                else {
                   mediaAverageScore?.setCompoundDrawablesWithIntrinsicBounds(
                        when(averageScore) {
                            in 1..33 -> faceSad
                            in 34..66 -> faceNeutral
                            in 67..100 -> faceHappy
                            else -> faceDefault
                        }, null, null, null)
                }
            }
            else -> mediaAverageScore?.text = averageScore.toString()
        }
    }

    private fun setRating(media: IMedia) {
        media.setMediaRatingDynamically()
    }

    private fun setListStatus(media: IMedia, isMiniMode: Boolean) {
        if (!media.mediaList.isValid()) {
           mediaListNotesIndicator?.gone()
           mediaListStatusIndicator?.gone()
            return
        }

        if (!isMiniMode) {
            if (!media.mediaList.privacy.notes.isNullOrBlank())
               mediaListNotesIndicator?.visible()
            else
               mediaListNotesIndicator?.gone()
        } else mediaListNotesIndicator?.gone()

       mediaListStatusIndicator?.visible()
        when (media.mediaList.status) {
            MediaListStatus.COMPLETED ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_completed)
            )
            MediaListStatus.CURRENT ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_current)
            )
            MediaListStatus.DROPPED ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_dropped)
            )
            MediaListStatus.PAUSED ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_paused)
            )
            MediaListStatus.PLANNING ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_planning)
            )
            MediaListStatus.REPEATING ->mediaListStatusIndicator?.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_repeat)
            )
            null -> {}
        }
    }

    /**
     * @param media Media
     * @param isMiniMode If the view should display minimalistic info
     */
    fun setupUsingMedia(media: IMedia, isMiniMode: Boolean = false) {
        setFavouriteStatus(media.isFavourite)
        setListStatus(media, isMiniMode)
        setRating(media)
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        context.getLayoutInflater().inflate(R.layout.media_rating_widget, this, true)
    }
}
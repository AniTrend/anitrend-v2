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

package co.anitrend.common.media.ui.widget.rating

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import co.anitrend.arch.extension.ext.*
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.databinding.MediaRatingWidgetBinding
import co.anitrend.core.android.extensions.format
import co.anitrend.core.android.views.FrameLayoutWithBinding
import co.anitrend.data.arch.AniTrendExperimentalFeature
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.extension.isValid
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat

@OptIn(AniTrendExperimentalFeature::class)
internal class MediaRatingWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayoutWithBinding<MediaRatingWidgetBinding>(
    context, attrs, defStyleAttr
), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    private fun setFavouriteStatus(isFavourite: Boolean, @ColorRes tintColor: Int) {
        requireBinding().mediaFavouriteIndicator.setImageDrawable(
            context.getCompatDrawable(R.drawable.ic_favourite, tintColor)
        )
        if (isFavourite)
            requireBinding().mediaFavouriteIndicator.visible()
        else
            requireBinding().mediaFavouriteIndicator.gone()
    }

    private fun IMedia.setMediaRatingDynamically(settings: IUserSettings, @ColorRes tintColor: Int) {
        requireBinding().mediaAverageScore.setTextColor(context.getCompatColor(tintColor))
        val scoreFormat: ScoreFormat = settings.scoreFormat.value
        val mediaScoreDefault = (meanScore.toFloat() * 5 / 100f).toString()
        when (scoreFormat) {
            ScoreFormat.POINT_100 -> {
                if (mediaList.isValid())
                   requireBinding().mediaAverageScore.text = mediaList!!.score.toInt().toString()
                else
                   requireBinding().mediaAverageScore.text = mediaScoreDefault
            }
            ScoreFormat.POINT_10 -> {
                if (mediaList.isValid())
                   requireBinding().mediaAverageScore.text = mediaList!!.score.toInt().toString()
                else
                   requireBinding().mediaAverageScore.text = (meanScore / 10f).toString()
            }
            ScoreFormat.POINT_5 -> {
                if (mediaList.isValid())
                   requireBinding().mediaAverageScore.text = mediaList!!.score.toInt().toString()
                else
                   requireBinding().mediaAverageScore.text = mediaScoreDefault
            }
            ScoreFormat.POINT_10_DECIMAL -> {
                if (mediaList.isValid())
                   requireBinding().mediaAverageScore.text = mediaList!!.score.format(1)
                else {
                    val scoreFormatted = (meanScore / 10f)
                    requireBinding().mediaAverageScore.text = scoreFormatted.format(1)
                }
            }
            ScoreFormat.POINT_3 -> {
                val faceDefault = context.getCompatDrawable(R.drawable.ic_face, tintColor)
                val faceNeutral = context.getCompatDrawable(R.drawable.ic_face_neutral, tintColor)
                val faceHappy = context.getCompatDrawable(R.drawable.ic_face_happy, tintColor)
                val faceSad = context.getCompatDrawable(R.drawable.ic_face_sad, tintColor)
                if (mediaList.isValid())
                   requireBinding().mediaAverageScore.setCompoundDrawablesWithIntrinsicBounds(
                        when (mediaList!!.score) {
                        1f -> faceSad
                        2f -> faceNeutral
                        3f -> faceHappy
                        else -> faceDefault
                    }, null, null, null)
                else {
                   requireBinding().mediaAverageScore.setCompoundDrawablesWithIntrinsicBounds(
                        when(averageScore) {
                            in 1..33 -> faceSad
                            in 34..66 -> faceNeutral
                            in 67..100 -> faceHappy
                            else -> faceDefault
                        }, null, null, null)
                }
            }
            else -> requireBinding().mediaAverageScore.text = averageScore.toString()
        }
    }

    private fun setRating(media: IMedia, @ColorRes tintColor: Int, settings: IUserSettings) {
        media.setMediaRatingDynamically(settings, tintColor)
    }

    private fun setListStatus(media: IMedia, @ColorRes tintColor: Int, isMiniMode: Boolean) {
        if (!media.mediaList.isValid()) {
           requireBinding().mediaListNotesIndicator.gone()
           requireBinding().mediaListStatusIndicator.gone()
            return
        }

        if (!isMiniMode) {
            if (!media.mediaList!!.privacy.notes.isNullOrBlank())
               requireBinding().mediaListNotesIndicator.visible()
            else
               requireBinding().mediaListNotesIndicator.gone()
        } else requireBinding().mediaListNotesIndicator.gone()

       requireBinding().mediaListStatusIndicator.visible()
        when (media.mediaList!!.status) {
            MediaListStatus.COMPLETED -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_completed, tintColor)
            )
            MediaListStatus.CURRENT -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_current, tintColor)
            )
            MediaListStatus.DROPPED -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_dropped, tintColor)
            )
            MediaListStatus.PAUSED -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_paused, tintColor)
            )
            MediaListStatus.PLANNING -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_planning, tintColor)
            )
            MediaListStatus.REPEATING -> requireBinding().mediaListStatusIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_repeat, tintColor)
            )
            null -> {}
        }
    }

    /**
     * @param media Media
     * @param isMiniMode If the view should display minimalistic info
     */
    fun setupUsingMedia(
        media: IMedia,
        settings: IUserSettings,
        isMiniMode: Boolean = false,
        @ColorRes tintColor: Int? = null
    ) {
        val desiredColorTint = tintColor ?: R.color.white_1000
        setFavouriteStatus(media.isFavourite, desiredColorTint)
        setListStatus(media, desiredColorTint, isMiniMode)
        setRating(media, desiredColorTint, settings)
        val background = if (tintColor == null)
            R.drawable.bubble_background
        else R.drawable.widget_background
        requireBinding().root.background =
            context.getCompatDrawable(background)
    }

    override fun createBinding() = MediaRatingWidgetBinding.inflate(
        context.getLayoutInflater(), this, true
    )
}
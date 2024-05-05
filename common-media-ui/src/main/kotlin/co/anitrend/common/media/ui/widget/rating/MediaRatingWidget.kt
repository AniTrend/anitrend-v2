/*
 * Copyright (C) 2020 AniTrend
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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.updateMargins
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.extensions.format
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.extension.isValid
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import com.google.android.material.textview.MaterialTextView

@SuppressLint("SetTextI18n")
internal class MediaRatingWidget
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayoutCompat(
            context,
            attrs,
            defStyleAttr,
        ),
        CustomView {
        private val mediaAverageScore =
            MaterialTextView(context).apply {
                layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).also { param ->
                        param.gravity = Gravity.CENTER_VERTICAL
                    }
                setTextColor(context.getCompatColor(co.anitrend.core.android.R.color.white_1000))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setTypeface(typeface, Typeface.BOLD)
            }

        private val mediaFavouriteIndicator =
            AppCompatImageView(context).apply {
                layoutParams =
                    LayoutParams(14.dp, 14.dp).also { param ->
                        param.gravity = Gravity.CENTER_VERTICAL
                    }
                setImageDrawable(
                    context.getCompatDrawable(R.drawable.ic_favourite),
                )
            }

        private val mediaListStatusIndicator =
            AppCompatImageView(context).apply {
                layoutParams =
                    LayoutParams(14.dp, 14.dp).also { param ->
                        param.gravity = Gravity.CENTER_VERTICAL
                    }
                setImageDrawable(
                    context.getCompatDrawable(R.drawable.ic_paused),
                )
            }

        private val mediaListNotesIndicator =
            AppCompatImageView(context).apply {
                layoutParams =
                    LayoutParams(14.dp, 14.dp).also { param ->
                        param.gravity = Gravity.CENTER_VERTICAL
                    }
                setImageDrawable(
                    context.getCompatDrawable(R.drawable.ic_note),
                )
            }

        init {
            onInit(context, attrs, defStyleAttr)
        }

        private fun setFavouriteStatus(
            isFavourite: Boolean,
            @ColorRes tintColor: Int,
        ) {
            mediaFavouriteIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_favourite, tintColor),
            )
            if (isFavourite) {
                mediaFavouriteIndicator.visible()
            } else {
                mediaFavouriteIndicator.gone()
            }
        }

        private fun IMedia.setMediaRatingDynamically(
            settings: IUserSettings,
            @ColorRes tintColor: Int,
        ) {
            mediaAverageScore.setTextColor(context.getCompatColor(tintColor))
            val scoreFormat: ScoreFormat = settings.scoreFormat.value
            val mediaScoreDefault = (meanScore.toFloat() * 5 / 100f).toString()
            when (scoreFormat) {
                ScoreFormat.POINT_100 -> {
                    if (mediaList.isValid()) {
                        mediaAverageScore.text = requireNotNull(mediaList).score.toInt().toString()
                    } else {
                        mediaAverageScore.text = mediaScoreDefault
                    }
                }
                ScoreFormat.POINT_10 -> {
                    if (mediaList.isValid()) {
                        mediaAverageScore.text = requireNotNull(mediaList).score.toInt().toString()
                    } else {
                        mediaAverageScore.text = (meanScore / 10f).toString()
                    }
                }
                ScoreFormat.POINT_5 -> {
                    if (mediaList.isValid()) {
                        mediaAverageScore.text = requireNotNull(mediaList).score.toInt().toString()
                    } else {
                        mediaAverageScore.text = mediaScoreDefault
                    }
                }
                ScoreFormat.POINT_10_DECIMAL -> {
                    if (mediaList.isValid()) {
                        mediaAverageScore.text = requireNotNull(mediaList).score.format(1)
                    } else {
                        val scoreFormatted = (meanScore / 10f)
                        mediaAverageScore.text = scoreFormatted.format(1)
                    }
                }
                ScoreFormat.POINT_3 -> {
                    val faceDefault = context.getCompatDrawable(R.drawable.ic_face)
                    val faceNeutral = context.getCompatDrawable(R.drawable.ic_face_neutral)
                    val faceHappy = context.getCompatDrawable(R.drawable.ic_face_happy)
                    val faceSad = context.getCompatDrawable(R.drawable.ic_face_sad)
                    if (mediaList.isValid()) {
                        mediaAverageScore.setCompoundDrawablesWithIntrinsicBounds(
                            when (requireNotNull(mediaList).score) {
                                1f -> faceSad
                                2f -> faceNeutral
                                3f -> faceHappy
                                else -> faceDefault
                            },
                            null,
                            null,
                            null,
                        )
                    } else {
                        mediaAverageScore.setCompoundDrawablesWithIntrinsicBounds(
                            when (averageScore) {
                                in 1..33 -> faceSad
                                in 34..66 -> faceNeutral
                                in 67..100 -> faceHappy
                                else -> faceDefault
                            },
                            null,
                            null,
                            null,
                        )
                    }
                }
                else -> mediaAverageScore.text = averageScore.toString()
            }
        }

        private fun setListStatus(
            media: IMedia,
            @ColorRes tintColor: Int,
        ) {
            if (!media.mediaList.isValid()) {
                mediaListNotesIndicator.gone()
                mediaListStatusIndicator.gone()
                return
            }

            mediaListNotesIndicator.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_note, tintColor),
            )

            if (!media.mediaList?.privacy?.notes.isNullOrBlank()) {
                mediaListNotesIndicator.visible()
            } else {
                mediaListNotesIndicator.gone()
            }

            mediaListStatusIndicator.visible()
            when (media.mediaList?.status) {
                MediaListStatus.COMPLETED ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_completed, tintColor),
                    )
                MediaListStatus.CURRENT ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_current, tintColor),
                    )
                MediaListStatus.DROPPED ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_dropped, tintColor),
                    )
                MediaListStatus.PAUSED ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_paused, tintColor),
                    )
                MediaListStatus.PLANNING ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_planning, tintColor),
                    )
                MediaListStatus.REPEATING ->
                    mediaListStatusIndicator.setImageDrawable(
                        context.getCompatDrawable(R.drawable.ic_repeat, tintColor),
                    )
                else -> throw NotImplementedError("`${media.mediaList?.status}` is not catered for")
            }
        }

        /**
         * @param media Media
         */
        fun setupUsingMedia(
            media: IMedia,
            settings: IUserSettings,
            @ColorRes tintColor: Int? = null,
        ) {
            val tint = tintColor ?: co.anitrend.core.android.R.color.white_1000
            setListStatus(media, tint)
            setFavouriteStatus(media.isFavourite, tint)
            media.setMediaRatingDynamically(settings, tint)
            background =
                if (tintColor == null) {
                    context.getCompatDrawable(co.anitrend.core.android.R.drawable.widget_background, co.anitrend.core.android.R.color.bubble_color)
                } else {
                    context.getCompatDrawable(co.anitrend.core.android.R.drawable.widget_background)
                }
        }

        /**
         * Callable in view constructors to perform view inflation and attribute initialization
         *
         * @param context view context
         * @param attrs view attributes if applicable
         * @param styleAttr style attribute if applicable
         */
        override fun onInit(
            context: Context,
            attrs: AttributeSet?,
            styleAttr: Int?,
        ) {
            addView(mediaAverageScore)
            addView(mediaListStatusIndicator)
            addView(mediaListNotesIndicator)
            addView(mediaFavouriteIndicator)

            if (isInEditMode) {
                val media =
                    Media.Core.empty().copy(
                        isFavourite = true,
                        averageScore = 69,
                        meanScore = 70,
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
                setFavouriteStatus(true, co.anitrend.core.android.R.color.white_1000)
                setListStatus(media, co.anitrend.core.android.R.color.white_1000)
                mediaAverageScore.text = media.mediaList?.score?.format(1)
                background = context.getCompatDrawable(co.anitrend.core.android.R.drawable.bubble_background)
            }

            mediaListStatusIndicator.updateMargins(start = 8.dp)
            mediaFavouriteIndicator.updateMargins(start = 8.dp)
            mediaListNotesIndicator.updateMargins(start = 8.dp)
        }
    }

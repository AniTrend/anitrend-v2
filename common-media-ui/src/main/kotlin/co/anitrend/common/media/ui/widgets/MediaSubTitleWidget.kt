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
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.annotation.VisibleForTesting
import androidx.core.text.bold
import co.anitrend.common.media.ui.R
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.media.entities.Media
import co.anitrend.domain.media.entities.MediaAttributes
import co.anitrend.domain.media.enums.MediaType
import com.google.android.material.textview.MaterialTextView

internal class MediaSubTitleWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    private fun buildEpisodeTextUsing(attributes: MediaAttributes, builder: SpannableStringBuilder) {
        val episodes = attributes.episodes
        if (episodes != null) {
            builder.append(" $CHARACTER_SEPARATOR ")
            if (episodes > 1) builder.append(
                "$episodes ${context.getString(R.string.label_episode_plural)}"
            )
            else builder.append(
                "$episodes ${context.getString(R.string.label_episode_singular)}"
            )
        }
    }

    private fun buildChaptersTextUsing(attributes: MediaAttributes, builder: SpannableStringBuilder) {
        val chapters = attributes.chapters
        if (chapters != null) {
            if (chapters > 1) builder.append(
                "$CHARACTER_SEPARATOR $chapters ${context.getString(R.string.label_chapter_plural)}"
            )
            else builder.append(
                "$CHARACTER_SEPARATOR $chapters ${context.getString(R.string.label_chapter_singular)}"
            )
        }
    }

    /**
     * Sets subtitle text in the following format for anime and manga respectively
     * > **2018** • TV • 25 Episodes
     *
     * > **2018** • Novel • 48 Chapters
     */
    fun setMediaSubTitleUsing(media: Media) {
        val builder = SpannableStringBuilder()
        val unknown = context.getString(
            R.string.label_place_holder_to_be_announced
        )
        builder.bold {
            append("${media.startDate?.year ?: unknown}")
        }.append(" $CHARACTER_SEPARATOR ")


        val mediaFormat = media.attributes.format
        if (mediaFormat != null)
            builder.append("$mediaFormat")

        text = when (media.attributes.type) {
            MediaType.ANIME -> {
                buildEpisodeTextUsing(media.attributes, builder)
                builder
            }
            MediaType.MANGA -> {
                buildChaptersTextUsing(media.attributes, builder)
                builder
            }
            null -> {
                context.getString(
                    R.string.label_place_holder_to_be_announced
                )
            }
        }

    }
}
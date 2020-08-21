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
import androidx.core.text.bold
import co.anitrend.common.media.ui.R
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.MediaCategory
import com.google.android.material.textview.MaterialTextView

internal class MediaSubTitleWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    private fun buildTextUsing(category: MediaCategory, builder: SpannableStringBuilder) {
        val unknown = context.getString(R.string.label_place_holder_to_be_announced)
        when(category) {
            is MediaCategory.Anime -> {
                when {
                    category.episodes == 0.toUShort() -> builder.append(unknown)
                    category.episodes > 1u -> builder.append(
                        "${category.episodes} ${context.getString(R.string.label_episode_plural)}"
                    )
                    else -> builder.append(
                        "${category.episodes} ${context.getString(R.string.label_episode_singular)}"
                    )
                }
            }
            is MediaCategory.Manga -> {
                when {
                    category.chapters == 0.toUShort() -> builder.append(unknown)
                    category.chapters > 1u -> builder.append(
                        "$CHARACTER_SEPARATOR ${category.chapters} ${context.getString(R.string.label_chapter_singular)}"
                    )
                    else -> builder.append(
                        "$CHARACTER_SEPARATOR ${category.chapters} ${context.getString(R.string.label_chapter_singular)}"
                    )
                }
            }
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
            if (media.startDate.year == FuzzyDate.UNKNOWN) append(unknown)
            else append("${media.startDate.year}")
        }.append(" $CHARACTER_SEPARATOR ")


        val mediaFormat = media.format
        if (mediaFormat != null)
            builder.append("$mediaFormat")
        buildTextUsing(media.category, builder)
        text = builder
    }
}
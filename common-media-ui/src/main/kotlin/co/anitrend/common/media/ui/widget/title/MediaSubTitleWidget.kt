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

package co.anitrend.common.media.ui.widget.title

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.italic
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaFormat.Companion.isQuantitative
import com.google.android.material.textview.MaterialTextView

internal class MediaSubTitleWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    private fun buildTextUsing(category: Media.Category, builder: SpannableStringBuilder) {
        val unknown = context.getString(R.string.label_place_holder_to_be_announced)
        when(category) {
            is Media.Category.Anime -> {
                when (category.episodes) {
                    0 -> builder.italic { append(unknown) }
                    else -> builder.bold{
                        append(
                            resources.getQuantityString(
                                R.plurals.label_number_of_episodes,
                                category.episodes,
                                category.episodes
                            )
                        )
                    }
                }
            }
            is Media.Category.Manga -> {
                when (category.chapters) {
                    0 -> builder.italic { append(unknown) }
                    else -> builder.bold{
                        append(
                            resources.getQuantityString(
                                R.plurals.label_number_of_chapters,
                                category.chapters,
                                category.chapters
                            )
                        )
                    }
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
    fun setUpSubTitle(media: Media) {
        val color = media.image.color?.asColorInt(context) ?: context.getCompatColor(R.color.primaryTextColor)

        val builder = SpannableStringBuilder()
        val unknown = context.getString(
            R.string.label_place_holder_to_be_announced
        )
        builder.bold {
            color(color) {
                if (media.startDate.isDateNotSet()) append(unknown)
                else append("${media.startDate.year}")
            }
        }
        builder.append(" $CHARACTER_SEPARATOR ")

        val isQuantitative = media.format.isQuantitative()

        if (media.format != null) {
            builder.bold { append(media.format!!.alias) }
            if (isQuantitative)
                builder.append(" $CHARACTER_SEPARATOR ")
        }

        if (isQuantitative)
            buildTextUsing(media.category, builder)
        text = builder
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (isInEditMode) {
            val media: Media = Media.Core.empty().copy(
                image = MediaImage.empty().copy(color = "#e4a15d"),
                startDate = FuzzyDate.empty().copy(2018),
                format = MediaFormat.TV,
                category = Media.Category.Anime.empty().copy(25)
            )
            setUpSubTitle(media)
        }
    }
}
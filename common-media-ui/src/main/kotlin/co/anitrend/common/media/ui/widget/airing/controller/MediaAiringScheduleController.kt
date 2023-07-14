/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.common.media.ui.widget.airing.controller

import android.content.Context
import androidx.annotation.ColorInt
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.HexColor
import co.anitrend.domain.media.entity.Media

internal class MediaAiringScheduleController(private val entity: Media) {

    /**
    * Provides a type of [ColorInt] from the [HexColor] presented by the underlying entity
     *
     * @see asColorInt
    */
    @ColorInt fun getColor(context: Context): Int {
        val colour: HexColor? = entity.image.color
        return colour?.asColorInt(context) ?: context.getCompatColor(co.anitrend.arch.theme.R.color.primaryTextColor)
    }

    /**
     * Provides a non-nullable [AiringSchedule], this is only called after [shouldHideWidget]
     * return false
     *
     * @see shouldHideWidget
     */
    fun getSchedule(): AiringSchedule {
        val anime = entity.category as Media.Category.Anime
        return requireNotNull(anime.schedule)
    }

    /**
     * Checks if the media entity type is an anime or if it has a non-null airing schedule
     */
    fun shouldHideWidget(): Boolean {
        val category = entity.category
        return category !is Media.Category.Anime || category.schedule == null
    }
}

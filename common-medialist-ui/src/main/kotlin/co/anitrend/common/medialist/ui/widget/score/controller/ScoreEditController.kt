/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.common.medialist.ui.widget.score.controller

import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.common.medialist.ui.widget.contract.controller.AbstractEditController
import co.anitrend.common.medialist.ui.widget.score.model.ScoreEditModel
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.ScoreFormat

internal class ScoreEditController(
    private val model: ScoreEditModel,
    private val settings: IUserSettings,
) : AbstractEditController() {
    private val scoreFormat by lazy(UNSAFE) {
        settings.scoreFormat.value
    }

    private val changeFactor by lazy(UNSAFE) {
        when (scoreFormat) {
            ScoreFormat.POINT_10_DECIMAL -> 0.1f
            else -> 1f
        }
    }

    init {
        model.maximum =
            when (scoreFormat) {
                ScoreFormat.POINT_10_DECIMAL,
                ScoreFormat.POINT_10,
                -> 10f
                ScoreFormat.POINT_100 -> 100f
                ScoreFormat.POINT_3 -> 3f
                ScoreFormat.POINT_5 -> 5f
            }
    }

    private fun Float.isWithBounds(): Boolean {
        if (model.maximum < 1f) {
            return this > -0.1f
        }
        return this > -0.1f && this <= model.maximum
    }

    fun incrementScore() {
        val delta = model.current + changeFactor
        if (delta.isWithBounds()) {
            model.current = delta
        }
    }

    fun decrementScore() {
        val delta = model.current - changeFactor
        if (delta.isWithBounds()) {
            model.current = delta
        }
    }

    override fun maximumFormatted(): CharSequence {
        return when (scoreFormat) {
            ScoreFormat.POINT_10_DECIMAL -> "/ %.1f".format(model.maximum)
            else -> "/ %d".format(model.maximum.toInt())
        }
    }

    override fun currentFormatted(): CharSequence {
        return when (scoreFormat) {
            ScoreFormat.POINT_10_DECIMAL -> "%.1f".format(model.current)
            else -> "%d".format(model.current.toInt())
        }
    }

    fun changeScore(score: String) {
        val value = score.toFloatOrNull()
        if (value?.isWithBounds() == true) {
            model.current = value
        }
    }
}

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
package co.anitrend.common.medialist.ui.widget.counter.controller

import co.anitrend.arch.extension.ext.empty
import co.anitrend.common.medialist.ui.widget.contract.controller.AbstractEditController
import co.anitrend.common.medialist.ui.widget.counter.model.CounterEditModel

internal class CounterEditController(
    private val model: CounterEditModel,
) : AbstractEditController() {
    private fun Int.isWithBounds(): Boolean {
        if (model.maximum > 0) {
            return this > -1 && this <= model.maximum
        }
        return this > -1
    }

    fun incrementCount() {
        val delta = model.current + 1
        if (delta.isWithBounds()) {
            model.current = delta
        }
    }

    fun decrementCount() {
        val delta = model.current - 1
        if (delta.isWithBounds()) {
            model.current = delta
        }
    }

    fun changeValue(number: String) {
        val value = number.toIntOrNull()
        if (value?.isWithBounds() == true) {
            model.current = value
        }
    }

    override fun maximumFormatted(): CharSequence {
        return String.empty()
    }

    override fun currentFormatted(): CharSequence {
        return "${model.current}"
    }
}

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

package co.anitrend.core.android.components.sheet.action

import android.view.View
import co.anitrend.core.android.animations.normalize
import co.anitrend.core.android.components.sheet.action.contract.OnSlideAction

class SheetHandleSlideAction(private val handle: View) : OnSlideAction {
    override fun onSlide(sheet: View, slideOffset: Float) {
        val alpha = slideOffset.normalize(
            -1f,
            0f,
            0f,
            1f
        )
        handle.alpha = 1f - alpha
    }
}
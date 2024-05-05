/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.navigation.drawer.action

import androidx.annotation.FloatRange
import co.anitrend.navigation.drawer.component.content.BottomDrawerContent

/**
 * Callback used to run actions when the offset/progress of [BottomDrawerContent]'s account
 * picker sandwich animation changes.
 */
internal interface OnSandwichSlideAction {
    /**
     * Called when the sandwich animation is running, either opening or closing the account picker.
     * [slideOffset] is a value between 0 and 1. 0 represents the closed, default state with the
     * account picker not visible. 1 represents the open state with the account picker visible.
     */
    fun onSlide(
        @FloatRange(
            from = 0.0,
            fromInclusive = true,
            to = 1.0,
            toInclusive = true,
        ) slideOffset: Float,
    )
}

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

package co.anitrend.core.ui.fragment.contract

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Fragment factory to help us construct new fragments
 *
 * @property fragmentTag unique tag that can be used by
 * [androidx.fragment.app.FragmentManager]
 *
 * @param T type of your fragment
 */
interface IFragmentFactory<T: Fragment> {
    val fragmentTag: String

    fun newInstance(bundle: Bundle? = null): T
}
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

package co.anitrend.model

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import co.anitrend.R
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.android.state.IScreenState

internal class ScreenState(screen: FragmentActivity) : IScreenState {
    private val uiRedirection by screen.extra(ARG_KEY_REDIRECT, R.id.navigation_home)

    @IdRes var selectedItem: Int = uiRedirection ?: R.id.navigation_home
    @StringRes var selectedTitle: Int = R.string.navigation_home

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_KEY_SELECTED_ITEM, selectedItem)
        outState.putInt(ARG_KEY_SELECTED_TITLE, selectedTitle)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(ARG_KEY_SELECTED_ITEM))
            selectedItem = savedInstanceState.getInt(ARG_KEY_SELECTED_ITEM)
        if (savedInstanceState.containsKey(ARG_KEY_SELECTED_TITLE))
            selectedTitle = savedInstanceState.getInt(ARG_KEY_SELECTED_TITLE)
    }

    companion object {
        internal const val ARG_KEY_REDIRECT = "ARG_KEY_REDIRECT"

        internal const val ARG_KEY_SELECTED_ITEM = "ARG_KEY_NAVIGATION_STATE"
        internal const val ARG_KEY_SELECTED_TITLE = "ARG_KEY_NAVIGATION_STATE"
    }
}
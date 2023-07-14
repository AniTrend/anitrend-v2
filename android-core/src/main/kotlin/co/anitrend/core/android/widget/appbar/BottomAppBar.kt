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

package co.anitrend.core.android.widget.appbar

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import androidx.core.view.iterator
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.extensions.cascadeMenu
import com.google.android.material.R
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.coroutines.delay
import me.saket.cascade.overrideAllPopupMenus

class BottomAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bottomAppBarStyle
) : BottomAppBar(context, attrs, defStyleAttr), CustomView {

    init {
        onInit(context, attrs, defStyleAttr)
    }

    /**
     * Change visibility of all items for in this menu
     *
     * @param shouldShow Visibility
     * @param filter Predicate for which menu items to hid
     */
    suspend fun toggleVisibility(
        shouldShow: Boolean,
        filter: (MenuItem) -> Boolean
    ) {
        runCatching {
            delay(128)
            menu.iterator().asSequence()
                .filter(filter)
                .forEach { menuItem ->
                    menuItem.isVisible = shouldShow
                }
        }
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        hideOnScroll = true
        isHapticFeedbackEnabled = true
        overrideAllPopupMenus { _, anchor ->
            anchor.cascadeMenu()
        }
        // HideBottomViewOnScrollBehavior
    }
}

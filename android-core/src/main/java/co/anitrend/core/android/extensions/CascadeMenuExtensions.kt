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

package co.anitrend.core.android.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuCompat
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.core.android.R
import co.anitrend.core.android.views.drawable.RoundedDrawable
import me.saket.cascade.CascadePopupMenu
import me.saket.cascade.allChildren

/**
 * Creates a cascade popup menu styler
 */
fun Context.cascadePopupStyler(): CascadePopupMenu.Styler {
    val rippleDrawable = {
        RippleDrawable(
            ColorStateList.valueOf(
                getColorFromAttr(R.attr.colorPrimaryDark)
            ),
            null,
            ColorDrawable(
                getColorFromAttr(R.attr.colorOnBackground)
            )
        )
    }

    return CascadePopupMenu.Styler(
        background = {
            RoundedDrawable(
                color = getColorFromAttr(R.attr.colorPrimaryVariant),
                radius = 8f.dp
            )
        },
        menuTitle = { header ->
            header.titleView.typeface = ResourcesCompat.getFont(
                this,
                R.font.product_sans_regular
            )
            header.setBackground(rippleDrawable())
        },
        menuItem = { item ->
            item.titleView.typeface = ResourcesCompat.getFont(
                this,
                R.font.product_sans_regular
            )
            item.setBackground(rippleDrawable())
            item.setGroupDividerColor(
                getColorFromAttr(R.attr.colorOnSurface)
            )
        }
    )
}

/**
 * Creates a cascade popup menu using [this]
 *
 * @see cascadePopupStyler
 */
fun View.cascadeMenu(): CascadePopupMenu {
    return CascadePopupMenu(
        context = context,
        anchor = this,
        gravity = Gravity.NO_GRAVITY,
        styler = context.cascadePopupStyler()
    )
}

/**
 * Allows the manipulation of internal menu and returns itself
 *
 * @param shouldNavigateBack Applies back navigation on any menu items without an intent
 * @param action Manipulator delegate
 */
fun CascadePopupMenu.onMenu(
    shouldNavigateBack: Boolean = true,
    action: Menu.() -> Unit
): CascadePopupMenu {
    MenuCompat.setGroupDividerEnabled(menu, true)
    menu.action()
    if (shouldNavigateBack)
        menu.allChildren.filter { menuItem ->
            menuItem.intent == null
        }.forEach { menuItem ->
            menuItem.setOnMenuItemClickListener {
                navigateBack()
            }
        }
    return this
}
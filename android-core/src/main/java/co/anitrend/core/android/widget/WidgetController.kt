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

package co.anitrend.core.android.widget

import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.core.android.widget.contract.IWidgetController

/**
 * Abstract contract for widget controllers, the sole responsibility of controller
 * is to delegate view logic outside of the UI driver (e.g. Activity, Fragment, View)
 */
abstract class WidgetController : IWidgetController, SupportLifecycle {

    /**
     * This is called when the view is attached to a window. At this point it
     * has a Surface and will start drawing.
     *
     * @see android.view.View.onDetachedFromWindow
     */
    override fun onAttached() {
        // Optional use-case
    }

    /**
     * This is called when the view is detached from a window. At this point it
     * no longer has a surface for drawing.
     *
     * @see android.view.View.onAttachedToWindow
     */
    override fun onDetached() {
        // Optional use-case
    }
}
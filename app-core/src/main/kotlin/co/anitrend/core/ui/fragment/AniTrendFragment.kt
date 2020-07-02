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

package co.anitrend.core.ui.fragment

import android.content.Context
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.extension.ext.attachComponent
import co.anitrend.arch.extension.ext.detachComponent
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.core.ui.ILifecycleFeature

abstract class AniTrendFragment : SupportFragment(), ILifecycleFeature {

    /**
     * Called when a fragment is first attached to its context.
     * [onCreate] will be called after this.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        featureModuleHelper()?.also {
            attachComponent(it)
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity. This
     * is called after [onDestroy].
     */
    override fun onDetach() {
        featureModuleHelper()?.also {
            detachComponent(it)
        }
        super.onDetach()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): ISupportViewModelState<*>? {
        // Making this optional
        return null
    }
}
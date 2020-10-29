/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.component.screen

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.core.ui.inject
import co.anitrend.core.util.config.contract.IConfigurationUtil
import org.koin.android.ext.android.getKoin
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.ScopeID

/**
 * Abstract application based activity for anitrend, avoids further modification of the
 * support library, any feature additions should be added through extensions
 */
abstract class AnitrendScreen<B : ViewBinding> : SupportActivity(), KoinScopeComponent, IBindingView<B> {

    protected val configurationUtil by inject<IConfigurationUtil>()

    override var binding: B? = null

    override val scope by lazy(UNSAFE) { activityScope() }

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        configurationUtil.onCreate(this)
        lifecycleScope.launchWhenCreated {
            runCatching {
                getKoin().logger.debug("Open activity scope: $scope")
                setupKoinFragmentFactory(scope)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newContext = configurationUtil.attachContext(newBase)
        super.attachBaseContext(newContext)
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        configurationUtil.onResume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
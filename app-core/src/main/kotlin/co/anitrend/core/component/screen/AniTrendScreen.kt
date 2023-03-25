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
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.core.android.settings.helper.config.contract.IConfigurationHelper
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.core.ui.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.component.KoinScopeComponent
import timber.log.Timber

/**
 * Abstract application based activity for anitrend, avoids further modification of the
 * support library, any feature additions should be added through extensions
 *
 * @property configurationHelper Configuration driver for this activity
 * @property scope Dependency injection scope for this activity lifecycle
 */
abstract class AniTrendScreen<B : ViewBinding> : SupportActivity(), KoinScopeComponent, IBindingView<B> {

    protected val configurationHelper by inject<IConfigurationHelper>()

    override val scope by activityRetainedScope()

    override var binding: B? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        runCatching {
            Timber.v("Setting up fragment factory using scope: $scope")
            setupKoinFragmentFactory(scope)
        }.onFailure {
            setupKoinFragmentFactory()
            Timber.v(it, "Reverting to scope-less based fragment factory")
        }
        configurationHelper.onCreate(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        val newContext = configurationHelper.attachContext(newBase)
        super.attachBaseContext(newContext)
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        configurationHelper.onResume(this)
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): AniTrendViewModelState<*>? = null

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
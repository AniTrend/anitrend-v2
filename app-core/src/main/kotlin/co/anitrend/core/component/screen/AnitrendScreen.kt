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
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.savedstate.SavedStateRegistry
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.core.extensions.orEmpty
import co.anitrend.core.ui.inject
import co.anitrend.core.util.config.contract.IConfigurationUtil
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.KoinScopeComponent
import timber.log.Timber

/**
 * Abstract application based activity for anitrend, avoids further modification of the
 * support library, any feature additions should be added through extensions
 */
abstract class AnitrendScreen<B : ViewBinding> : SupportActivity(), KoinScopeComponent, IBindingView<B> {

    protected val savedStateProviderKey by lazy { "${moduleTag}State" }
    protected val savedStateProvider = SavedStateRegistry.SavedStateProvider { intent.extras.orEmpty() }

    protected val configurationUtil by inject<IConfigurationUtil>()

    override val scope by lazy(UNSAFE) { activityScope() }

    override var binding: B? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        configurationUtil.onCreate(this)
        lifecycleScope.launchWhenCreated {
            runCatching {
                getKoin().logger.debug("Open activity scope: $scope")
                setupKoinFragmentFactory(scope)
            }.onFailure {
                setupKoinFragmentFactory()
                Timber.tag(moduleTag).w(it, "Defaulting to scope-less based fragment factory")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedStateRegistry.registerSavedStateProvider(
            savedStateProviderKey,
            savedStateProvider
        )
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
        savedStateRegistry.unregisterSavedStateProvider(
            savedStateProviderKey
        )
        binding = null
    }
}
/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.deeplink.component.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.deeplink.component.compose.DeepLinkScreenContent
import co.anitrend.deeplink.component.presenter.OnBoardingPresenter
import co.anitrend.deeplink.component.presenter.SplashPresenter
import co.anitrend.deeplink.component.viewmodel.DeepLinkViewModel
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.extensions.startActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeepLinkScreen : AniTrendScreen() {
    private val viewModel by viewModel<DeepLinkViewModel>()
    private val splashPresenter by inject<SplashPresenter>()
    private val onBoardingPresenter by inject<OnBoardingPresenter>()

    private fun setupSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition {
            lifecycleScope.launch {
                viewModel.splashState.emit(SplashPresenter.State.RUNNING)
            }
            false
        }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            lifecycleScope.launch {
                viewModel.splashState.emit(SplashPresenter.State.DONE)
            }
            splashScreenView.remove()
        }
    }

    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setupSplashScreen(splashScreen)
        setContent {
            AniTrendTheme3 {
                val navController = rememberNavController()
                DeepLinkScreenContent(
                    viewModel = viewModel,
                    splashPresenter = splashPresenter,
                    onBoardingPresenter = onBoardingPresenter,
                    navigationController = navController,
                    onNavigateTo = {
                        viewModel.intentState?.also(::startActivity) ?: MainRouter.startActivity(this)
                        ActivityCompat.finishAfterTransition(this)
                    },
                )
            }
        }
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel(intent.data)
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel(intent.data)
    }
}

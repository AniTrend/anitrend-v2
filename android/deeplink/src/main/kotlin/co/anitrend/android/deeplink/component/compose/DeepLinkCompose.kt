/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.android.deeplink.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.anitrend.android.deeplink.component.presenter.OnBoardingPresenter
import co.anitrend.android.deeplink.component.presenter.SplashPresenter
import co.anitrend.android.deeplink.component.viewmodel.DeepLinkViewModel
import co.anitrend.navigation.DeepLinkRouter

@Composable
fun DeepLinkScreenContent(
    viewModel: DeepLinkViewModel,
    splashPresenter: SplashPresenter,
    onBoardingPresenter: OnBoardingPresenter,
    navigationController: NavHostController,
    onNavigateTo: () -> Unit,
) {
    val splashState by viewModel.splashState.collectAsStateWithLifecycle()
    Scaffold { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navigationController,
            startDestination = DeepLinkRouter.Destination.SPLASH.name,
        ) {
            composable(
                route = DeepLinkRouter.Destination.SPLASH.name,
                content = {
                    SplashScreenContent(
                        splashState = splashState,
                        splashPresenter = splashPresenter,
                        onSplashFinished = { showOnBoarding ->
                            if (showOnBoarding) {
                                navigationController.navigate(DeepLinkRouter.Destination.ON_BOARDING.name) {
                                    popUpTo(DeepLinkRouter.Destination.SPLASH.name) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                onNavigateTo()
                            }
                        },
                    )
                },
            )
            composable(
                route = DeepLinkRouter.Destination.ON_BOARDING.name,
                content = {
                    OnBoardingScreenContent(
                        onBoardingPresenter = onBoardingPresenter,
                        onBoardingCompleted = {
                            onNavigateTo()
                        },
                    )
                },
            )
        }
    }
}

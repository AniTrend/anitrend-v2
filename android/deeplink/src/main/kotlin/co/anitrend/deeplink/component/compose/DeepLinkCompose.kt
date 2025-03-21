package co.anitrend.deeplink.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.anitrend.deeplink.component.presenter.OnBoardingPresenter
import co.anitrend.deeplink.component.presenter.SplashPresenter
import co.anitrend.deeplink.component.viewmodel.DeepLinkViewModel
import co.anitrend.navigation.DeepLinkRouter

@Composable
fun DeepLinkScreenContent(
    viewModel: DeepLinkViewModel,
    splashPresenter: SplashPresenter,
    onBoardingPresenter: OnBoardingPresenter,
    navigationController: NavHostController,
    onNavigateTo: () -> Unit
) {
    val splashState by viewModel.splashState.collectAsStateWithLifecycle()
    if (splashState != SplashPresenter.State.DONE) return
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
                        }
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

/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.deeplink.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.deeplink.component.presenter.SplashPresenter

@Composable
fun SplashSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(dimensionResource(id = co.anitrend.core.android.R.dimen.size_24dp)),
    )
}

@Composable
fun SplashLines(
    largeMargin: Dp,
    rotation: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(top = largeMargin)
                .rotate(rotation),
    ) {
        // First stripe
        Box(
            modifier =
                Modifier
                    .width(dimensionResource(id = co.anitrend.core.android.R.dimen.series_image_xl))
                    .height(largeMargin)
                    .background(
                        color = colorResource(id = co.anitrend.core.android.R.color.green_A700),
                        shape = RoundedCornerShape(4.dp),
                    ),
        )
        Spacer(modifier = Modifier.height(largeMargin))
        // Second stripe
        Box(
            modifier =
                Modifier
                    .width(dimensionResource(id = co.anitrend.core.android.R.dimen.series_image_lg))
                    .height(largeMargin)
                    .background(
                        color = colorResource(id = co.anitrend.core.android.R.color.blue_A700),
                        shape = RoundedCornerShape(4.dp),
                    ),
        )
        Spacer(modifier = Modifier.height(largeMargin))
        // Third stripe
        Box(
            modifier =
                Modifier
                    .width(dimensionResource(id = co.anitrend.core.android.R.dimen.series_image_md))
                    .height(largeMargin)
                    .background(
                        color = colorResource(id = co.anitrend.core.android.R.color.orange_A700),
                        shape = RoundedCornerShape(4.dp),
                    ),
        )
        Spacer(modifier = Modifier.height(largeMargin))
        // Fourth stripe
        Box(
            modifier =
                Modifier
                    .width(dimensionResource(id = co.anitrend.core.android.R.dimen.series_image_sm))
                    .height(largeMargin)
                    .background(
                        color = colorResource(id = co.anitrend.core.android.R.color.red_A700),
                        shape = RoundedCornerShape(4.dp),
                    ),
        )
    }
}

@Composable
fun SplashLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = co.anitrend.core.R.drawable.ic_anitrend_logo),
        contentDescription = null,
        modifier =
            modifier
                .size(dimensionResource(id = co.anitrend.core.android.R.dimen.series_image_sm))
                .clip(MaterialTheme.shapes.extraLarge),
    )
}

@Composable
fun SplashAppName(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = co.anitrend.deeplink.R.string.splash_label_segment_first),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(id = co.anitrend.deeplink.R.string.splash_label_segment_second),
            fontWeight = FontWeight.Bold,
            color = colorResource(id = co.anitrend.arch.theme.R.color.colorStateBlue),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
fun SplashCredits(
    largeMargin: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = co.anitrend.arch.theme.R.dimen.md_margin)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = co.anitrend.deeplink.R.string.splash_label_powered_by),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = stringResource(id = co.anitrend.deeplink.R.string.splash_label_provider),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = largeMargin),
        )
    }
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    val largeMargin = dimensionResource(id = co.anitrend.arch.theme.R.dimen.lg_margin)
    val rotation = integerResource(id = co.anitrend.deeplink.R.integer.splash_stripe_rotation_factor).toFloat()

    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        // Create constraint references
        val (
            splashSpinnerRef,
            splashLinesRef,
            splashLogoRef,
            splashAppNameRef,
            splashCreditsRef,
        ) = createRefs()

        SplashSpinner(
            modifier =
                Modifier.constrainAs(splashSpinnerRef) {
                    top.linkTo(parent.top, margin = largeMargin)
                    end.linkTo(parent.end, margin = largeMargin)
                },
        )

        SplashLines(
            largeMargin = largeMargin,
            rotation = rotation,
            modifier =
                Modifier.constrainAs(splashLinesRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
        )

        SplashLogo(
            modifier =
                Modifier.constrainAs(splashLogoRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        SplashAppName(
            modifier =
                Modifier.constrainAs(splashAppNameRef) {
                    top.linkTo(splashLogoRef.bottom)
                    bottom.linkTo(splashCreditsRef.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        SplashCredits(
            largeMargin = largeMargin,
            modifier =
                Modifier.constrainAs(splashCreditsRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )
    }
}

@Composable
fun SplashScreenContent(
    splashState: SplashPresenter.State,
    splashPresenter: SplashPresenter,
    onSplashFinished: (Boolean) -> Unit,
) {
    LaunchedEffect(splashState) {
        if (splashState == SplashPresenter.State.RUNNING) return@LaunchedEffect
        val shouldShowOnBoarding = splashPresenter.shouldShowOnBoarding()
        onSplashFinished(shouldShowOnBoarding)
    }
    SplashContent()
}

@AniTrendPreview.Default
@Composable
private fun SplashContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        SplashContent()
    }
}

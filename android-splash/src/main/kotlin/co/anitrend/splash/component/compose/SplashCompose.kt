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
package co.anitrend.splash.component.compose

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
import androidx.compose.material3.Scaffold
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
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import kotlinx.coroutines.CoroutineScope

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
                .clip(RoundedCornerShape(16.dp)),
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
            text = stringResource(id = co.anitrend.splash.R.string.splash_label_segment_first),
            style = AniTrendTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(id = co.anitrend.splash.R.string.splash_label_segment_second),
            style =
                AniTrendTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = co.anitrend.arch.theme.R.color.colorStateBlue),
                ),
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
            text = stringResource(id = co.anitrend.splash.R.string.splash_label_powered_by),
            style = AniTrendTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(id = co.anitrend.splash.R.string.splash_label_provider),
            style = AniTrendTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = largeMargin),
        )
    }
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    val largeMargin = dimensionResource(id = co.anitrend.arch.theme.R.dimen.lg_margin)
    val rotation = integerResource(id = co.anitrend.splash.R.integer.splash_stripe_rotation_factor).toFloat()

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
fun SplashScreenContent(onLoad: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(key1 = Unit, block = onLoad)
    Scaffold { innerPadding ->
        SplashContent(Modifier.padding(innerPadding))
    }
}

@AniTrendPreview.Mobile
@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun SplashContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme) {
        SplashContent()
    }
}

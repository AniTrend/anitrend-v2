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
package co.anitrend.onboarding.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.onboarding.R
import co.anitrend.onboarding.component.model.OnboardingPage

@Composable
fun OnBoardingItem(
    modifier: Modifier = Modifier,
    page: OnboardingPage,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(page.resource),
            contentDescription = null,
            modifier =
                Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Text(
            text = stringResource(page.title),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(page.description),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@AniTrendPreview.Default
private fun OnBoardingItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        OnBoardingItem(
            page =
                OnboardingPage(
                    resource = R.drawable.welcome,
                    background = emptyList(),
                    title = R.string.onboarding_title_welcome,
                    description = R.string.onboarding_desc_welcome,
                ),
        )
    }
}

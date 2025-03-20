package co.anitrend.onboarding.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
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
    page: OnboardingPage
) {
    val background = MaterialTheme.colorScheme.surface
    val brush by remember(page, background) {
        derivedStateOf {
            Brush.linearGradient(
                colors = page.background.plus(background),
                start = Offset.Zero,
                end = Offset.Infinite,
                tileMode = TileMode.Clamp,
            )
        }
    }
    Column(
        modifier = modifier.background(brush).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(page.resource),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .clip(MaterialTheme.shapes.extraLarge),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Text(
            text = stringResource(page.title),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(page.description),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center
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
            page = OnboardingPage(
                resource = R.drawable.welcome,
                background = listOf(Color(0xFFEADDFF), Color(0xFF6750A4)),
                title = R.string.onboarding_title_welcome,
                description = R.string.onboarding_desc_welcome
            ),
        )
    }
}

package co.anitrend.deeplink.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@Composable
private fun DeepLinkContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.then(Modifier.padding(16.dp))) {
        // Show logo?
    }
}

@Composable
fun DeepLinkScreenContent(onBackPress: () -> Unit) {
    DefaultScaffold(onBackPress) { modifier ->
        DeepLinkContent(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
@AniTrendPreview.Mobile
@AniTrendPreview.Light
@AniTrendPreview.Dark
private fun DeepLinkScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean
) {
    PreviewTheme(darkTheme = darkTheme) {
        DeepLinkContent()
    }
}

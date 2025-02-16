package co.anitrend.notification.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme


@Composable
private fun NotificationContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {

    }
}

@Composable
fun NotificationScreenContent(onBackPress: () -> Unit) {
    DefaultScaffold(onBackPress) { modifier ->
        NotificationContent(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}


@AniTrendPreview.Mobile
@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun NotificationComposablePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean
) {
    PreviewTheme(darkTheme = darkTheme) {
        NotificationContent()
    }
}

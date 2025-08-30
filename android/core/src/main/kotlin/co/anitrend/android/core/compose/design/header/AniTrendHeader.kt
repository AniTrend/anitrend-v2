package co.anitrend.android.core.compose.design.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme


@Composable
fun AniTrendHeader(
    state: AniTrendHeaderState,
    modifier: Modifier = Modifier,
) {
    val hideDescription = state.description == null
    val verticalArrangement = if (hideDescription) Arrangement.Top else Arrangement.spacedBy(2.dp)
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
    ) {
        Text(
            text = state.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = state.lineSizeLimit.title,
        )
        if (state.description != null) {
            Text(
                text = state.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = state.lineSizeLimit.description,
            )
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun AniTrendHeaderPreview(
    @PreviewParameter(AniTrendHeaderPreviewProvider::class) state: AniTrendHeaderState,
) {
    PreviewTheme(wrapInSurface = true) {
        AniTrendHeader(
            state = state,
            modifier = Modifier.padding(16.dp),
        )
    }
}


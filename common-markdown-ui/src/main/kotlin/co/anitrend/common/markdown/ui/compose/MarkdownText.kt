package co.anitrend.common.markdown.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.empty
import co.anitrend.common.markdown.ui.widget.MarkdownTextWidget
import co.anitrend.domain.common.entity.contract.ISynopsis

@Composable
fun MarkdownText(
    content: CharSequence?,
    modifier: Modifier = Modifier
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    AndroidView(
        factory = ::MarkdownTextWidget,
        update = { widget ->
            widget.setTextColor(onSurfaceColor)
            widget.setContent(content)
        },
        onReset = { widget ->
            widget.text = String.empty()
        },
        onRelease = MarkdownTextWidget::onViewRecycled,
        modifier = modifier,
    )
}

@Composable
fun MarkdownText(
    synopsis: ISynopsis,
    modifier: Modifier = Modifier
) = MarkdownText(synopsis.description, modifier)

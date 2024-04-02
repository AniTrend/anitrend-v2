package co.anitrend.common.markdown.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.empty
import co.anitrend.common.markdown.ui.widget.MarkdownTextWidget
import co.anitrend.domain.common.entity.contract.ISynopsis

@Composable
fun MarkdownText(
    content: CharSequence?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = ::MarkdownTextWidget,
        update = { widget ->
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

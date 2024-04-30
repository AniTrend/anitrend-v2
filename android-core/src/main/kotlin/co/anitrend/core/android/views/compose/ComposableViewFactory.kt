package co.anitrend.core.android.views.compose

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy

fun composable(
    context: Context,
    defaultWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    defaultHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    compositionStrategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
    content: @Composable () -> Unit = {},
) : ComposeView = ComposeView(context).apply {
    layoutParams = ViewGroup.LayoutParams(defaultWidth, defaultHeight)
    setViewCompositionStrategy(strategy = compositionStrategy)
    setContent(content)
}

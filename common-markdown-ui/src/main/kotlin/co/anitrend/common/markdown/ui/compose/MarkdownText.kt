/*
 * Copyright (C) 2024 AniTrend
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
    modifier: Modifier = Modifier,
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
    modifier: Modifier = Modifier,
) = MarkdownText(synopsis.description, modifier)

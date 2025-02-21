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
package co.anitrend.news.component.screen.compose

import android.text.Spanned
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import co.anitrend.arch.extension.ext.empty
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import com.aghajari.compose.text.AnnotatedText
import com.aghajari.compose.text.asAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun NewsReaderScreen(
    state: Flow<String>,
    transformer: (String) -> Spanned,
    onBackPress: () -> Unit,
    onOpenInWebClick: () -> Unit,
    onShareClick: () -> Unit,
    onUrlClick: (String) -> Unit,
) {
    val data by state.collectAsState(String.empty())

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackPress)
                    IconButton(onClick = onOpenInWebClick) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open in browser",
                        )
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Open in browser",
                        )
                    }
                },
            )
        },
    ) { padding ->
        AnnotatedText(
            modifier = Modifier.padding(padding),
            text = transformer(data).asAnnotatedString(),
            onURLClick = onUrlClick,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun NewsScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme) {
        NewsReaderScreen(
            state = flowOf("Hello world"),
            transformer = {
                buildSpannedString {
                    bold { append("Hello world!") }
                    append(" Why don't you want to be red?")
                }
            },
            onBackPress = {},
            onOpenInWebClick = {},
            onShareClick = {},
            onUrlClick = {},
        )
    }
}

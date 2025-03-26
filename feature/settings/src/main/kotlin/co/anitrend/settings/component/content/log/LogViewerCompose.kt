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
package co.anitrend.settings.component.content.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.component.content.log.state.LogUiState
import co.anitrend.settings.component.content.log.viewmodel.LogViewModel
import org.koin.compose.koinInject

@Composable
fun LogViewerScreen(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = koinInject(),
) {
    val context = LocalContext.current
    val logState by viewModel.logState.collectAsStateWithLifecycle(LogUiState.Loading)

    LaunchedEffect(Unit) {
        viewModel.getLogs(context)
    }

    Column(modifier = modifier) {
        AniTrendHintCard(
            title = "Share logs",
            description = "Logs help us figure out the root cause to some issues. Tap to share",
            icon = Icons.Outlined.Share,
        )
        when (val state = logState) {
            is LogUiState.Error ->
                Box {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            LogUiState.Loading ->
                Box {
                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(48.dp).align(
                                alignment = Alignment.Center,
                            ),
                    )
                }
            is LogUiState.Success ->
                LogViewerContent(
                    state = state,
                )
        }
    }
}

@Composable
private fun LogViewerContent(
    modifier: Modifier = Modifier,
    state: LogUiState.Success,
) {
    SelectionContainer {
        LazyColumn(
            modifier = modifier,
        ) {
            items(
                items = state.logs,
            ) { logItem ->
                Text(
                    text = logItem.message,
                    color = getLogColor(logItem.level),
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun getLogColor(level: LogUiState.LogItem.Level): Color =
    when (level) {
        LogUiState.LogItem.Level.ERROR -> MaterialTheme.colorScheme.error
        LogUiState.LogItem.Level.WARNING -> MaterialTheme.colorScheme.error.copy(alpha = 0.85f)
        LogUiState.LogItem.Level.INFO -> MaterialTheme.colorScheme.primary
        LogUiState.LogItem.Level.DEBUG -> MaterialTheme.colorScheme.secondary
        LogUiState.LogItem.Level.VERBOSE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

@AniTrendPreview.Default
@Composable
private fun LogViewerScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        LogViewerContent(
            state =
                LogUiState.Success(
                    listOf(
                        LogUiState.LogItem(
                            date = "03-07",
                            time = "21:12:01:802",
                            level = LogUiState.LogItem.Level.DEBUG,
                            message =
                                "03-07 21:12:01:802 D/[Koin](2) : | (+) '[Factory: 'co.anitrend.android.navigation.drawer.component" +
                                    ".content.BottomDrawerContent',scope:bottom_nav_drawer,binds:co.anitrend.android.navigation.drawer.component" +
                                    ".content.contract.INavigationDrawer]'",
                        ),
                        LogUiState.LogItem(
                            date = "03-07",
                            time = "21:12:02:123",
                            level = LogUiState.LogItem.Level.ERROR,
                            message = "03-07 21:12:02:123 E/[Network](5) : Error connecting to server",
                        ),
                        LogUiState.LogItem(
                            date = "03-07",
                            time = "21:12:03:456",
                            level = LogUiState.LogItem.Level.INFO,
                            message = "03-07 21:12:03:456 I/[Main](1) : Initialization complete",
                        ),
                        LogUiState.LogItem(
                            date = "03-07",
                            time = "21:12:04:789",
                            level = LogUiState.LogItem.Level.WARNING,
                            message = "03-07 21:12:04:789 W/[Cache](3) : Cache miss for key 'user_123'",
                        ),
                    ),
                ),
        )
    }
}

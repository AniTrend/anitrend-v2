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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.design.cards.AniTrendHintCard
import co.anitrend.core.android.storage.contract.IStorageController
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.core.extensions.stackTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LogViewerScreen(
    modifier: Modifier = Modifier,
    controller: IStorageController = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fileName = "${context.packageName}.log"
    var logs by remember { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            runCatching {
                val directory = controller.getLogsCache(context)
                logs = directory.resolve(fileName).readLines()
            }.stackTrace()
        }
    }
    Column(modifier = modifier) {
        AniTrendHintCard(
            title = "Share logs",
            description = "Logs help us figure out the root cause to some issues. Tap to share",
            icon = Icons.Outlined.Share,
        )
        LogViewerContent(
            logs = logs,
        )
    }
}

@Composable
private fun LogViewerContent(
    modifier: Modifier = Modifier,
    logs: List<String>,
) {
    SelectionContainer {
        LazyColumn(
            modifier = modifier,
        ) {
            items(
                count = logs.size,
                key = { index: Int -> logs[index] },
            ) { index ->
                val log = logs[index]
                val color = getLogColor(log)
                Text(
                    text = log,
                    color = color,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                )
            }
        }
    }
}

/**
 * Extracts the log level from a log line based on the expected format:
 * "03-07 21:12:01:802 D/[Koin](2) : ..."
 */
private fun extractLogLevel(log: String): Char? {
    // Split the log by spaces. Expecting at least three tokens:
    // [date, time, levelWithTag, ...]
    val tokens = log.split(" ")
    if (tokens.size < 3) return null
    return tokens[2].firstOrNull()
}

@Composable
@ReadOnlyComposable
private fun getLogColor(log: String): Color {
    val level = extractLogLevel(log)
    return when (level) {
        'E' -> MaterialTheme.colorScheme.error
        'W' -> MaterialTheme.colorScheme.secondary
        'I' -> MaterialTheme.colorScheme.primary
        'D' -> MaterialTheme.colorScheme.onSurface
        'V' -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@AniTrendPreview.Default
@Composable
private fun LogViewerScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        LogViewerContent(
            logs =
                listOf(
                    "03-07 21:12:01:802 D/[Koin](2) : | (+) '[Factory: 'co.anitrend.navigation.drawer.component.content.BottomDrawerContent',scope:bottom_nav_drawer,binds:co.anitrend.navigation.drawer.component.content.contract.INavigationDrawer]'",
                    "03-07 21:12:02:123 E/[Network](5) : Error connecting to server",
                    "03-07 21:12:03:456 I/[Main](1) : Initialization complete",
                    "03-07 21:12:04:789 W/[Cache](3) : Cache miss for key 'user_123'",
                ),
        )
    }
}

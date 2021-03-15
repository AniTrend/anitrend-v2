/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.media.component.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.common.genre.ui.compose.GenresListComponent
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.media.component.viewmodel.state.MediaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun BottomSheetContentComponent(
    state: MediaState,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    val mediaState by state.model.observeAsState()
    Box(
        Modifier
            .fillMaxWidth()
            .height(128.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Swipe up to expand sheet")
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenresListComponent(genres = mediaState?.genres?.toList().orEmpty())
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                scope.launch { scaffoldState.bottomSheetState.collapse() }
            }
        ) {
            Text("Click to collapse sheet")
        }
    }
}

@Composable
private fun FloatingActionButtonComponent(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    FloatingActionButton(
        onClick = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Snackbar #")
            }
        }
    ) {
        Icon(Icons.Rounded.Favorite, contentDescription = "Localized description")
    }
}

@Composable
private fun BottomSheetComponent(scope: CoroutineScope, state: MediaState) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContentComponent(state, scope, scaffoldState)
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButtonComponent(scope, scaffoldState)
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 128.dp
    ) {

    }
}

@Composable
fun MediaDetailComponent(state: MediaState) {
    AniTrendTheme {
        val scope = rememberCoroutineScope()
        BottomSheetComponent(scope, state)
    }
}
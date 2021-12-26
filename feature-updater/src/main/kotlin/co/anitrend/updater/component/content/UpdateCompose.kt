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

package co.anitrend.updater.component.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.onBackgroundColor
import kotlinx.coroutines.launch

@Preview
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun UpdateCompose() {
    AniTrendTheme {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val selection = remember { mutableStateOf(1) }
        val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
        BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                TopAppBar(
                    title = { Text("Backdrop scaffold") },
                    navigationIcon = {
                        if (scaffoldState.isConcealed) {
                            IconButton(onClick = { scope.launch { scaffoldState.reveal() } }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "Localized description",
                                    tint = context.onBackgroundColor()
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { scaffoldState.conceal() } }) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = "Localized description",
                                    tint = context.onBackgroundColor()
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                // show snackbar as a suspend function
                                scope.launch {
                                    scaffoldState.snackbarHostState
                                        .showSnackbar("Snackbar #")
                                }
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Favorite,
                                contentDescription = "Localized description",
                                tint = context.onBackgroundColor()
                            )
                        }
                    },
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                )
            },
            backLayerContent = {
                LazyColumn {
                    items(5) {
                        ListItem(
                            Modifier.clickable {
                                selection.value = it
                                scope.launch { scaffoldState.conceal() }
                            },
                            text = { Text("Select $it") }
                        )
                    }
                }
            },
            frontLayerContent = {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text("Selection: ${selection.value}")
                }
                LazyColumn(modifier = Modifier.padding(vertical = 32.dp)) {
                    items(8) {
                        ListItem(
                            text = { Text("Item $it") },
                            icon = {
                                Icon(
                                    Icons.TwoTone.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        )
                    }
                }
            }
        )
    }
}
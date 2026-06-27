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
package co.anitrend.app.navigation.nav3

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.navigation.nav3.AboutNavKey
import co.anitrend.navigation.nav3.AiringNavKey
import co.anitrend.navigation.nav3.DiscoverNavKey
import co.anitrend.navigation.nav3.EpisodesNavKey
import co.anitrend.navigation.nav3.ForumsNavKey
import co.anitrend.navigation.nav3.HomeNavKey
import co.anitrend.navigation.nav3.ImageViewerNavKey
import co.anitrend.navigation.nav3.Nav3SpikeHomeKey
import co.anitrend.navigation.nav3.NavigationDispatcher
import co.anitrend.navigation.nav3.NewsNavKey
import co.anitrend.navigation.nav3.ReviewsNavKey
import co.anitrend.navigation.nav3.SettingsNavKey
import co.anitrend.navigation.nav3.SocialNavKey
import co.anitrend.navigation.nav3.SuggestionsNavKey
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Spike: Full Compose app shell demonstrating Nav3 host + drawer integration
 * with all primary navigation destinations.
 */
class MainComposeShellActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AniTrendTheme3 {
                MainComposeShell()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainComposeShell() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val dispatcher = koinInject<NavigationDispatcher>()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    Text("AniTrend", modifier = Modifier.padding(16.dp))

                    DrawerNavItem(
                        icon = Icons.Default.Home,
                        label = "Home",
                        onClick = {
                            dispatcher.navigate(HomeNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Search,
                        label = "Discover",
                        onClick = {
                            dispatcher.navigate(DiscoverNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Notifications,
                        label = "News",
                        onClick = {
                            dispatcher.navigate(NewsNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.List,
                        label = "Social",
                        onClick = {
                            dispatcher.navigate(SocialNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.DateRange,
                        label = "Airing",
                        onClick = {
                            dispatcher.navigate(AiringNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Info,
                        label = "About",
                        onClick = {
                            dispatcher.navigate(AboutNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        onClick = {
                            dispatcher.navigate(SettingsNavKey())
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Image,
                        label = "Image Viewer",
                        onClick = {
                            dispatcher.navigate(ImageViewerNavKey(emptyList(), 0))
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.PlayArrow,
                        label = "Episodes",
                        onClick = {
                            dispatcher.navigate(EpisodesNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Star,
                        label = "Reviews",
                        onClick = {
                            dispatcher.navigate(ReviewsNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Lightbulb,
                        label = "Suggestions",
                        onClick = {
                            dispatcher.navigate(SuggestionsNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )

                    DrawerNavItem(
                        icon = Icons.Default.Forum,
                        label = "Forums",
                        onClick = {
                            dispatcher.navigate(ForumsNavKey)
                            scope.launch { drawerState.close() }
                        },
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AniTrend Nav3 Spike") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                AniTrendNav3Host(startKey = Nav3SpikeHomeKey)
            }
        }
    }
}

@Composable
private fun DrawerNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
        selected = false,
        onClick = onClick,
    )
}

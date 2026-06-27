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
package co.anitrend.news.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.navigation.NewsRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.nav3.NewsNavKey
import co.anitrend.news.component.compose.NewsCompose
import co.anitrend.news.component.content.viewmodel.NewsContentViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal class NewsNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(NewsNavKey::class) { _ ->
            NewsNavContent(onBackPress = ::pop)
        }
    }
}

@Composable
private fun NewsNavContent(onBackPress: () -> Unit) {
    val settings = koinInject<Settings>()
    val viewModel = koinViewModel<NewsContentViewModel>()
    val context = LocalContext.current

    AniTrendTheme3 {
        DefaultScaffold(onBackPress = onBackPress) {
            NewsCompose(
                settings = settings,
                viewModel = viewModel,
                onNewsClick = { param ->
                    NewsRouter.startActivity(
                        context = context,
                        navPayload = param.asNavPayload(),
                    )
                },
            )
        }
    }
}

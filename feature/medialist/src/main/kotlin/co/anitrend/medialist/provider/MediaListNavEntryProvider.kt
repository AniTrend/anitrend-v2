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
package co.anitrend.medialist.provider

import androidx.compose.runtime.Composable
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.medialist.component.compose.MediaListCompose
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import co.anitrend.navigation.nav3.AnimeListNavKey
import co.anitrend.navigation.nav3.MangaListNavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal class MediaListNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(AnimeListNavKey::class) { key ->
            MediaListNavContent(
                mediaType = MediaType.ANIME,
                userId = key.userId,
                onBackPress = ::pop,
            )
        }
        registry.register(MangaListNavKey::class) { key ->
            MediaListNavContent(
                mediaType = MediaType.MANGA,
                userId = key.userId,
                onBackPress = ::pop,
            )
        }
    }
}

@Composable
private fun MediaListNavContent(
    mediaType: MediaType,
    userId: Long,
    onBackPress: () -> Unit,
) {
    val settings = koinInject<Settings>()
    val userViewModel = koinViewModel<UserViewModel>()
    val mediaViewModel = koinViewModel<MediaListViewModel>()

    AniTrendTheme3 {
        MediaListCompose(
            settings = settings,
            userSettings = settings,
            userViewModel = userViewModel,
            mediaViewModel = mediaViewModel,
            onMediaItemClick = { /* TODO: handle media item click for detail navigation */ },
            onBackPress = onBackPress,
        )
    }
}

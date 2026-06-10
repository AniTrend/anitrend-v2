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
package co.anitrend.viewer.provider

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.navigation.nav3.ImageViewerNavKey
import co.anitrend.viewer.component.viewmodel.ImageViewerViewModel
import co.anitrend.viewer.component.screen.ImageViewerContent
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.compose.koinInject

internal class ImageViewerNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(ImageViewerNavKey::class) { key ->
            ImageViewerNavContent(
                key = key,
                onBackPress = ::pop,
            )
        }
    }
}

@Composable
private fun ImageViewerNavContent(
    key: ImageViewerNavKey,
    onBackPress: () -> Unit,
) {
    val viewModel = koinInject<ImageViewerViewModel>()
    var downloadSource by remember { mutableStateOf<String?>(null) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                downloadSource?.let { viewModel.downloadImage(it) }
            } else {
                // Permission denied — silently skip download
            }
            downloadSource = null
        }

    val imageSources =
        key.imageSources
            .map(String::trim)
            .filter(String::isNotBlank)
            .ifEmpty { null }
    val initialIndex =
        key.initialIndex.coerceIn(
            0,
            (imageSources?.lastIndex ?: 0).coerceAtLeast(0),
        )

    if (imageSources.isNullOrEmpty()) {
        onBackPress()
        return
    }

    AniTrendTheme3 {
        ImageViewerContent(
            imageSources = imageSources,
            initialIndex = initialIndex,
            onDownloadClick = { source ->
                downloadSource = source
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            },
        )
    }
}

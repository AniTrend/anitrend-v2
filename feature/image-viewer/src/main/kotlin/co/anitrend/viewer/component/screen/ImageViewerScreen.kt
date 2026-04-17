/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.viewer.component.screen

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import co.anitrend.arch.extension.ext.extra
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.viewer.R
import co.anitrend.viewer.component.viewmodel.ImageViewerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageViewerScreen : AniTrendScreen() {
    private val param by extra<ImageViewerRouter.ImageSourceParam>(
        key = nameOf<ImageViewerRouter.ImageSourceParam>(),
    )

    private val viewModel by viewModel<ImageViewerViewModel>()
    private var pendingDownloadSource: String? = null

    private val permissionResult =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isAllowed: Boolean ->
            if (isAllowed) {
                viewModel.downloadImage(pendingDownloadSource)
            } else {
                Toast
                    .makeText(
                        this,
                        R.string.warning_permission_for_storage_not_granted,
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageSources =
            param
                ?.imageSources
                ?.map(String::trim)
                ?.filter(String::isNotBlank)
                ?.ifEmpty {
                    listOf(param?.imageSrc?.toString().orEmpty())
                }.orEmpty()
        val initialIndex = param?.initialIndex?.coerceIn(0, imageSources.lastIndex.coerceAtLeast(0)) ?: 0

        setContent {
            AniTrendTheme3 {
                ImageViewerContent(
                    imageSources = imageSources,
                    initialIndex = initialIndex,
                    onDownloadClick = { source ->
                        pendingDownloadSource = source
                        permissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    },
                )
            }
        }
    }
}

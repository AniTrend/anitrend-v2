/*
 * Copyright (C) 2026 AniTrend
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

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import co.anitrend.android.core.compose.design.pageindicator.PageIndicator
import co.anitrend.android.core.helpers.image.toCoverImage
import co.anitrend.android.core.helpers.image.using
import co.anitrend.viewer.R
import coil.request.Disposable
import coil.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

@Composable
internal fun ImageViewerContent(
    imageSources: List<String>,
    initialIndex: Int,
    onDownloadClick: (String) -> Unit,
) {
    if (imageSources.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.label_image_viewer_missing_image),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    val pagerState =
        rememberPagerState(
            initialPage = initialIndex.coerceIn(0, imageSources.lastIndex),
            pageCount = { imageSources.size },
        )
    var chromeVisible by remember { mutableStateOf(true) }
    val currentImageSource by remember(imageSources, pagerState) {
        derivedStateOf { imageSources.getOrElse(pagerState.currentPage) { imageSources.first() } }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            ZoomableImagePage(
                imageSource = imageSources[page],
                onToggleChrome = { chromeVisible = !chromeVisible },
            )
        }

        AnimatedVisibility(
            visible = chromeVisible && imageSources.size > 1,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                PageIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    pageIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    currentPageIndicatorColor = MaterialTheme.colorScheme.primary,
                )
            }
        }

        AnimatedVisibility(
            visible = chromeVisible,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
        ) {
            FloatingActionButton(onClick = { onDownloadClick(currentImageSource) }) {
                Icon(
                    imageVector = Icons.Rounded.Download,
                    contentDescription = stringResource(R.string.action_title_download),
                )
            }
        }
    }
}

@Composable
private fun ZoomableImagePage(
    imageSource: String,
    onToggleChrome: () -> Unit,
) {
    val context = LocalContext.current
    var pageState by remember(imageSource) { mutableStateOf(ImagePageState.LOADING) }
    var imageViewRef by remember { mutableStateOf<SubsamplingScaleImageView?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            factory = { viewContext ->
                SubsamplingScaleImageView(viewContext).apply {
                    imageViewRef = this
                    setOnClickListener { onToggleChrome() }
                }
            },
            update = { imageView ->
                imageViewRef = imageView
                imageView.setOnClickListener { onToggleChrome() }
                val currentRequest = imageView.tag as? ImagePageRequest
                if (currentRequest?.source != imageSource) {
                    currentRequest?.dispose()
                    imageView.recycle()
                    pageState = ImagePageState.LOADING

                    val target =
                        object : Target {
                            override fun onStart(placeholder: Drawable?) {
                                pageState = ImagePageState.LOADING
                            }

                            override fun onError(error: Drawable?) {
                                pageState = ImagePageState.ERROR
                            }

                            override fun onSuccess(result: Drawable) {
                                imageView.setImage(ImageSource.bitmap(result.toBitmap()))
                                pageState = ImagePageState.SUCCESS
                            }
                        }
                    imageView.tag = ImagePageRequest(imageSource, target.using(imageSource.toCoverImage(), context))
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        when (pageState) {
            ImagePageState.LOADING -> CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color.White)
            ImagePageState.ERROR -> {
                Text(
                    text = stringResource(R.string.label_image_viewer_load_error),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
            ImagePageState.SUCCESS -> Unit
        }
    }

    DisposableEffect(imageSource) {
        onDispose {
            val imageView = imageViewRef
            val request = imageView?.tag as? ImagePageRequest
            request?.dispose()
            imageView?.setOnClickListener(null)
            imageView?.recycle()
            imageView?.tag = null
        }
    }
}

private data class ImagePageRequest(
    val source: String,
    val disposable: Disposable,
) {
    fun dispose() {
        disposable.dispose()
    }
}

private enum class ImagePageState {
    LOADING,
    SUCCESS,
    ERROR,
}

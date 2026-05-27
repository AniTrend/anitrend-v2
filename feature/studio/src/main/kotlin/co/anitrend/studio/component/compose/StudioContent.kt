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
package co.anitrend.studio.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.entity.StudioDetailData

private sealed interface StudioDetailUiState {
    data object Loading : StudioDetailUiState

    data object Empty : StudioDetailUiState

    data class Error(val message: String) : StudioDetailUiState

    data class Populated(val data: StudioDetailData) : StudioDetailUiState
}

@Composable
internal fun StudioDetailContent(
    state: StudioDetailData?,
    loadState: LoadState?,
    onRetry: () -> Unit,
    onSeeAllMediaClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val uiState = studioDetailUiState(state = state, loadState = loadState)

    when (uiState) {
        is StudioDetailUiState.Populated ->
            StudioPopulatedContent(data = uiState.data, onSeeAllMediaClick = onSeeAllMediaClick, modifier = modifier)
        StudioDetailUiState.Loading -> StudioLoadingState(modifier = modifier)
        StudioDetailUiState.Empty -> StudioInfoState(text = "No studio details available.", modifier = modifier)
        is StudioDetailUiState.Error ->
            StudioErrorState(text = uiState.message, onRetry = onRetry, modifier = modifier)
    }
}

private fun studioDetailUiState(
    state: StudioDetailData?,
    loadState: LoadState?,
): StudioDetailUiState =
    when {
        loadState is LoadState.Error -> {
            val details = loadState.details.message?.takeIf(String::isNotBlank)
            StudioDetailUiState.Error(details ?: "Unable to load studio details.")
        }
        state != null -> StudioDetailUiState.Populated(state)
        loadState !is LoadState.Loading -> StudioDetailUiState.Empty
        else -> StudioDetailUiState.Loading
    }

@Composable
private fun StudioPopulatedContent(
    data: StudioDetailData,
    onSeeAllMediaClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            StudioHeroSection(data = data)
        }

        item {
            MediaSection(
                entries = data.mediaEntries,
                entryCount = data.mediaEntries.size,
                onSeeAllClick = onSeeAllMediaClick,
            )
        }
    }
}

@Composable
private fun StudioHeroSection(
    data: StudioDetailData,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.30f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val studioImage = data.studio.image
            if (studioImage != null) {
                AniTrendImage(
                    image = studioImage,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                )
            } else {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.BrokenImage,
                            contentDescription = "No studio image",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Text(
                text = data.studio.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Favourites: ${data.studio.favourites}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (data.studio.isAnimationStudio) {
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = "Animation studio",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                )
            }

            data.studio.siteUrl?.let { siteUrl ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = siteUrl.replace("https://", "").trimEnd('/'),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { uriHandler.openUri(siteUrl) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaSection(
    entries: List<MediaStudioEntry>,
    entryCount: Int,
    onSeeAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Media",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "$entryCount ${if (entryCount == 1) "entry" else "entries"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (onSeeAllClick != null && entryCount > 0) {
                TextButton(
                    onClick = onSeeAllClick,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            }

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No media found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(entries, key = { it.id }) { entry ->
                        StudioMediaPosterCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioMediaPosterCard(
    entry: MediaStudioEntry,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.width(120.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val image = entry.mediaCoverImage
            if (image != null) {
                AniTrendImage(
                    image = image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f).clip(RoundedCornerShape(10.dp)),
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "No image",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Text(
                text = entry.mediaTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            val details =
                listOfNotNull(
                    entry.mediaFormat?.toDisplayLabel(),
                    entry.mediaStartYear?.toString(),
                ).joinToString(separator = " \u2022 ")
            Text(
                text = details.ifBlank { if (entry.isMain) "Main studio" else "Support studio" },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "Score ${entry.mediaAverageScore?.toString() ?: "-"}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun MediaFormat.toDisplayLabel(): String =
    name.lowercase().split('_').joinToString(" ") { part -> part.replaceFirstChar(Char::titlecaseChar) }

@Composable
private fun StudioLoadingState(modifier: Modifier = Modifier) {
    val placeholderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.30f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = placeholderColor) {}
                    Surface(modifier = Modifier.width(160.dp).height(22.dp), shape = RoundedCornerShape(10.dp), color = placeholderColor) {}
                    Surface(modifier = Modifier.width(110.dp).height(16.dp), shape = RoundedCornerShape(10.dp), color = placeholderColor) {}
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Surface(modifier = Modifier.width(96.dp).height(24.dp), shape = RoundedCornerShape(8.dp), color = placeholderColor) {}

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(3) {
                            Surface(
                                modifier = Modifier.width(120.dp),
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f),
                                        shape = RoundedCornerShape(10.dp),
                                        color = placeholderColor,
                                    ) {}
                                    Surface(modifier = Modifier.fillMaxWidth().height(16.dp), shape = RoundedCornerShape(8.dp), color = placeholderColor) {}
                                    Surface(modifier = Modifier.width(72.dp).height(14.dp), shape = RoundedCornerShape(8.dp), color = placeholderColor) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioErrorState(
    text: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StudioInfoState(
        text = text,
        onRetry = onRetry,
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(28.dp),
            )
        },
    )
}

@Composable
private fun StudioInfoState(
    text: String,
    onRetry: (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.30f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            icon?.invoke()
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = if (icon != null) Modifier.padding(top = 10.dp) else Modifier,
            )
            onRetry?.let {
                Button(onClick = it, modifier = Modifier.padding(top = 12.dp)) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

private val previewPopulatedState =
    StudioDetailData(
        studio =
            Studio.Core(
                favourites = 14_200,
                isFavourite = false,
                isFavouriteBlocked = false,
                name = "Wit Studio",
                image = null,
                isAnimationStudio = true,
                siteUrl = "https://www.witstudio.co.jp/",
                id = 1L,
            ),
        mediaEntries =
            listOf(
                MediaStudioEntry(
                    studio =
                        Studio.Core(
                            favourites = 0,
                            isFavourite = false,
                            isFavouriteBlocked = false,
                            name = "Attack on Titan",
                            image = null,
                            isAnimationStudio = true,
                            siteUrl = null,
                            id = 11L,
                        ),
                    mediaTitle = "Attack on Titan",
                    mediaCoverImage = null,
                    mediaFormat = MediaFormat.TV,
                    mediaStartYear = 2013,
                    mediaAverageScore = 84,
                    isMain = true,
                    networkMatch =
                        MediaStudioEntry.StudioNetworkMatch(
                            networkId = 901L,
                            name = "Attack on Titan",
                            category = "TV",
                            originCountry = "JP",
                            logoPath = null,
                            isPrimary = true,
                            similarity = 0.92f,
                        ),
                    id = 101L,
                ),
                MediaStudioEntry(
                    studio =
                        Studio.Core(
                            favourites = 0,
                            isFavourite = false,
                            isFavouriteBlocked = false,
                            name = "Vinland Saga",
                            image = null,
                            isAnimationStudio = true,
                            siteUrl = null,
                            id = 12L,
                        ),
                    mediaTitle = "Vinland Saga",
                    mediaCoverImage = null,
                    mediaFormat = MediaFormat.TV,
                    mediaStartYear = 2019,
                    mediaAverageScore = 86,
                    isMain = true,
                    networkMatch =
                        MediaStudioEntry.StudioNetworkMatch(
                            networkId = 902L,
                            name = "Vinland Saga",
                            category = "TV",
                            originCountry = "JP",
                            logoPath = null,
                            isPrimary = true,
                            similarity = 0.89f,
                        ),
                    id = 102L,
                ),
            ),
        id = 1L,
    )

private val previewNoMediaState = previewPopulatedState.copy(mediaEntries = emptyList())

@AniTrendPreview.Default
@Composable
private fun StudioContentLoadingPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = null,
            loadState = LoadState.Loading(),
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun StudioContentPopulatedPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = previewPopulatedState,
            loadState = null,
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun StudioContentNoMediaPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = previewNoMediaState,
            loadState = null,
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun StudioContentErrorPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = null,
            loadState = LoadState.Error(details = IllegalStateException("Studio preview failed")),
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun StudioContentMobilePopulatedPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = previewPopulatedState,
            loadState = null,
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun StudioContentMobileNoMediaPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        StudioDetailContent(
            state = previewNoMediaState,
            loadState = null,
            onRetry = {},
            onSeeAllMediaClick = {},
        )
    }
}

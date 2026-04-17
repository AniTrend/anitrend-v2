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
package co.anitrend.media.component.compose.studios

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.MediaStudioItemUiModel
import co.anitrend.media.component.compose.section.StudioBadgeRow
import co.anitrend.media.component.compose.section.StudioImageBadge
import co.anitrend.media.component.compose.section.asStudioParam
import co.anitrend.media.component.compose.section.toMediaStudioUiModels
import co.anitrend.media.component.viewmodel.MediaStudiosViewModel
import co.anitrend.navigation.StudioRouter
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaStudiosRoute(
    mediaId: Long,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    onStudioClick: (StudioRouter.StudioParam) -> Unit,
    onExternalLinkClick: (String) -> Unit,
    viewModel: MediaStudiosViewModel = koinViewModel(),
) {
    val studios by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()
    val studioItems = remember(studios) { studios.orEmpty().toMediaStudioUiModels() }

    LaunchedEffect(mediaId) {
        viewModel(mediaId)
    }

    MediaStudiosScreenContent(
        studios = studioItems,
        loadState = loadState,
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
        onStudioClick = onStudioClick,
        onExternalLinkClick = onExternalLinkClick,
        onRetry = { viewModel(mediaId) },
    )
}

@Composable
private fun MediaStudiosScreenContent(
    studios: List<MediaStudioItemUiModel>,
    loadState: LoadState?,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    onStudioClick: (StudioRouter.StudioParam) -> Unit,
    onExternalLinkClick: (String) -> Unit,
    onRetry: () -> Unit,
) {
    StudiosScreenScaffold(
        title = stringResource(R.string.title_media_studios_screen),
        subtitle = stringResource(R.string.subtitle_media_studios_screen),
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
    ) {
        when {
            studios.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(studios, key = MediaStudioItemUiModel::id) { item ->
                        StudioListRow(
                            item = item,
                            onClick = { onStudioClick(item.asStudioParam()) },
                            onOpenSite = item.siteUrl?.takeIf(String::isNotBlank)?.let { url -> { onExternalLinkClick(url) } },
                        )
                    }
                }
            }

            loadState is LoadState.Loading -> {
                CenteredStudiosState(
                    title = stringResource(R.string.label_media_studios_loading),
                    subtitle = stringResource(R.string.message_media_studios_loading),
                )
            }

            loadState is LoadState.Error -> {
                RetryStudiosState(
                    title = stringResource(R.string.label_media_studios_error_title),
                    onRetry = onRetry,
                )
            }

            else -> {
                CenteredStudiosState(
                    title = stringResource(R.string.label_media_studios_empty_title),
                    subtitle = stringResource(R.string.message_media_studios_empty),
                )
            }
        }
    }
}

@Composable
private fun StudiosScreenScaffold(
    title: String,
    subtitle: String,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    content: @Composable () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                mediaTitle
                    ?.takeIf(String::isNotBlank)
                    ?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Composable
internal fun StudioListRow(
    item: MediaStudioItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenSite: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item.image?.let { image ->
                StudioImageBadge(
                    image = image,
                    name = item.name,
                    modifier = Modifier.size(42.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (item.isMain || item.isAnimationStudio || item.networkCategory != null || item.networkOriginCountry != null) {
                    StudioBadgeRow(item = item)
                }

                item.favourites
                    .takeIf { it > 0 }
                    ?.let {
                        Text(
                            text =
                                stringResource(
                                    R.string.label_media_studios_favourites_value,
                                    it.toHumanReadableQuantity(),
                                ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
            }

            onOpenSite?.let {
                TextButton(
                    onClick = it,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.action_media_studios_external_site),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredStudiosState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RetryStudiosState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}

private val PreviewStudios =
    listOf(
        MediaStudioItemUiModel(
            id = 1L,
            name = "Bones",
            isMain = true,
            isAnimationStudio = true,
            favourites = 16_400,
            siteUrl = "https://www.bones.co.jp",
            image =
                CoverImage(
                    large = "https://image.tmdb.org/t/p/original/example-bones.png",
                    medium = "https://image.tmdb.org/t/p/original/example-bones.png",
                ),
            networkCategory = "Network",
            networkOriginCountry = "JP",
        ),
        MediaStudioItemUiModel(
            id = 2L,
            name = "TOHO animation",
            isMain = false,
            isAnimationStudio = true,
            favourites = 12_100,
            siteUrl = "https://tohoanimation.com",
            image =
                CoverImage(
                    large = "https://image.tmdb.org/t/p/original/example-toho.png",
                    medium = "https://image.tmdb.org/t/p/original/example-toho.png",
                ),
            networkCategory = "Company",
            networkOriginCountry = "JP",
        ),
        MediaStudioItemUiModel(
            id = 3L,
            name = "Shueisha",
            isMain = false,
            isAnimationStudio = false,
            favourites = 8_400,
            siteUrl = null,
            image = null,
            networkCategory = null,
            networkOriginCountry = null,
        ),
    )

@AniTrendPreview.Default
@Composable
private fun StudioListRowPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            StudioListRow(
                item = PreviewStudios.first(),
                onClick = {},
                onOpenSite = {},
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaStudiosScreenContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaStudiosScreenContent(
            studios = PreviewStudios,
            loadState = null,
            mediaTitle = "My Hero Academia Season 3",
            onBackPress = {},
            onStudioClick = {},
            onExternalLinkClick = {},
            onRetry = {},
        )
    }
}

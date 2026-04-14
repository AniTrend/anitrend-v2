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
package co.anitrend.media.component.compose.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.media.R
import co.anitrend.navigation.StudioRouter

private const val FEATURED_STUDIO_SUPPORTING_LIMIT = 3
private const val FALLBACK_STUDIO_PREVIEW_LIMIT = 4

internal data class MediaStudioItemUiModel(
    val id: Long,
    val name: String,
    val isMain: Boolean,
    val isAnimationStudio: Boolean,
    val favourites: Int,
    val siteUrl: String?,
    val image: CoverImage?,
    val networkCategory: String?,
    val networkOriginCountry: String?,
)

internal data class MediaStudiosPreviewUiState(
    val featuredStudio: MediaStudioItemUiModel?,
    val supportingStudios: List<MediaStudioItemUiModel>,
    val totalCount: Int,
)

private val MediaStudioUiComparator =
    compareByDescending<MediaStudioItemUiModel> { it.isMain }
        .thenByDescending { it.isAnimationStudio }
        .thenByDescending { it.favourites }
        .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name }

internal fun List<MediaStudioEntry>.toMediaStudioUiModels(): List<MediaStudioItemUiModel> =
    map { entry ->
        MediaStudioItemUiModel(
            id = entry.studio.id,
            name = entry.studio.name,
            isMain = entry.isMain,
            isAnimationStudio = entry.studio.isAnimationStudio,
            favourites = entry.studio.favourites,
            siteUrl = entry.studio.siteUrl,
            image = entry.studio.image,
            networkCategory = entry.networkMatch?.category?.asStudioChipLabel(),
            networkOriginCountry = entry.networkMatch?.originCountry?.asCountryChipLabel(),
        )
    }.sortedWith(MediaStudioUiComparator)

private fun String.asStudioChipLabel(): String? =
    trim()
        .takeIf(String::isNotBlank)
        ?.replaceFirstChar { character -> character.titlecase() }

private fun String.asCountryChipLabel(): String? =
    trim().takeIf(String::isNotBlank)?.uppercase()

internal fun List<MediaStudioItemUiModel>.toMediaStudiosPreviewUiState(): MediaStudiosPreviewUiState {
    val featuredStudio = firstOrNull(MediaStudioItemUiModel::isMain)
    val supportingStudios =
        if (featuredStudio != null) {
            filterNot { it.id == featuredStudio.id }.take(FEATURED_STUDIO_SUPPORTING_LIMIT)
        } else {
            take(FALLBACK_STUDIO_PREVIEW_LIMIT)
        }

    return MediaStudiosPreviewUiState(
        featuredStudio = featuredStudio,
        supportingStudios = supportingStudios,
        totalCount = size,
    )
}

internal fun MediaStudioItemUiModel.asStudioParam(): StudioRouter.StudioParam =
    StudioRouter.StudioParam(
        id = id,
        name = name,
    )

@Composable
internal fun MediaStudiosPreviewSection(
    studios: List<MediaStudioEntry>?,
    studiosLoadState: LoadState?,
    onStudioClick: (StudioRouter.StudioParam) -> Unit,
    onSeeAllClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val studioItems = remember(studios) { studios.orEmpty().toMediaStudioUiModels() }
    val previewState = remember(studioItems) { studioItems.toMediaStudiosPreviewUiState() }

    MediaHubSection(
        title = stringResource(R.string.title_media_studios_section),
        subtitle = stringResource(R.string.subtitle_media_studios_section),
        trailingActionLabel =
            previewState
                .takeIf { it.totalCount > 1 }
                ?.let { stringResource(R.string.action_media_studios_section_see_all) },
        onTrailingAction = previewState.takeIf { it.totalCount > 1 }?.let { { onSeeAllClick() } },
        modifier = modifier,
    ) {
        when {
            studioItems.isNotEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    previewState.featuredStudio?.let { item ->
                        StudioFeaturedCard(
                            item = item,
                            onClick = { onStudioClick(item.asStudioParam()) },
                        )
                    }

                    previewState.supportingStudios.forEach { item ->
                        StudioCompactRow(
                            item = item,
                            onClick = { onStudioClick(item.asStudioParam()) },
                        )
                    }
                }
            }

            studiosLoadState is LoadState.Loading || (studios == null && studiosLoadState !is LoadState.Error) -> {
                LoadingSkeletonContributorRows()
            }

            studiosLoadState is LoadState.Error -> {
                StudioRetryState(
                    title = stringResource(R.string.label_media_studios_error_title),
                    onRetry = onRetry,
                )
            }

            else -> {
                EmptyStateBlock(
                    title = stringResource(R.string.label_media_studios_empty_title),
                    message = stringResource(R.string.message_media_studios_empty),
                )
            }
        }
    }
}

@Composable
internal fun StudioFeaturedCard(
    item: MediaStudioItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item.image?.let { image ->
                StudioImageBadge(
                    image = image,
                    name = item.name,
                    modifier = Modifier.size(56.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                StudioBadgeRow(item = item)
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
        }
    }
}

@Composable
internal fun StudioCompactRow(
    item: MediaStudioItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        shape = RoundedCornerShape(18.dp),
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
                verticalArrangement = Arrangement.spacedBy(6.dp),
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
            }

            trailingContent?.invoke()
        }
    }
}

@Composable
internal fun StudioBadgeRow(
    item: MediaStudioItemUiModel,
    modifier: Modifier = Modifier,
) {
    val chips =
        buildList {
            if (item.isMain) {
                add(stringResource(R.string.label_media_production_studio_main_badge))
            }
            if (item.isAnimationStudio) {
                add(stringResource(R.string.label_media_studios_animation_badge))
            }
            item.networkCategory?.let(::add)
            item.networkOriginCountry?.let(::add)
        }

    if (chips.isEmpty()) {
        return
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        chips.forEach { label ->
            StudioLabelChip(label = label)
        }
    }
}

@Composable
internal fun StudioImageBadge(
    image: CoverImage,
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
    ) {
        AniTrendImage(
            image = image,
            imageType = RequestImage.Media.ImageType.POSTER,
            contentDescription = name,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun StudioLabelChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(999.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun StudioRetryState(
    title: String,
    onRetry: () -> Unit,
) {
    MediaHubSectionRetryState(
        title = title,
        onRetry = onRetry,
    )
}

private val PreviewStudios =
    listOf(
        previewStudio(id = 1L, name = "Bones", isMain = true, isAnimationStudio = true, favourites = 16_400),
        previewStudio(id = 2L, name = "TOHO animation", isAnimationStudio = true, favourites = 12_100),
        previewStudio(id = 3L, name = "Shueisha", favourites = 8_400, siteUrl = "https://www.shueisha.co.jp"),
        previewStudio(id = 4L, name = "Dentsu", favourites = 6_200),
    )

private val PreviewStudiosNoMain =
    listOf(
        previewStudio(id = 11L, name = "TMS Entertainment", isAnimationStudio = true, favourites = 10_300),
        previewStudio(id = 12L, name = "Kodansha", favourites = 7_200),
        previewStudio(id = 13L, name = "Aniplex", favourites = 13_500),
        previewStudio(id = 14L, name = "CloverWorks", isAnimationStudio = true, favourites = 14_000),
    )

private fun previewStudio(
    id: Long,
    name: String,
    isMain: Boolean = false,
    isAnimationStudio: Boolean = false,
    favourites: Int = 0,
    siteUrl: String? = null,
    image: CoverImage? = null,
    networkMatch: MediaStudioEntry.StudioNetworkMatch? = null,
) = MediaStudioEntry(
    studio =
        Studio.Core(
            favourites = favourites,
            isFavourite = false,
            isFavouriteBlocked = false,
            name = name,
            image = image,
            isAnimationStudio = isAnimationStudio,
            siteUrl = siteUrl,
            id = id,
        ),
    isMain = isMain,
    networkMatch = networkMatch,
    id = id,
)

@AniTrendPreview.Default
@Composable
private fun MediaStudiosPreviewSectionPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            MediaStudiosPreviewSection(
                studios = PreviewStudios,
                studiosLoadState = null,
                onStudioClick = {},
                onSeeAllClick = {},
                onRetry = {},
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaStudiosPreviewSectionNoMainPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            MediaStudiosPreviewSection(
                studios = PreviewStudiosNoMain,
                studiosLoadState = null,
                onStudioClick = {},
                onSeeAllClick = {},
                onRetry = {},
            )
        }
    }
}

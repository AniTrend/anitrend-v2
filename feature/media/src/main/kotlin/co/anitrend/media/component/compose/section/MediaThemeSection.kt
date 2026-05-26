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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.theme.ThemePlaybackController
import co.anitrend.media.component.compose.section.theme.ThemePlaybackEngine
import co.anitrend.media.component.compose.section.theme.ThemePlaybackRequest
import co.anitrend.media.component.compose.section.theme.ThemePlaybackUiState

private const val THEME_PREVIEW_COUNT = 2
private const val PREVIEW_LINE_LIMIT = 2

internal fun MediaTheme.hasAudioAsset(): Boolean = !audio.isNullOrBlank()

internal fun MediaTheme.hasVideoAsset(): Boolean = video.isNotBlank()

internal fun MediaTheme.metaTypeLabel(): String? =
    meta
        ?.type
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?.uppercase()

internal fun MediaTheme.metaVersionLabel(): String? =
    meta
        ?.version
        ?.takeIf { it > 1 }
        ?.let { version -> "v$version" }

private fun MediaTheme.typeSortRank(): Int =
    when (metaTypeLabel()) {
        "OP" -> 0
        "ED" -> 1
        else -> 2
    }

private fun MediaTheme.versionSortValue(): Int = meta?.version ?: 0

internal fun List<MediaTheme>.sortedForDisplay(): List<MediaTheme> =
    sortedWith(
        compareBy<MediaTheme>(
            { it.typeSortRank() },
            { it.meta?.number ?: Int.MAX_VALUE },
            { it.versionSortValue() },
            { it.name.lowercase() },
        ),
    )

internal fun MediaTheme.Preview.mediaTagTokens(): List<String> =
    buildList {
        resolution?.takeIf { it > 0 }?.let { add("${it}P") }
        source?.takeIf(String::isNotBlank)?.let { add(it.uppercase()) }
        addAll(tags)
    }

internal fun MediaTheme.Variant.variantLabel(): String =
    if (version > 0) {
        "v$version"
    } else {
        "-"
    }

internal fun MediaTheme.Variant.previewSummaryText(): String? =
    previews
        .firstOrNull()
        ?.mediaTagTokens()
        ?.takeIf { it.isNotEmpty() }
        ?.joinToString(" ")

internal fun MediaTheme.metaBadgeLabel(): String? {
    val themeMeta = meta ?: return null
    val typeLabel = metaTypeLabel()
    val versionLabel = metaVersionLabel()

    return buildList {
        typeLabel?.let {
            add(it)
        }
        themeMeta.number.takeIf { it > 0 }?.let {
            add(it.toString())
        }
        versionLabel?.let(::add)
    }.takeIf { it.isNotEmpty() }?.joinToString(" ")
}

internal interface ThemePreviewSelectionState {
    val variant: MediaTheme.Variant
    val preview: MediaTheme.Preview
    val previewKey: String
}

private data class ThemePreviewSelection(
    override val variant: MediaTheme.Variant,
    override val preview: MediaTheme.Preview,
    override val previewKey: String,
) : ThemePreviewSelectionState

private object SheetThemePlaybackEngine : ThemePlaybackEngine {
    override fun setSource(url: String) = Unit

    override fun play() = Unit

    override fun pause() = Unit

    override fun seekTo(positionMs: Long) = Unit

    override fun release() = Unit
}

private fun MediaTheme.Preview.mediaIdentity(): String = video.takeIf(String::isNotBlank) ?: audio.orEmpty()

internal fun MediaTheme.heroPreviewSelections(): List<ThemePreviewSelectionState> =
    variants.mapNotNull { variant ->
        variant.previews.firstOrNull { !it.audio.isNullOrBlank() }?.let { preview ->
            ThemePreviewSelection(
                variant = variant,
                preview = preview,
                previewKey = "$themeId:v${variant.version}:${preview.mediaIdentity()}",
            )
        }
    }.ifEmpty {
        audio
            ?.takeIf(String::isNotBlank)
            ?.let { audioUrl ->
                val fallbackVariant = MediaTheme.Variant(version = meta?.version ?: 0, episodes = null, previews = emptyList())
                val fallbackPreview = MediaTheme.Preview(video = video, audio = audioUrl, resolution = null, source = null)
                listOf(
                    ThemePreviewSelection(
                        variant = fallbackVariant,
                        preview = fallbackPreview,
                        previewKey = "$themeId:v${fallbackVariant.version}:${fallbackPreview.mediaIdentity()}",
                    ),
                )
            }.orEmpty()
    }

internal fun MediaTheme.preferredPreviewSelection(): ThemePreviewSelectionState? = heroPreviewSelections().firstOrNull()

@StringRes
internal fun MediaTheme.availabilitySummaryResId(): Int =
    when {
        hasAudioAsset() && hasVideoAsset() -> R.string.label_media_theme_section_audio_video_available
        hasAudioAsset() -> R.string.label_media_theme_section_audio_available
        hasVideoAsset() -> R.string.label_media_theme_section_video_available
        else -> R.string.label_media_theme_section_details_only
    }

@Composable
private fun MediaTheme.availabilitySummary(): String =
    stringResource(availabilitySummaryResId())

@Composable
private fun ThemeBadge(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun MediaThemeItem(
    theme: MediaTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val metadataLabel = theme.metaBadgeLabel()
    val availability = theme.availabilitySummary()

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.42f)),
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.MusicNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = theme.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    metadataLabel?.let {
                        ThemeBadge(label = it)
                    }
                    Text(
                        text = availability,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ThemeVariantRow(
    variant: MediaTheme.Variant,
    selection: ThemePreviewSelectionState?,
    isSelected: Boolean,
    isActive: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val previewSummary = selection?.preview?.mediaTagTokens()?.takeIf { it.isNotEmpty() }?.joinToString(" ") ?: variant.previewSummaryText()

    Surface(
        color = if (isSelected || isActive) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (isSelected || isActive) 0.7f else 0.35f)),
        modifier =
            modifier
                .fillMaxWidth()
                .let { base ->
                    if (onClick != null) {
                        base.clickable(onClick = onClick)
                    } else {
                        base
                    }
                },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ThemeBadge(label = variant.variantLabel())
                variant.episodes
                    ?.takeIf(String::isNotBlank)
                    ?.let { episodes ->
                        Text(
                            text = stringResource(R.string.label_media_theme_sheet_episodes_value, episodes),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
            }

            previewSummary?.let { summary ->
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = PREVIEW_LINE_LIMIT,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ThemeIdentityBlock(
    theme: MediaTheme,
    selection: ThemePreviewSelectionState?,
    modifier: Modifier = Modifier,
) {
    val supportingLine =
        buildString {
            theme.metaBadgeLabel()?.let(::append)
            selection?.variant?.episodes
                ?.takeIf(String::isNotBlank)
                ?.let { episodes ->
                    if (isNotEmpty()) {
                        append(" • ")
                    }
                    append(stringResource(R.string.label_media_theme_sheet_episodes_value, episodes))
                }
        }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = theme.name,
            style = MaterialTheme.typography.titleLarge,
        )

        if (supportingLine.isNotBlank()) {
            Text(
                text = supportingLine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ThemeHeroPreviewCard(
    selections: List<ThemePreviewSelectionState>,
    selection: ThemePreviewSelectionState?,
    playbackState: ThemePlaybackUiState,
    onSelectionClick: (ThemePreviewSelectionState) -> Unit,
    onPlayPauseClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val previewSummary = selection?.preview?.mediaTagTokens()?.takeIf { it.isNotEmpty() }?.joinToString(" ")
    val isActiveSelection = selection?.previewKey == playbackState.activePreviewKey
    val canPlay = selection?.preview?.audio?.isNotBlank() == true

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.48f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (selections.isNotEmpty()) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    selections.forEach { item ->
                        ThemeBadge(
                            label = item.variant.variantLabel(),
                            modifier = Modifier.clickable { onSelectionClick(item) },
                            containerColor =
                                if (item.previewKey == selection?.previewKey) {
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.58f)
                                } else {
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.72f)
                                },
                            contentColor =
                                if (item.previewKey == selection?.previewKey) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = selection?.variant?.variantLabel() ?: stringResource(R.string.label_media_theme_sheet_not_available),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    selection?.variant?.episodes
                        ?.takeIf(String::isNotBlank)
                        ?.let { episodes ->
                            Text(
                                text = stringResource(R.string.label_media_theme_sheet_episodes_value, episodes),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                }

                FilledTonalButton(
                    onClick = { onPlayPauseClick?.invoke() },
                    enabled = canPlay,
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(
                        imageVector = if (isActiveSelection && playbackState.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text =
                            stringResource(
                                if (canPlay) {
                                    R.string.label_media_theme_sheet_available
                                } else {
                                    R.string.label_media_theme_sheet_not_available
                                },
                            ),
                    )
                }
            }

            previewSummary?.let { summary ->
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ThemeBadge(
                    label =
                        stringResource(
                            if (canPlay) {
                                R.string.label_media_theme_section_audio_available
                            } else {
                                R.string.label_media_theme_sheet_not_available
                            },
                        ),
                )
                if (isActiveSelection && playbackState.isPlaying) {
                    ThemeBadge(
                        label = stringResource(R.string.label_media_theme_sheet_available),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.58f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaThemeDetailSheet(
    theme: MediaTheme,
    onDismiss: () -> Unit,
) {
    val previewSelections = remember(theme) { theme.heroPreviewSelections() }
    var selectedPreviewKey by remember(theme) { mutableStateOf(theme.preferredPreviewSelection()?.previewKey) }
    val selectedSelection =
        remember(previewSelections, selectedPreviewKey) {
            previewSelections.firstOrNull { it.previewKey == selectedPreviewKey } ?: previewSelections.firstOrNull()
        }
    val controller = remember { ThemePlaybackController(SheetThemePlaybackEngine) }
    val playbackState by controller.uiState.collectAsStateWithLifecycle()
    val hasAudio = previewSelections.any { !it.preview.audio.isNullOrBlank() }
    val hasVideo = theme.hasVideoAsset()
    val metadataLabel = theme.metaBadgeLabel()

    DisposableEffect(controller) {
        onDispose {
            controller.release()
        }
    }

    ListBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            ThemeHeroPreviewCard(
                selections = previewSelections,
                selection = selectedSelection,
                playbackState = playbackState,
                onSelectionClick = { selection ->
                    selectedPreviewKey = selection.previewKey
                    controller.select(
                        request =
                            ThemePlaybackRequest(
                                previewKey = selection.previewKey,
                                audioUrl = selection.preview.audio.orEmpty(),
                                title = theme.name,
                            ),
                        playWhenSelected = playbackState.isPlaying,
                    )
                },
                onPlayPauseClick =
                    selectedSelection
                        ?.takeIf { !it.preview.audio.isNullOrBlank() }
                        ?.let { selection ->
                            {
                                controller.toggle(
                                    ThemePlaybackRequest(
                                        previewKey = selection.previewKey,
                                        audioUrl = selection.preview.audio.orEmpty(),
                                        title = theme.name,
                                    ),
                                )
                            }
                        },
            )

            ThemeIdentityBlock(
                theme = theme,
                selection = selectedSelection,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                metadataLabel?.let {
                    ThemeBadge(
                        label = it,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.48f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
                if (hasAudio) {
                    ThemeBadge(label = stringResource(R.string.label_media_theme_section_audio_available))
                }
                if (hasVideo) {
                    ThemeBadge(label = stringResource(R.string.label_media_theme_section_video_available))
                }
                selectedSelection?.preview?.mediaTagTokens()?.takeIf { it.isNotEmpty() }?.let { tokens ->
                    ThemeBadge(label = tokens.joinToString(" "))
                }
            }

            if (theme.variants.isNotEmpty()) {
                HorizontalDivider()

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.label_media_theme_sheet_versions),
                        style = MaterialTheme.typography.titleSmall,
                    )

                    theme.variants.forEachIndexed { index, variant ->
                        val variantSelection = previewSelections.firstOrNull { it.variant == variant }

                        if (index > 0) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }

                        ThemeVariantRow(
                            variant = variant,
                            selection = variantSelection,
                            isSelected = variantSelection?.previewKey == selectedSelection?.previewKey,
                            isActive = variantSelection?.previewKey == playbackState.activePreviewKey,
                            onClick =
                                variantSelection?.let { selection ->
                                    {
                                        selectedPreviewKey = selection.previewKey
                                        controller.select(
                                            request =
                                                ThemePlaybackRequest(
                                                    previewKey = selection.previewKey,
                                                    audioUrl = selection.preview.audio.orEmpty(),
                                                    title = theme.name,
                                                ),
                                            playWhenSelected = playbackState.isPlaying,
                                        )
                                    }
                                },
                        )
                    }
                }
            }

            if (!hasAudio && !hasVideo) {
                Text(
                    text = stringResource(R.string.label_media_theme_sheet_no_preview_assets),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Composable
internal fun MediaThemePreviewBlock(
    themes: List<MediaTheme>,
    modifier: Modifier = Modifier,
) {
    var selectedTheme by remember(themes) { mutableStateOf<MediaTheme?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        themes.forEach { theme ->
            MediaThemeItem(
                theme = theme,
                onClick = { selectedTheme = theme },
            )
        }
    }

    selectedTheme?.let { theme ->
        MediaThemeDetailSheet(
            theme = theme,
            onDismiss = { selectedTheme = null },
        )
    }
}

@Composable
fun MediaThemeSection(
    themes: List<MediaTheme>,
    modifier: Modifier = Modifier,
    collapsedCount: Int = THEME_PREVIEW_COUNT,
) {
    val orderedThemes = remember(themes) { themes.sortedForDisplay() }

    if (orderedThemes.isEmpty()) {
        return
    }

    var showAll by rememberSaveable(orderedThemes.size) { mutableStateOf(false) }
    val canExpand = orderedThemes.size > collapsedCount
    val visibleThemes = if (canExpand && !showAll) orderedThemes.take(collapsedCount) else orderedThemes

    MediaHubSection(
        title = stringResource(R.string.label_media_extended_details_themes),
        subtitle = stringResource(R.string.subtitle_media_theme_section),
        trailingActionLabel =
            if (canExpand) {
                stringResource(
                    if (showAll) {
                        R.string.action_media_theme_section_show_less
                    } else {
                        R.string.action_media_theme_section_show_all
                    },
                )
            } else {
                null
            },
        onTrailingAction = { showAll = !showAll },
        modifier = modifier,
    ) {
        MediaThemePreviewBlock(
            themes = visibleThemes,
        )
    }
}

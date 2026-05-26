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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URI
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
import androidx.compose.material3.Slider
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
import androidx.compose.ui.platform.LocalContext
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

private const val MIN_DURATION_MS = 1L

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

internal fun MediaTheme.Variant.videoPreviewSelection(themeId: String): ThemePreviewSelectionState? =
    previews.firstOrNull { it.video.isNotBlank() }?.let { preview ->
        ThemePreviewSelection(
            variant = this,
            preview = preview,
            previewKey = "$themeId:v$version:${preview.mediaIdentity()}",
        )
    }

internal fun formatDuration(ms: Long): String {
    if (ms < 0L) {
        return "--:--"
    }
    val totalSeconds = ms / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "%d:%02d".format(minutes, seconds)
}

internal data class PreviewRowState(
    val isActive: Boolean,
    val canSeek: Boolean,
    val hasVideo: Boolean,
)

internal fun previewRowState(
    activePreviewKey: String?,
    previewKey: String,
    isPlaying: Boolean,
): PreviewRowState =
    PreviewRowState(
        isActive = activePreviewKey == previewKey,
        canSeek = activePreviewKey == previewKey && isPlaying,
        hasVideo = previewKey.isNotBlank(),
    )

private fun sliderValue(
    positionMs: Long,
    durationMs: Long,
): Float {
    if (durationMs < MIN_DURATION_MS) {
        return 0f
    }
    return (positionMs.coerceIn(0L, durationMs).toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
}

private fun seekPositionFromSlider(
    value: Float,
    durationMs: Long,
): Long {
    if (durationMs < MIN_DURATION_MS) {
        return 0L
    }
    return (durationMs.toFloat() * value.coerceIn(0f, 1f)).toLong()
}

internal fun isOpenVideoPreviewUrlSupported(url: String): Boolean {
    val rawUrl = url.trim()
    if (rawUrl.isBlank()) {
        return false
    }
    val scheme = runCatching { URI(rawUrl).scheme?.lowercase() }.getOrNull()
    return scheme == "http" || scheme == "https"
}

internal fun openVideoPreview(
    context: Context,
    url: String,
): Boolean {
    if (!isOpenVideoPreviewUrlSupported(url)) {
        return false
    }
    val parsedUri = Uri.parse(url.trim())

    val viewIntent =
        Intent(Intent.ACTION_VIEW, parsedUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    val chooserIntent =
        Intent.createChooser(viewIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    return try {
        context.startActivity(chooserIntent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}

internal fun MediaTheme.heroPreviewSelections(): List<ThemePreviewSelectionState> =
    variants
        .mapNotNull { variant ->
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

internal fun MediaTheme.hasSelectableVideoAsset(): Boolean =
    video.isNotBlank() ||
        variants.any { variant ->
            variant.previews.any { preview ->
                preview.video.isNotBlank()
            }
        }

@StringRes
internal fun MediaTheme.availabilitySummaryResId(): Int =
    when {
        hasAudioAsset() && hasVideoAsset() -> R.string.label_media_theme_section_audio_video_available
        hasAudioAsset() -> R.string.label_media_theme_section_audio_available
        hasVideoAsset() -> R.string.label_media_theme_section_video_available
        else -> R.string.label_media_theme_section_details_only
    }

@Composable
private fun MediaTheme.availabilitySummary(): String = stringResource(availabilitySummaryResId())

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
    onPlayPreview: (() -> Unit)?,
    onOpenVideo: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val previewSummary =
        selection
            ?.preview
            ?.mediaTagTokens()
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(" ") ?: variant.previewSummaryText()

    Surface(
        color =
            if (isSelected ||
                isActive
            ) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.72f)
            },
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledTonalButton(
                    onClick = { onPlayPreview?.invoke() },
                    enabled = onPlayPreview != null,
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        imageVector = if (isActive) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.label_media_theme_sheet_audio))
                }

                FilledTonalButton(
                    onClick = { onOpenVideo?.invoke() },
                    enabled = onOpenVideo != null,
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(text = stringResource(R.string.label_media_theme_sheet_video))
                }
            }
        }
    }
}

@Composable
private fun ThemeAudioPreviewControls(
    isPlaying: Boolean,
    isBuffering: Boolean,
    positionMs: Long,
    durationMs: Long,
    onPlayPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val resolvedDuration = if (durationMs > 0L) durationMs else 0L
    val sliderPosition = sliderValue(positionMs = positionMs, durationMs = resolvedDuration)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilledTonalButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isBuffering,
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text =
                    stringResource(
                        if (isPlaying) {
                            R.string.label_media_theme_sheet_not_available
                        } else {
                            R.string.label_media_theme_sheet_available
                        },
                    ),
            )
        }

        Slider(
            value = sliderPosition,
            onValueChange = { value ->
                onSeek(seekPositionFromSlider(value = value, durationMs = resolvedDuration))
            },
            enabled = resolvedDuration > 0L,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(positionMs),
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = formatDuration(resolvedDuration),
                style = MaterialTheme.typography.labelMedium,
            )
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
            selection
                ?.variant
                ?.episodes
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
    onSeek: ((Long) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val previewSummary =
        selection
            ?.preview
            ?.mediaTagTokens()
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(" ")
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
                    selection
                        ?.variant
                        ?.episodes
                        ?.takeIf(String::isNotBlank)
                        ?.let { episodes ->
                            Text(
                                text = stringResource(R.string.label_media_theme_sheet_episodes_value, episodes),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                }

                ThemeBadge(
                    label =
                        stringResource(
                            if (canPlay) {
                                R.string.label_media_theme_sheet_available
                            } else {
                                R.string.label_media_theme_sheet_not_available
                            },
                        ),
                )
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

            if (canPlay && onPlayPauseClick != null && onSeek != null) {
                ThemeAudioPreviewControls(
                    isPlaying = isActiveSelection && playbackState.isPlaying,
                    isBuffering = isActiveSelection && playbackState.isBuffering,
                    positionMs = if (isActiveSelection) playbackState.positionMs else 0L,
                    durationMs = if (isActiveSelection) playbackState.durationMs else 0L,
                    onPlayPauseClick = onPlayPauseClick,
                    onSeek = onSeek,
                )
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
    val selectedRowState =
        selectedSelection?.let { selection ->
            previewRowState(
                activePreviewKey = playbackState.activePreviewKey,
                previewKey = selection.previewKey,
                isPlaying = playbackState.isPlaying,
            )
        }
    val hasAudio = previewSelections.any { !it.preview.audio.isNullOrBlank() }
    val hasVideo = theme.hasSelectableVideoAsset()
    val metadataLabel = theme.metaBadgeLabel()
    val context = LocalContext.current

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
                                selectedPreviewKey = selection.previewKey
                                controller.toggle(
                                    ThemePlaybackRequest(
                                        previewKey = selection.previewKey,
                                        audioUrl = selection.preview.audio.orEmpty(),
                                        title = theme.name,
                                    ),
                                )
                            }
                        },
                onSeek =
                    selectedSelection
                        ?.takeIf { selectedRowState?.canSeek == true }
                        ?.let {
                            { positionMs ->
                                controller.seekTo(positionMs)
                                controller.updatePlayback(
                                    positionMs = positionMs,
                                    durationMs = playbackState.durationMs,
                                    isBuffering = playbackState.isBuffering,
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
                        val videoSelection = variant.videoPreviewSelection(theme.themeId)
                        val rowState =
                            variantSelection?.let { selection ->
                                previewRowState(
                                    activePreviewKey = playbackState.activePreviewKey,
                                    previewKey = selection.previewKey,
                                    isPlaying = playbackState.isPlaying,
                                )
                            }

                        if (index > 0) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }

                        ThemeVariantRow(
                            variant = variant,
                            selection = variantSelection,
                            isSelected = variantSelection?.previewKey == selectedSelection?.previewKey,
                            isActive = rowState?.isActive == true,
                            onClick =
                                variantSelection?.let { selection ->
                                    {
                                        selectedPreviewKey = selection.previewKey
                                    }
                                },
                            onPlayPreview =
                                variantSelection?.takeIf { !it.preview.audio.isNullOrBlank() }?.let { selection ->
                                    {
                                        selectedPreviewKey = selection.previewKey
                                        controller.toggle(
                                            ThemePlaybackRequest(
                                                previewKey = selection.previewKey,
                                                audioUrl = selection.preview.audio.orEmpty(),
                                                title = theme.name,
                                            ),
                                        )
                                    }
                                },
                            onOpenVideo =
                                videoSelection?.let { selection ->
                                    {
                                        if (!openVideoPreview(context = context, url = selection.preview.video)) {
                                            controller.onError(message = "Unable to open video preview")
                                        }
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

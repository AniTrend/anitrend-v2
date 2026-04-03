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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.media.R

private fun MediaTheme.hasAudioAsset(): Boolean = !audio.isNullOrBlank()

private fun MediaTheme.hasVideoAsset(): Boolean = video.isNotBlank()

private fun MediaTheme.metaBadgeLabel(): String? {
    val themeMeta = meta ?: return null

    return buildList {
        themeMeta.type.trim().takeIf(String::isNotBlank)?.let {
            add(it.uppercase())
        }
        themeMeta.number.takeIf { it > 0 }?.let {
            add(it.toString())
        }
        themeMeta.version.takeIf { it > 1 }?.let {
            add("v$it")
        }
    }.takeIf { it.isNotEmpty() }?.joinToString(" ")
}

@Composable
private fun MediaTheme.availabilitySummary(): String =
    when {
        hasAudioAsset() && hasVideoAsset() ->
            stringResource(R.string.label_media_theme_section_audio_video_available)
        hasAudioAsset() ->
            stringResource(R.string.label_media_theme_section_audio_available)
        hasVideoAsset() ->
            stringResource(R.string.label_media_theme_section_video_available)
        else ->
            stringResource(R.string.label_media_theme_section_details_only)
    }

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
private fun MediaThemeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.34f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.66f),
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
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.16f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.56f)),
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
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
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
private fun MediaThemeDetailSheet(
    theme: MediaTheme,
    onDismiss: () -> Unit,
) {
    val hasAudio = theme.hasAudioAsset()
    val hasVideo = theme.hasVideoAsset()
    val metadataLabel = theme.metaBadgeLabel()

    ListBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = theme.name,
                style = MaterialTheme.typography.titleLarge,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                theme.meta
                    ?.type
                    ?.trim()
                    ?.takeIf(String::isNotBlank)
                    ?.let { type ->
                        MediaThemeInfoRow(
                            label = stringResource(R.string.label_media_theme_sheet_type),
                            value = type.uppercase(),
                        )
                    }

                theme.meta
                    ?.number
                    ?.takeIf { it > 0 }
                    ?.let { number ->
                        MediaThemeInfoRow(
                            label = stringResource(R.string.label_media_theme_sheet_number),
                            value = number.toString(),
                        )
                    }

                theme.meta
                    ?.version
                    ?.takeIf { it > 1 }
                    ?.let { version ->
                        MediaThemeInfoRow(
                            label = stringResource(R.string.label_media_theme_sheet_version),
                            value = "v$version",
                        )
                    }

                MediaThemeInfoRow(
                    label = stringResource(R.string.label_media_theme_sheet_audio),
                    value =
                        stringResource(
                            if (hasAudio) {
                                R.string.label_media_theme_sheet_available
                            } else {
                                R.string.label_media_theme_sheet_not_available
                            },
                        ),
                )

                MediaThemeInfoRow(
                    label = stringResource(R.string.label_media_theme_sheet_video),
                    value =
                        stringResource(
                            if (hasVideo) {
                                R.string.label_media_theme_sheet_available
                            } else {
                                R.string.label_media_theme_sheet_not_available
                            },
                        ),
                )
            }

            HorizontalDivider()

            if (hasAudio) {
                FilledTonalButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MusicNote,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.action_media_theme_sheet_preview_audio_coming_soon))
                }
            }

            if (hasVideo) {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.action_media_theme_sheet_preview_video_coming_soon))
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
fun MediaThemeSection(
    themes: List<MediaTheme>,
    modifier: Modifier = Modifier,
) {
    if (themes.isEmpty()) {
        return
    }

    var showAll by rememberSaveable(themes.size) { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf<MediaTheme?>(null) }
    val canExpand = themes.size > 3
    val visibleThemes = if (canExpand && !showAll) themes.take(3) else themes

    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.label_media_extended_details_themes),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (canExpand) {
                    TextButton(onClick = { showAll = !showAll }) {
                        Text(
                            text =
                                stringResource(
                                    if (showAll) {
                                        R.string.action_media_theme_section_show_less
                                    } else {
                                        R.string.action_media_theme_section_show_all
                                    },
                                ),
                        )
                    }
                }
            }

            visibleThemes.forEach { theme ->
                MediaThemeItem(
                    theme = theme,
                    onClick = { selectedTheme = theme },
                )
            }
        }
    }

    selectedTheme?.let { theme ->
        MediaThemeDetailSheet(
            theme = theme,
            onDismiss = { selectedTheme = null },
        )
    }
}

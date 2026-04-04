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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.color.asColorInt
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.media.R
import co.anitrend.navigation.MediaDiscoverRouter

private const val TAG_PREVIEW_COUNT = 6

private fun Tag.rankPercent(): Int? =
    (this as? Tag.Extended)
        ?.rank
        ?.takeIf { it > 0 }

private fun Tag.hasDescription(): Boolean = !description.isNullOrBlank()

@Composable
private fun TagBadge(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
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
private fun Tag.rememberAccentColor(spoilerLevel: MediaTagSpoilerLevel): Color {
    val surface = MaterialTheme.colorScheme.surface.toArgb()
    val errorColor = MaterialTheme.colorScheme.error
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val defaultAccent = MaterialTheme.colorScheme.primary

    return remember(this, spoilerLevel, surface, errorColor, secondaryColor, defaultAccent) {
        when (spoilerLevel) {
            MediaTagSpoilerLevel.MEDIA -> errorColor
            MediaTagSpoilerLevel.GENERAL -> secondaryColor
            MediaTagSpoilerLevel.NONE ->
                (this as? Tag.Extended)
                    ?.background
                    ?.let { background ->
                        runCatching { Color(background.asColorInt(surface)) }.getOrNull()
                    }
                    ?: defaultAccent
        }
    }
}

@Composable
private fun MediaTagItem(
    tag: Tag,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spoilerLevel = tag.spoilerLevel()
    val accent = tag.rememberAccentColor(spoilerLevel)
    val containerColor =
        when (spoilerLevel) {
            MediaTagSpoilerLevel.MEDIA -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.38f)
            MediaTagSpoilerLevel.GENERAL -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.34f)
            MediaTagSpoilerLevel.NONE -> accent.copy(alpha = 0.14f).compositeOver(MaterialTheme.colorScheme.surfaceVariant)
        }
    val borderColor =
        when (spoilerLevel) {
            MediaTagSpoilerLevel.MEDIA -> MaterialTheme.colorScheme.error.copy(alpha = 0.48f)
            MediaTagSpoilerLevel.GENERAL -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.42f)
            MediaTagSpoilerLevel.NONE -> accent.copy(alpha = 0.38f)
        }
    val icon =
        when (spoilerLevel) {
            MediaTagSpoilerLevel.MEDIA -> Icons.Filled.VisibilityOff
            MediaTagSpoilerLevel.GENERAL -> Icons.Rounded.Warning
            MediaTagSpoilerLevel.NONE -> Icons.Rounded.Tag
        }

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        border = BorderStroke(1.dp, borderColor),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = containerColor,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        modifier =
            modifier
                .widthIn(max = 300.dp)
                .defaultMinSize(minHeight = 44.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = tag.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.widthIn(max = 176.dp),
        )
        tag.rankPercent()?.let { rank ->
            Spacer(modifier = Modifier.size(8.dp))
            TagBadge(
                label = "$rank%",
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (tag.isAdult) {
            Spacer(modifier = Modifier.size(8.dp))
            TagBadge(
                label = stringResource(R.string.label_media_tag_indicator_adult),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}

@Composable
private fun SpoilerDisclosureCard(
    mediaSpoilerCount: Int,
    generalSpoilerCount: Int,
    spoilersRevealed: Boolean,
    onToggleSpoilers: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spoilerSummary =
        buildList {
            if (mediaSpoilerCount > 0) {
                add(
                    pluralStringResource(
                        R.plurals.plural_media_tag_section_media_spoiler_count,
                        mediaSpoilerCount,
                        mediaSpoilerCount,
                    ),
                )
            }
            if (generalSpoilerCount > 0) {
                add(
                    pluralStringResource(
                        R.plurals.plural_media_tag_section_general_spoiler_count,
                        generalSpoilerCount,
                        generalSpoilerCount,
                    ),
                )
            }
        }.joinToString(" • ")

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.68f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                color =
                    if (spoilersRevealed) {
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.48f)
                    } else {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.48f)
                    },
                contentColor =
                    if (spoilersRevealed) {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(40.dp),
            ) {
                androidx.compose.foundation.layout.Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (spoilersRevealed) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text =
                        stringResource(
                            if (spoilersRevealed) {
                                R.string.label_media_tag_section_spoilers_revealed
                            } else {
                                R.string.label_media_tag_section_spoilers_hidden
                            },
                        ),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = spoilerSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(onClick = onToggleSpoilers) {
                Text(
                    text =
                        stringResource(
                            if (spoilersRevealed) {
                                R.string.action_media_tag_section_hide_spoilers
                            } else {
                                R.string.action_media_tag_section_reveal_spoilers
                            },
                        ),
                )
            }
        }
    }
}

@Composable
private fun TagInfoRow(
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
private fun MediaTagInfoSheet(
    tag: Tag,
    onExploreTag: () -> Unit,
    onDismiss: () -> Unit,
) {
    val spoilerLevel = tag.spoilerLevel()
    val accent = tag.rememberAccentColor(spoilerLevel)
    val description = tag.description?.trim().orEmpty()

    ListBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = accent,
                )
                Text(
                    text = stringResource(R.string.subtitle_media_tag_sheet),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (spoilerLevel) {
                    MediaTagSpoilerLevel.MEDIA ->
                        TagBadge(
                            label = stringResource(R.string.label_media_tag_sheet_media_spoiler),
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )

                    MediaTagSpoilerLevel.GENERAL ->
                        TagBadge(
                            label = stringResource(R.string.label_media_tag_sheet_general_spoiler),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        )

                    MediaTagSpoilerLevel.NONE -> Unit
                }

                if (tag.isAdult) {
                    TagBadge(
                        label = stringResource(R.string.label_media_tag_indicator_adult),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.48f),
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }

            Text(
                text =
                    if (tag.hasDescription()) {
                        description
                    } else {
                        stringResource(R.string.label_media_tag_sheet_no_description)
                    },
                style = MaterialTheme.typography.bodyMedium,
                color =
                    if (tag.hasDescription()) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                fontStyle = if (tag.hasDescription()) FontStyle.Normal else FontStyle.Italic,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                tag.category
                    ?.trim()
                    ?.takeIf(String::isNotBlank)
                    ?.let { category ->
                        TagInfoRow(
                            label = stringResource(R.string.label_media_tag_sheet_category),
                            value = category,
                        )
                    }

                tag.rankPercent()?.let { rank ->
                    TagInfoRow(
                        label = stringResource(R.string.label_media_tag_sheet_confidence),
                        value = "$rank%",
                    )
                }
            }

            HorizontalDivider()

            FilledTonalButton(
                onClick = onExploreTag,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Tag,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.action_media_tag_sheet_explore))
            }
        }
    }
}

@Composable
fun MediaTagSection(
    tags: List<Tag>,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tags.isEmpty()) {
        return
    }

    val partition = remember(tags) { partitionMediaTags(tags) }
    val rememberKey = remember(tags) { tags.map(Tag::id) }

    var showAllSafeTags by rememberSaveable(rememberKey) { mutableStateOf(false) }
    var showSpoilers by rememberSaveable(rememberKey) { mutableStateOf(false) }
    var selectedTag by remember(tags) { mutableStateOf<Tag?>(null) }

    val visibleSafeTags =
        if (showAllSafeTags) {
            partition.safeTags
        } else {
            partition.safeTags.take(TAG_PREVIEW_COUNT)
        }
    val hasSafeTagOverflow = partition.safeTags.size > TAG_PREVIEW_COUNT

    MediaHubSection(
        title = stringResource(R.string.label_media_tag_section_title),
        subtitle = stringResource(R.string.subtitle_media_tag_section),
        trailingActionLabel =
            if (hasSafeTagOverflow) {
                stringResource(
                    if (showAllSafeTags) {
                        R.string.action_media_tag_section_show_less
                    } else {
                        R.string.action_media_tag_section_show_all
                    },
                )
            } else {
                null
            },
        onTrailingAction =
            if (hasSafeTagOverflow) {
                { showAllSafeTags = !showAllSafeTags }
            } else {
                null
            },
        modifier = modifier,
    ) {
        if (visibleSafeTags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                visibleSafeTags.forEach { tag ->
                    MediaTagItem(
                        tag = tag,
                        onClick = { selectedTag = tag },
                    )
                }
            }
        } else {
            MediaHubSectionEmptyState(
                title = stringResource(R.string.label_media_tag_section_no_safe_tags_title),
                message = stringResource(R.string.label_media_tag_section_no_safe_tags_message),
                icon = Icons.Rounded.Info,
            )
        }

        if (partition.spoilerTags.isNotEmpty()) {
            SpoilerDisclosureCard(
                mediaSpoilerCount = partition.mediaSpoilerCount,
                generalSpoilerCount = partition.generalSpoilerCount,
                spoilersRevealed = showSpoilers,
                onToggleSpoilers = { showSpoilers = !showSpoilers },
            )
        }

        if (showSpoilers && partition.spoilerTags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                partition.spoilerTags.forEach { tag ->
                    MediaTagItem(
                        tag = tag,
                        onClick = { selectedTag = tag },
                    )
                }
            }
        }
    }

    selectedTag?.let { tag ->
        MediaTagInfoSheet(
            tag = tag,
            onDismiss = { selectedTag = null },
            onExploreTag = {
                onMediaDiscoverableItemClick(
                    MediaDiscoverRouter.MediaDiscoverParam(tag = tag.name),
                )
                selectedTag = null
            },
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaTagSectionPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaTagSection(
            tags =
                listOf(
                    Tag.Extended(
                        rank = 91,
                        isMediaSpoiler = false,
                        background = "#6AA5FF",
                        name = "Time Skip",
                        description = "Narrative jumps across major character milestones.",
                        category = "Storytelling",
                        isGeneralSpoiler = false,
                        isAdult = false,
                        id = 0,
                    ),
                    Tag.Extended(
                        rank = 78,
                        isMediaSpoiler = true,
                        background = "#FF7A7A",
                        name = "Major protagonist death",
                        description = "Contains a critical late-story loss.",
                        category = "Plot",
                        isGeneralSpoiler = false,
                        isAdult = false,
                        id = 1,
                    ),
                ),
            onMediaDiscoverableItemClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

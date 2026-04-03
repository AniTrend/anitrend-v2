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
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.media.R
import co.anitrend.navigation.MediaDiscoverRouter

private enum class TagSpoilerLevel {
    NONE,
    GENERAL,
    MEDIA,
}

private fun Tag.spoilerLevel(): TagSpoilerLevel =
    when {
        this is Tag.Extended && isMediaSpoiler -> TagSpoilerLevel.MEDIA
        isGeneralSpoiler -> TagSpoilerLevel.GENERAL
        else -> TagSpoilerLevel.NONE
    }

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
private fun MediaTagItem(
    tag: Tag,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spoilerLevel = tag.spoilerLevel()
    val (containerColor, borderColor, iconTint) =
        when (spoilerLevel) {
            TagSpoilerLevel.MEDIA ->
                Triple(
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.34f),
                    MaterialTheme.colorScheme.error.copy(alpha = 0.45f),
                    MaterialTheme.colorScheme.error,
                )

            TagSpoilerLevel.GENERAL ->
                Triple(
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.32f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f),
                    MaterialTheme.colorScheme.secondary,
                )

            TagSpoilerLevel.NONE ->
                Triple(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
                    MaterialTheme.colorScheme.onSurfaceVariant,
                )
        }

    val icon =
        when (spoilerLevel) {
            TagSpoilerLevel.MEDIA -> Icons.Filled.VisibilityOff
            TagSpoilerLevel.GENERAL -> Icons.Rounded.Warning
            TagSpoilerLevel.NONE -> Icons.Rounded.Tag
        }

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        border = BorderStroke(1.dp, borderColor),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = containerColor,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        modifier =
            modifier
                .widthIn(max = 280.dp)
                .defaultMinSize(minHeight = 40.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = tag.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.widthIn(max = 168.dp),
        )
        tag.rankPercent()?.let { rank ->
            Spacer(modifier = Modifier.size(8.dp))
            TagBadge(
                label = "$rank%",
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (tag.isAdult) {
            Spacer(modifier = Modifier.size(8.dp))
            TagBadge(
                label = stringResource(R.string.label_media_tag_indicator_adult),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.42f),
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

    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = if (spoilersRevealed) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = null,
                tint =
                    if (spoilersRevealed) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
            )
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
            Text(
                text = tag.name,
                style = MaterialTheme.typography.titleLarge,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (spoilerLevel) {
                    TagSpoilerLevel.MEDIA ->
                        TagBadge(
                            label = stringResource(R.string.label_media_tag_sheet_media_spoiler),
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )

                    TagSpoilerLevel.GENERAL ->
                        TagBadge(
                            label = stringResource(R.string.label_media_tag_sheet_general_spoiler),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        )

                    TagSpoilerLevel.NONE -> Unit
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

    val safeTags = remember(tags) { tags.filter { it.spoilerLevel() == TagSpoilerLevel.NONE } }
    val spoilerTags = remember(tags) { tags.filter { it.spoilerLevel() != TagSpoilerLevel.NONE } }
    val mediaSpoilerCount = remember(tags) { tags.count { it.spoilerLevel() == TagSpoilerLevel.MEDIA } }
    val generalSpoilerCount = remember(tags) { tags.count { it.spoilerLevel() == TagSpoilerLevel.GENERAL } }

    var showAllSafeTags by rememberSaveable(tags.size) { mutableStateOf(false) }
    var showSpoilers by rememberSaveable(tags.size) { mutableStateOf(false) }
    var selectedTag by remember(tags) { mutableStateOf<Tag?>(null) }

    val visibleSafeTags = if (showAllSafeTags) safeTags else safeTags.take(6)
    val hasSafeTagOverflow = safeTags.size > 6

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_media_tag_section_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (hasSafeTagOverflow) {
                TextButton(
                    onClick = { showAllSafeTags = !showAllSafeTags },
                ) {
                    Text(
                        text =
                            stringResource(
                                if (showAllSafeTags) {
                                    R.string.action_media_tag_section_show_less
                                } else {
                                    R.string.action_media_tag_section_show_all
                                },
                            ),
                    )
                }
            }
        }

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
        }

        if (spoilerTags.isNotEmpty()) {
            SpoilerDisclosureCard(
                mediaSpoilerCount = mediaSpoilerCount,
                generalSpoilerCount = generalSpoilerCount,
                spoilersRevealed = showSpoilers,
                onToggleSpoilers = { showSpoilers = !showSpoilers },
            )
        }

        if (showSpoilers && spoilerTags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                spoilerTags.forEach { tag ->
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

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaTagSectionPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        MediaTagSection(
            tags =
                listOf(
                    Tag.Extended(
                        rank = 92,
                        isMediaSpoiler = false,
                        background = null,
                        name = "Found Family",
                        description = "Characters create a close-knit bond outside their original family structure.",
                        category = "Dynamic",
                        isGeneralSpoiler = false,
                        isAdult = false,
                        id = 1,
                    ),
                    Tag.Extended(
                        rank = 71,
                        isMediaSpoiler = false,
                        background = null,
                        name = "Politics",
                        description = "Power struggles and formal political systems shape the narrative.",
                        category = "Setting",
                        isGeneralSpoiler = false,
                        isAdult = true,
                        id = 2,
                    ),
                    Tag.Extended(
                        rank = 58,
                        isMediaSpoiler = true,
                        background = null,
                        name = "Identity Reveal",
                        description = "A major reveal changes how key characters understand one another.",
                        category = "Plot",
                        isGeneralSpoiler = false,
                        isAdult = false,
                        id = 3,
                    ),
                    Tag.Core(
                        name = "Time Skip",
                        description = "The story jumps forward significantly.",
                        category = "Structure",
                        isGeneralSpoiler = true,
                        isAdult = false,
                        id = 4,
                    ),
                ),
            onMediaDiscoverableItemClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

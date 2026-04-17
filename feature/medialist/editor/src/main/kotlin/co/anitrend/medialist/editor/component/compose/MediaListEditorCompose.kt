/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.medialist.editor.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.AniTrendDimensions
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.R
import co.anitrend.medialist.editor.component.compose.state.MediaListEditorState
import co.anitrend.medialist.editor.component.compose.state.rememberMediaListEditorState

enum class OnMediaListEditorAction {
    SAVE,
    DELETE,
}

@Composable
fun MediaListEditorScreen(
    modifier: Modifier = Modifier,
    state: MediaListEditorState,
    onAction: (OnMediaListEditorAction) -> Unit,
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        EditorHero(
            state = state,
            onAction = onAction,
        )
        PrimaryEditGroup(state = state)
        SecondaryEditGroup(state = state)
        MoreOptionsSection(state = state)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EditorHero(
    state: MediaListEditorState,
    onAction: (OnMediaListEditorAction) -> Unit,
) {
    val canDelete = state.media.mediaList != null
    val quickFacts = mediaQuickFacts(state)

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            HeroPoster(
                media = state.media,
                modifier =
                    Modifier
                        .width(84.dp)
                        .aspectRatio(AniTrendDimensions.series_image_aspect_ratio),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = state.mediaTitle.ifBlank { stringResource(R.string.placeholder_media_list_editor_media_title) },
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(R.string.subtitle_media_list_editor_quick_edit),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                quickFacts.takeIf(String::isNotBlank)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (canDelete) {
                OutlinedButton(
                    onClick = { onAction(OnMediaListEditorAction.DELETE) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.action_media_list_editor_delete_entry))
                }
            }

            Button(
                onClick = { onAction(OnMediaListEditorAction.SAVE) },
                modifier = Modifier.weight(if (canDelete) 1.15f else 1f),
                shape = RoundedCornerShape(18.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.action_media_list_editor_save_changes))
            }
        }
    }
}

@Composable
private fun HeroPoster(
    media: Media,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
    ) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.POSTER,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun PrimaryEditGroup(state: MediaListEditorState) {
    EditorGroupCard {
        WatchStatusSection(
            selectedStatus = state.selectedStatus ?: MediaListStatus.PLANNING,
            onStatusSelected = state::onStatusSelected,
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        ProgressSection(state = state)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        ScoreSection(state = state)
    }
}

@Composable
private fun SecondaryEditGroup(state: MediaListEditorState) {
    EditorGroupCard {
        VolumesAndRepeatSection(state = state)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        DateSelectionSection(state = state)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        PrivacySection(
            isPrivate = state.isPrivate,
            onPrivacyChange = { state.isPrivate = it },
        )
    }
}

@Composable
private fun EditorGroupCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            content = content,
        )
    }
}

@Composable
private fun SectionHeading(
    title: String,
    supportingText: String? = null,
    trailingText: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            if (!supportingText.isNullOrBlank()) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (!trailingText.isNullOrBlank()) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WatchStatusSection(
    selectedStatus: MediaListStatus,
    onStatusSelected: (MediaListStatus) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeading(
            title = stringResource(R.string.label_media_list_editor_watch_status),
            trailingText = stringResource(R.string.label_media_list_editor_primary_state),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MediaListStatus.entries.forEach { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { onStatusSelected(status) },
                    label = { Text(status.alias.toString()) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(statusIcon(status)),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun ProgressSection(state: MediaListEditorState) {
    val label =
        if (state.mediaType == MediaType.ANIME) {
            stringResource(R.string.label_media_list_editor_episode_progress)
        } else {
            stringResource(R.string.label_media_list_editor_chapter_progress)
        }
    val progressFraction =
        if (state.totalUnits != null && state.totalUnits > 0) {
            (state.currentProgress.toFloat() / state.totalUnits.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        NumericStepperRow(
            label = label,
            value = state.progressText,
            onValueChange = state::updateProgressText,
            onDecrement = { state.adjustProgress(-1) },
            onIncrement = { state.adjustProgress(1) },
            valueSuffix = state.totalUnits?.toString(),
            supportingText = stringResource(R.string.label_media_list_editor_progress_auto_complete_hint),
            canDecrement = state.currentProgress > 0,
            canIncrement = state.totalUnits?.let { state.currentProgress < it } ?: true,
        )
    }
}

@Composable
private fun ScoreSection(state: MediaListEditorState) {
    FormatAwareScoreControl(
        scoreFormat = state.scoreFormat,
        scoreText = state.scoreText,
        maxScore = state.maxScore,
        onScoreChange = state::updateScoreText,
        onScoreIncrement = { state.adjustScore(1) },
        onScoreDecrement = { state.adjustScore(-1) },
        onClearScore = state::clearScore,
        onDiscreteScoreSelected = state::setDiscreteScore,
    )
}

@Composable
private fun VolumesAndRepeatSection(state: MediaListEditorState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (state.mediaType == MediaType.MANGA) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NumericStepperRow(
                    label = stringResource(R.string.media_list_editor_label_progress_volumes),
                    value = state.volumeProgressText,
                    onValueChange = state::updateVolumeProgressText,
                    onDecrement = { state.adjustVolumeProgress(-1) },
                    onIncrement = { state.adjustVolumeProgress(1) },
                    modifier = Modifier.weight(1f),
                    valueSuffix = state.totalVolumes?.toString(),
                    canDecrement = state.currentVolumeProgress > 0,
                    canIncrement = state.totalVolumes?.let { state.currentVolumeProgress < it } ?: true,
                    compact = true,
                )
                NumericStepperRow(
                    label = stringResource(R.string.media_list_editor_label_repeat_count),
                    value = state.repeatText,
                    onValueChange = state::updateRepeatText,
                    onDecrement = { state.adjustRepeat(-1) },
                    onIncrement = { state.adjustRepeat(1) },
                    modifier = Modifier.weight(1f),
                    canDecrement = state.currentRepeatCount > 0,
                    compact = true,
                )
            }
        } else {
            NumericStepperRow(
                label = stringResource(R.string.media_list_editor_label_repeat_count),
                value = state.repeatText,
                onValueChange = state::updateRepeatText,
                onDecrement = { state.adjustRepeat(-1) },
                onIncrement = { state.adjustRepeat(1) },
                canDecrement = state.currentRepeatCount > 0,
                compact = true,
            )
        }
    }
}

@Composable
private fun DateSelectionSection(state: MediaListEditorState) {
    if (state.showStartDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = state.startDateEpoch,
            )
        DatePickerDialog(
            onDismissRequest = { state.showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.onStartDateSelected(datePickerState.selectedDateMillis)
                    },
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { state.showStartDatePicker = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showEndDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = state.endDateEpoch,
            )
        DatePickerDialog(
            onDismissRequest = { state.showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.onEndDateSelected(datePickerState.selectedDateMillis)
                    },
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { state.showEndDatePicker = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DateFieldCard(
            label = stringResource(R.string.label_media_list_editor_start_date),
            value = state.startDateText.ifBlank { stringResource(R.string.label_media_list_editor_not_set) },
            modifier = Modifier.weight(1f),
            onClick = { state.showStartDatePicker = true },
        )
        DateFieldCard(
            label = stringResource(R.string.label_media_list_editor_end_date),
            value = state.endDateText.ifBlank { stringResource(R.string.label_media_list_editor_not_set) },
            modifier = Modifier.weight(1f),
            onClick = { state.showEndDatePicker = true },
        )
    }
}

@Composable
private fun DateFieldCard(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = stringResource(R.string.action_media_list_editor_select_date),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PrivacySection(
    isPrivate: Boolean,
    onPrivacyChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(42.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
            ) {}
            Icon(
                imageVector = Icons.Filled.VisibilityOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = stringResource(R.string.label_media_list_editor_private_update),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = stringResource(R.string.label_media_list_editor_private_update_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(
            checked = isPrivate,
            onCheckedChange = onPrivacyChange,
        )
    }
}

@Composable
private fun MoreOptionsSection(state: MediaListEditorState) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    EditorGroupCard {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeading(
                title = stringResource(R.string.label_media_list_editor_more_options),
                supportingText =
                    if (expanded) {
                        stringResource(R.string.label_media_list_editor_optional)
                    } else {
                        stringResource(R.string.label_media_list_editor_collapsed_by_default)
                    },
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (expanded) {
            if (state.advancedScoresText.isNotEmpty()) {
                AdvancedScoresSection(state = state)
            }

            if (state.advancedScoresText.isNotEmpty() && state.customLists.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            }

            if (state.customLists.isNotEmpty()) {
                CustomListsSection(
                    customLists = state.customLists,
                    onCustomListToggle = { listName, isSelected ->
                        state.toggleCustomList(listName, isSelected)
                    },
                )
            }

            if (state.advancedScoresText.isNotEmpty() || state.customLists.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            }

            NotesSection(
                notes = state.notesText,
                onNotesChange = { state.notesText = it },
            )
        }
    }
}

@Composable
private fun AdvancedScoresSection(state: MediaListEditorState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeading(
            title = stringResource(R.string.label_media_list_editor_advanced_scores, state.advancedScoresText.size),
        )
        state.advancedScoresText.forEach { (name, value) ->
            NumericStepperRow(
                label = name,
                value = value,
                onValueChange = { newValue -> state.setAdvancedScore(name, newValue) },
                onDecrement = { state.adjustAdvancedScore(name, -1) },
                onIncrement = { state.adjustAdvancedScore(name, 1) },
                valueSuffix = state.maxScore,
                canDecrement = (value.toFloatOrNull() ?: 0f) > 0f,
                canIncrement = (value.toFloatOrNull() ?: 0f) < scoreFormatMax(state.scoreFormat),
                compact = true,
            )
        }
    }
}

@Composable
private fun CustomListsSection(
    customLists: Map<String, Boolean>,
    onCustomListToggle: (listName: String, isSelected: Boolean) -> Unit,
) {
    val selectedCount = customLists.count { it.value }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeading(
            title = stringResource(R.string.label_media_list_editor_custom_lists_count, customLists.size),
            trailingText =
                if (selectedCount > 0) {
                    stringResource(R.string.label_media_list_editor_custom_lists_selected, selectedCount)
                } else {
                    null
                },
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            customLists.forEach { (name, isSelected) ->
                FilterChip(
                    selected = isSelected,
                    onClick = { onCustomListToggle(name, !isSelected) },
                    label = { Text(name) },
                )
            }
        }
    }
}

@Composable
private fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Notes,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.label_media_list_editor_personal_notes),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = stringResource(R.string.label_media_list_editor_optional),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.placeholder_media_list_editor_personal_notes)) },
            minLines = 4,
            maxLines = 5,
        )
    }
}

@Composable
private fun mediaQuickFacts(state: MediaListEditorState): String {
    val media = state.media
    val parts = mutableListOf<String>()

    media.startDate.year
        .takeIf { it > 0 }
        ?.let { parts += it.toString() }
    media.format
        ?.alias
        ?.toString()
        ?.takeIf(String::isNotBlank)
        ?.let(parts::add)

    when (val category = media.category) {
        is Media.Category.Anime -> {
            category.episodes.takeIf { it > 0 }?.let {
                parts += stringResource(R.string.label_media_list_editor_episode_count, it)
            }
        }
        is Media.Category.Manga -> {
            category.chapters.takeIf { it > 0 }?.let {
                parts += stringResource(R.string.label_media_list_editor_chapter_count, it)
            }
            category.volumes.takeIf { it > 0 }?.let {
                parts += stringResource(R.string.label_media_list_editor_volume_count, it)
            }
        }
    }

    return parts.joinToString(" • ")
}

private fun statusIcon(status: MediaListStatus): Int =
    when (status) {
        MediaListStatus.CURRENT -> co.anitrend.common.media.ui.R.drawable.ic_current
        MediaListStatus.COMPLETED -> co.anitrend.common.media.ui.R.drawable.ic_completed
        MediaListStatus.DROPPED -> co.anitrend.common.media.ui.R.drawable.ic_dropped
        MediaListStatus.PAUSED -> co.anitrend.common.media.ui.R.drawable.ic_paused
        MediaListStatus.REPEATING -> co.anitrend.common.media.ui.R.drawable.ic_repeat
        else -> co.anitrend.common.media.ui.R.drawable.ic_planning
    }

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaListEditorScreenPreview(
    @PreviewParameter(MediaListEditorContentPreviewProvider::class) media: Media,
) {
    PreviewTheme(wrapInSurface = true) {
        val state =
            rememberMediaListEditorState(
                media = media,
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                dateHelper = AniTrendDateHelper(),
            )
        MediaListEditorScreen(
            state = state,
            onAction = {},
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaListEditorDiscreteScorePreview(
    @PreviewParameter(MediaListEditorContentPreviewProvider::class) media: Media,
) {
    PreviewTheme(wrapInSurface = true) {
        val state =
            rememberMediaListEditorState(
                media = media,
                scoreFormat = ScoreFormat.POINT_5,
                dateHelper = AniTrendDateHelper(),
            )
        MediaListEditorScreen(
            state = state,
            onAction = {},
        )
    }
}

private fun scoreFormatMax(scoreFormat: ScoreFormat): Float =
    when (scoreFormat) {
        ScoreFormat.POINT_10,
        ScoreFormat.POINT_10_DECIMAL,
        -> 10f
        ScoreFormat.POINT_100 -> 100f
        ScoreFormat.POINT_3 -> 3f
        ScoreFormat.POINT_5 -> 5f
    }

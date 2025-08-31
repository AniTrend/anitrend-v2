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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.R
import co.anitrend.medialist.editor.component.compose.state.MediaListEditorState
import co.anitrend.medialist.editor.component.compose.state.rememberMediaListEditorState
import co.anitrend.medialist.editor.component.compose.util.filterDecimalInputOnePlace
import co.anitrend.medialist.editor.component.compose.util.filterScoreInput

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
                .padding(horizontal = 16.dp),
    ) {
        EditorHeader(
            mediaTitle = state.mediaTitle,
            canDelete = state.media.mediaList != null,
            onAction = onAction,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrivacySection(
            isPrivate = state.isPrivate,
            onPrivacyChange = { state.isPrivate = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        WatchStatusSection(
            selectedStatus = state.selectedStatus,
            onStatusSelected = state::onStatusSelected,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSection(
            mediaType = state.mediaType,
            progressText = state.progressText,
            onProgressChange = state::updateProgressText,
            totalUnits = state.totalUnits,
            currentProgress = state.progressText.toIntOrNull() ?: 0,
        )
        Spacer(modifier = Modifier.height(16.dp))
        VolumesAndRepeatSection(
            mediaType = state.mediaType,
            volumeProgress = state.volumeProgressText,
            onVolumeChange = state::updateVolumeProgressText,
            repeatCount = state.repeatText,
            onRepeatChange = state::updateRepeatText,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateSelectionSection(state)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreSection(
            score = state.scoreText,
            onScoreChange = state::updateScoreText,
            maxScore = state.maxScore,
            scoreFormat = state.scoreFormat,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (state.advancedScoresText.isNotEmpty()) {
            AdvancedScoresSection(
                values = state.advancedScoresText,
                onValueChange = { name, value ->
                    state.setAdvancedScore(name, value)
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        CustomListsSection(
            customLists = state.customLists,
            onCustomListToggle = { listName, isSelected ->
                state.toggleCustomList(listName, isSelected)
            },
            onCreateNewList = { /* TODO: Handle create new list */ },
        )
        Spacer(modifier = Modifier.height(16.dp))
        NotesSection(
            notes = state.notesText,
            onNotesChange = { state.notesText = it },
        )
        Spacer(modifier = Modifier.height(32.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_save))
        }
    }
}

@Composable
private fun EditorHeader(
    mediaTitle: String,
    canDelete: Boolean,
    onAction: (OnMediaListEditorAction) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.title_media_list_editor_add_to_library),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.subtitle_media_list_editor_manage_media, mediaTitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = { onAction(OnMediaListEditorAction.SAVE) }) {
            Icon(
                Icons.Filled.Save,
                contentDescription = stringResource(co.anitrend.common.medialist.ui.R.string.action_media_list_save),
            )
        }
        if (canDelete) {
            IconButton(onClick = { onAction(OnMediaListEditorAction.DELETE) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = stringResource(co.anitrend.common.medialist.ui.R.string.action_media_list_delete),
                )
            }
        }
    }
}

@Composable
private fun PrivacySection(
    isPrivate: Boolean,
    onPrivacyChange: (Boolean) -> Unit,
) {
    Column {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors =
                CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.label_media_list_editor_private_update), style = MaterialTheme.typography.bodyLarge)
                    Text(
                        stringResource(R.string.label_media_list_editor_private_update_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(checked = isPrivate, onCheckedChange = onPrivacyChange)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchStatusSection(
    selectedStatus: MediaListStatus?,
    onStatusSelected: (MediaListStatus) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val items = MediaListStatus.entries

    Column {
        Text(
            stringResource(R.string.label_media_list_editor_watch_status),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value =
                    selectedStatus?.alias?.toString() ?: stringResource(R.string.placeholder_media_list_editor_select_status),
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    val resource =
                        when (selectedStatus) {
                            MediaListStatus.CURRENT -> co.anitrend.common.media.ui.R.drawable.ic_current
                            MediaListStatus.COMPLETED -> co.anitrend.common.media.ui.R.drawable.ic_completed
                            MediaListStatus.DROPPED -> co.anitrend.common.media.ui.R.drawable.ic_dropped
                            MediaListStatus.PAUSED -> co.anitrend.common.media.ui.R.drawable.ic_paused
                            MediaListStatus.REPEATING -> co.anitrend.common.media.ui.R.drawable.ic_repeat
                            else -> co.anitrend.common.media.ui.R.drawable.ic_planning
                        }
                    Icon(painter = painterResource(resource), contentDescription = "")
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.alias.toString()) },
                        onClick = {
                            onStatusSelected(status)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressSection(
    mediaType: MediaType,
    progressText: String,
    onProgressChange: (String) -> Unit,
    totalUnits: Int?,
    currentProgress: Int,
) {
    val label =
        if (mediaType == MediaType.ANIME) {
            stringResource(R.string.label_media_list_editor_episode_progress)
        } else {
            stringResource(R.string.label_media_list_editor_chapter_progress)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.label_progress_percentage, (progressFraction * 100).toInt()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

    Column {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = progressText,
            onValueChange = { onProgressChange(it.filter { char -> char.isDigit() }) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    stringResource(
                        if (mediaType ==
                            MediaType.ANIME
                        ) {
                            R.string.label_media_list_editor_current_episode
                        } else {
                            R.string.label_media_list_editor_current_chapter
                        },
                    ),
                )
            },
            suffix = { totalUnits?.let { Text("/ $it") } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(4.dp))
        val progressFraction =
            if (totalUnits != null && totalUnits > 0) {
                (currentProgress.toFloat() / totalUnits.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.label_media_list_editor_progress_percentage, (progressFraction * 100).toInt()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LinearProgressIndicator(
            progress = { progressFraction },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
        )
    }

    if (state.showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.endDate.toEpochMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { state.showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.onEndDateSelected(datePickerState.selectedDateMillis)
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { state.showEndDatePicker = false }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.label_start_date), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = state.startDateText,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { state.showStartDatePicker = true },
                label = { Text("yyyy/MM/dd") },
                trailingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = stringResource(R.string.action_select_date)) },
            )
        }
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.label_end_date), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = state.endDateText,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { state.showEndDatePicker = true },
                label = { Text("yyyy/MM/dd") },
                trailingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = stringResource(R.string.action_select_date)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { state.showStartDatePicker = false }) {
                    Text("Cancel")
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { state.showEndDatePicker = false }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(Modifier.weight(1f)) {
            OutlinedTextField(
                value = state.startDateText,
                onValueChange = {},
                readOnly = true,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { state.showStartDatePicker = true },
                label = { Text(stringResource(R.string.label_media_list_editor_start_date)) },
                trailingIcon = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = stringResource(R.string.action_media_list_editor_select_date),
                    )
                },
            )
        }
        Column(Modifier.weight(1f)) {
            OutlinedTextField(
                value = state.endDateText,
                onValueChange = {},
                readOnly = true,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { state.showEndDatePicker = true },
                label = { Text(stringResource(R.string.label_media_list_editor_end_date)) },
                trailingIcon = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = stringResource(R.string.action_media_list_editor_select_date),
                    )
                },
            )
        }
    }
}

@Composable
private fun ScoreSection(
    score: String,
    onScoreChange: (String) -> Unit,
    maxScore: String,
    scoreFormat: ScoreFormat,
) {
    Column {
        Text(
            stringResource(R.string.label_media_list_editor_your_score),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        OutlinedTextField(
            value = score,
            onValueChange = { value ->
                val cleaned = filterScoreInput(value, scoreFormat)
                onScoreChange(cleaned)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.label_media_list_editor_score)) },
            suffix = { Text("/ $maxScore") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

@Composable
private fun VolumesAndRepeatSection(
    mediaType: MediaType,
    volumeProgress: String,
    onVolumeChange: (String) -> Unit,
    repeatCount: String,
    onRepeatChange: (String) -> Unit,
) {
    Column {
        if (mediaType == MediaType.MANGA) {
            Text(
                stringResource(co.anitrend.medialist.editor.R.string.media_list_editor_label_progress_volumes),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = volumeProgress,
                onValueChange = { onVolumeChange(it.filter { ch -> ch.isDigit() }) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(co.anitrend.medialist.editor.R.string.media_list_editor_label_progress_volumes)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(12.dp))
        }
        Text(stringResource(co.anitrend.medialist.editor.R.string.media_list_editor_label_repeat_count), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = repeatCount,
            onValueChange = { onRepeatChange(it.filter { ch -> ch.isDigit() }) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(co.anitrend.medialist.editor.R.string.media_list_editor_label_repeat_count)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

@Composable
private fun AdvancedScoresSection(
    values: Map<String, String>,
    onValueChange: (name: String, value: String) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.label_media_list_editor_advanced_scores, values.size), style = MaterialTheme.typography.titleMedium)
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
            )
        }
        if (expanded) {
            Spacer(Modifier.height(4.dp))
            values.forEach { (name, value) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        val cleaned =
                            filterDecimalInputOnePlace(newValue)
                        onValueChange(name, cleaned)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    label = { Text(name) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        }
    }
}

@Composable
private fun CustomListsSection(
    customLists: Map<String, Boolean>,
    onCustomListToggle: (listName: String, isSelected: Boolean) -> Unit,
    onCreateNewList: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selectedCount = customLists.count { it.value }

    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.label_media_list_editor_custom_lists_count, customLists.size), style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCount > 0) {
                    AssistChip(
                        onClick = { expanded = !expanded },
                        label = { Text(stringResource(R.string.label_media_list_editor_custom_lists_selected, selectedCount)) },
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                )
            }
        }

        if (expanded) {
            customLists.forEach { (name, isSelected) ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { onCustomListToggle(name, !isSelected) }
                            .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = isSelected, onCheckedChange = { onCustomListToggle(name, it) })
                    Spacer(Modifier.width(8.dp))
                    Text(name, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onCreateNewList,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.action_media_list_editor_create_new_list))
            }
        }
    }
}

@Composable
private fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(stringResource(R.string.label_media_list_editor_personal_notes), style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier =
                Modifier
                    .fillMaxWidth(),
            label = { Text(stringResource(R.string.placeholder_media_list_editor_personal_notes)) },
            maxLines = 5,
        )
    }
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

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.Media.SiteUrl
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.R
import co.anitrend.medialist.editor.component.compose.helper.MediaListEditorState
import co.anitrend.medialist.editor.component.compose.helper.rememberMediaListEditorState
import co.anitrend.navigation.MediaListEditorRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListEditorSheetScreen(
    mediaData: LiveData<Media?>,
    param: MediaListEditorRouter.MediaListEditorParam,
    onDismiss: () -> Unit,
    onSave: (MediaList.Core) -> Unit,
) {
    var showSheet by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val editorState = rememberMediaListEditorState(
        mediaData = mediaData,
        param = param,
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                onDismiss()
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.9f),
        ) {
            MediaListEditorContent(
                state = editorState,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showSheet = false
                            onDismiss()
                        }
                    }
                },
                onSave = {
                    val updatedMediaList = editorState.buildMediaListCore()
                    onSave(updatedMediaList)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showSheet = false
                            onDismiss()
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun MediaListEditorContent(
    state: MediaListEditorState,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp), // Space for save button
    ) {
        EditorHeader(
            mediaTitle = state.mediaTitle,
            onDismiss = onDismiss,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrivacySection(
            isPrivate = state.privateUpdate,
            onPrivacyChange = { state.privateUpdate = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        WatchStatusSection(
            selectedStatus = state.selectedStatus,
            onStatusSelected = { state.selectedStatus = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSection(
            mediaType = state.param.mediaType,
            progressText = state.progressText,
            onProgressChange = { state.progressText = it },
            totalUnits = state.totalUnits,
            currentProgress = state.progressText.toIntOrNull() ?: 0,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateSelectionSection(state)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreSection(
            score = state.scoreText,
            onScoreChange = { state.scoreText = it },
            maxScore = "100"//state.maxScore,
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomListsSection(
            customLists = state.customLists,
            onCustomListToggle = { listName, isSelected ->
                state.customLists = state.customLists.toMutableMap().apply { this[listName] = isSelected }
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
private fun EditorHeader(mediaTitle: String, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.title_add_to_library),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.subtitle_manage_media, mediaTitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDismiss) {
            Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.action_close))
        }
    }
}

@Composable
private fun PrivacySection(isPrivate: Boolean, onPrivacyChange: (Boolean) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )
            Text(
                stringResource(R.string.label_privacy),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Row(
                modifier = Modifier
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
                    Text(stringResource(R.string.label_private_update), style = MaterialTheme.typography.bodyLarge)
                    Text(
                        stringResource(R.string.label_private_update_description),
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
            stringResource(R.string.label_watch_status),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selectedStatus?.name?.replaceFirstChar { it.titlecase() } ?: stringResource(R.string.placeholder_select_status),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name.replaceFirstChar { it.titlecase() }) },
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
    val label = if (mediaType == MediaType.ANIME) stringResource(R.string.label_episode_progress)
    else stringResource(R.string.label_chapter_progress)

    val icon = if (mediaType == MediaType.ANIME) Icons.Filled.PlayArrow else Icons.Filled.MenuBook

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(label, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = progressText,
            onValueChange = { onProgressChange(it.filter { char -> char.isDigit() }) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(if (mediaType == MediaType.ANIME) R.string.label_current_episode else R.string.label_current_chapter)) },
            suffix = { totalUnits?.let { Text("/ $it") } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(4.dp))
        val progressFraction = if (totalUnits != null && totalUnits > 0) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelectionSection(state: MediaListEditorState) {
    if (state.showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.startDate.toEpochMillis(),
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

@Composable
private fun ScoreSection(score: String, onScoreChange: (String) -> Unit, maxScore: String) {
    Column {
        Text(stringResource(R.string.label_your_score), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = score,
            onValueChange = { onScoreChange(it.filter { char -> char.isDigit() }) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.label_score)) },
            suffix = { Text("/ $maxScore") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.label_custom_lists_count, customLists.size), style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCount > 0) {
                    AssistChip(
                        onClick = { expanded = !expanded },
                        label = { Text(stringResource(R.string.label_custom_lists_selected, selectedCount)) },
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = stringResource(if (expanded) R.string.action_collapse else R.string.action_expand),
                )
            }
        }

        if (expanded) {
            customLists.forEach { (name, isSelected) ->
                Row(
                    modifier = Modifier
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
                Text(stringResource(R.string.action_create_new_list))
            }
        }
    }
}

@Composable
private fun NotesSection(notes: String, onNotesChange: (String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(stringResource(R.string.label_personal_notes), style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            label = { Text(stringResource(R.string.placeholder_personal_notes)) },
            maxLines = 5,
        )
    }
}


@Preview(showBackground = true, name = "MediaListEditor Content Preview")
@Composable
private fun MediaListEditorContentPreview() {
    val previewMedia = Media.Extended.empty().copy(
        title = MediaTitle("Attack on Titan", "Shingeki no Kyojin", "進撃の巨人", "Attack on Titan"),
        image = MediaImage("url_banner", "url_extra_large", "url_large", "url_medium", "#FFC107"),
        category = Media.Category.Anime(
            episodes = 87,
            duration = 24,
            broadcast = "Sundays at 00:10 (JST)",
            premiered = "Spring 2013",
            schedule = null,
        ),
        status = MediaStatus.RELEASING,
        score = MediaScore(88, 89, 9.5f, 100000, 5000),
        format = MediaFormat.TV,
        countryCode = "JP", description = "Centuries ago...",
        externalLinks = emptyList(), favourites = 12000, genres = emptyList(),
        twitterTag = "#shingeki", isRecommendationBlocked = false, isReviewBlocked = false,
        rankings = emptyList(), isLicensed = true, isLocked = false,
        siteUrl = SiteUrl("anilist.co/anime/123", "myanimelist.net/anime/123"),
        source = MediaSource.ANIME,
        synonyms = emptyList(),
        tags = emptyList(),
        season = MediaSeason.SUMMER,
        startDate = FuzzyDate(2013, 4, 7),
        endDate = FuzzyDate.empty(),
        trailer = MediaTrailer("trailer_id", "youtube", "thumbnail_url"),
        isAdult = false,
        isFavourite = false,
        isFavouriteBlocked = false,
        mediaList = MediaList.Core(
            id = 1L, mediaId = 123L, userId = 1L,
            status = MediaListStatus.CURRENT,
            score = 9f,
            progress = MediaListProgress.Anime(
                episodeProgress = 60,
                repeatedCount = 0,
            ),
            startedOn = FuzzyDate(2020, 1, 15),
            finishedOn = FuzzyDate.empty(),
            privacy = MediaListPrivacy(
                isPrivate = false,
                notes = "Best anime ever!",
                isHidden = false,
            ),
            customLists = listOf(MediaList.CustomList("Favorites", true), MediaList.CustomList("To Discuss", false)),
            advancedScores = emptyList(), priority = 0, createdOn = System.currentTimeMillis(),
        ),
        id = 1L,
    )
    val param = MediaListEditorRouter.MediaListEditorParam(
        mediaId = 123L, mediaType = MediaType.ANIME, scoreFormat = ScoreFormat.POINT_10,
    )
    val state = rememberMediaListEditorState(MutableLiveData(previewMedia), param)

    PreviewTheme(wrapInSurface = true) {
        MediaListEditorContent(
            state = state,
            onDismiss = {},
            onSave = {},
        )
    }
}

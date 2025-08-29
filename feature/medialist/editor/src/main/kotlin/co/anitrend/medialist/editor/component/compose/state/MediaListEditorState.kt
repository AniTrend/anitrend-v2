/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.medialist.editor.component.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.MediaListTaskRouter

@Stable
class MediaListEditorState(
    val media: Media,
    val scoreFormat: ScoreFormat,
    val dateHelper: AniTrendDateHelper,
) {
    var privateUpdate by mutableStateOf(false)
    var selectedStatus by mutableStateOf<MediaListStatus?>(null)
    var progressText by mutableStateOf("")
    var scoreText by mutableStateOf("")
    var notesText by mutableStateOf("")
    var customLists by mutableStateOf<Map<String, Boolean>>(emptyMap())

    // For DatePickers
    var showStartDatePicker by mutableStateOf(false)
    var showEndDatePicker by mutableStateOf(false)
    var selectedStartDate by mutableStateOf<FuzzyDate?>(null)
    var selectedEndDate by mutableStateOf<FuzzyDate?>(null)

    /**
     * Non-null views of the currently selected dates for convenience in UI bindings.
     */
    val startDate: FuzzyDate
        get() = selectedStartDate.orEmpty()

    val endDate: FuzzyDate
        get() = selectedEndDate.orEmpty()

    val mediaType: MediaType
        get() = media.category.type

    val maxScore: String = scoreFormat.base.toString()

    /**
     * Non-null views of the currently selected dates for convenience in UI bindings.
     */
    val startDateEpoch: Long?
        get() = selectedStartDate?.let(dateHelper::convertToUnixTimeStamp)

    val endDateEpoch: Long?
        get() = selectedEndDate?.let(dateHelper::convertToUnixTimeStamp)

    /**
     * Text representations of the currently selected dates, formatted via [AniTrendDateHelper].
     */
    val startDateText: String
        get() = dateHelper.convertToTextDate(selectedStartDate)?.toString() ?: ""

    val endDateText: String
        get() = dateHelper.convertToTextDate(selectedEndDate)?.toString() ?: ""

    val mediaTitle: String = media.title.userPreferred?.toString() ?: ""
    val totalUnits: Int? =
        when (val category = media.category) {
            is Media.Category.Anime -> category.episodes
            is Media.Category.Manga -> category.chapters
        }
    private val initialMediaList = media.mediaList as MediaList.Core?

    init {
        initialMediaList?.let { list ->
            privateUpdate = list.privacy.isPrivate
            selectedStatus = list.status
            progressText = list.progress.progress.toString()
            scoreText = list.score
                .takeIf { it > 0 }
                ?.toInt()
                ?.toString() ?: ""
            selectedStartDate = list.startedOn
            selectedEndDate = list.finishedOn
            notesText = list.privacy.notes?.toString() ?: ""
            customLists = list.customLists.associate { it.name.toString() to it.enabled }
        }
    }

    fun onStartDateSelected(millis: Long?) {
        selectedStartDate = millis?.let(dateHelper::convertToFuzzyDate)
        showStartDatePicker = false
    }

    fun onEndDateSelected(millis: Long?) {
        selectedEndDate = millis?.let(dateHelper::convertToFuzzyDate)
        showEndDatePicker = false
    }

    fun createSaveEntryParams(): MediaListTaskRouter.Param.SaveEntry =
        MediaListTaskRouter.Param.SaveEntry(
            id = initialMediaList?.id ?: 0L,
            mediaId = media.id,
            status = selectedStatus ?: MediaListStatus.PLANNING,
            score = scoreText.toFloatOrNull() ?: 0f,
            progress = initialMediaList?.progress?.progress,
            startedAt = selectedStartDate,
            completedAt = selectedEndDate,
            private = privateUpdate,
            customLists = customLists.filterValues { it }.keys.toList(),
            advancedScores = initialMediaList?.advancedScores?.map { it.score },
            priority = initialMediaList?.priority,
            scoreFormat = scoreFormat,
            scoreRaw = scoreText.toInt(),
            progressVolumes = initialMediaList?.progress?.progress,
            repeat = initialMediaList?.progress?.repeated,
            notes = notesText,
            hiddenFromStatusLists = privateUpdate,
        )

    fun createDeleteEntryParams(): MediaListTaskRouter.Param.DeleteEntry? = initialMediaList?.id?.let(MediaListTaskRouter.Param::DeleteEntry)
}

@Composable
fun rememberMediaListEditorState(
    media: Media,
    scoreFormat: ScoreFormat,
    dateHelper: AniTrendDateHelper,
): MediaListEditorState =
    remember(key1 = media, key2 = scoreFormat) {
        MediaListEditorState(
            media = media,
            scoreFormat = scoreFormat,
            dateHelper = dateHelper,
        )
    }

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

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.MediaListTaskRouter
import java.util.Locale
import kotlin.math.roundToInt

@Stable
class MediaListEditorState(
    val media: Media,
    val scoreFormat: ScoreFormat,
    val dateHelper: AniTrendDateHelper,
    private val nowProvider: () -> Long = { System.currentTimeMillis() },
) {
    var isPrivate by mutableStateOf(false)
    var selectedStatus by mutableStateOf<MediaListStatus?>(null)
    var progressText by mutableStateOf("")
    var scoreText by mutableStateOf("")
    var notesText by mutableStateOf("")

    // Manga volumes progress input as text
    var volumeProgressText by mutableStateOf("")

    // Repeat count input as text
    var repeatText by mutableStateOf("")

    // Advanced scores as a map of name -> text value, to preserve order and edits
    val advancedScoresText = mutableStateMapOf<String, String>()
    val customLists = mutableStateMapOf<String, Boolean>()

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
            is Media.Category.Anime -> category.episodes?.takeIf { it > 0 }
            is Media.Category.Manga -> category.chapters?.takeIf { it > 0 }
        }
    val totalVolumes: Int? =
        when (val category = media.category) {
            is Media.Category.Anime -> null
            is Media.Category.Manga -> category.volumes?.takeIf { it > 0 }
        }
    private val initialMediaList = media.mediaList as MediaList.Core?
    private val initialVolumeProgress: Int? =
        (initialMediaList?.progress as? MediaListProgress.Manga)?.let { it.volumeProgress }
    val currentProgress: Int
        get() = parseBoundedInt(progressText, totalUnits) ?: 0
    val currentVolumeProgress: Int
        get() = parseBoundedInt(volumeProgressText, totalVolumes) ?: 0
    val currentRepeatCount: Int
        get() = parseBoundedInt(repeatText, max = null) ?: 0
    val currentScore: Float?
        get() = parseScoreClamped()

    init {
        initialMediaList?.let { list ->
            isPrivate = list.privacy.isPrivate
            selectedStatus = list.status
            progressText = sanitizeWholeNumberInput(list.progress.progress.toString(), totalUnits)
            scoreText = formatInitialScore(list.score)
            selectedStartDate = list.startedOn
            selectedEndDate = list.finishedOn
            notesText = list.privacy.notes?.toString() ?: ""
            list.customLists.forEach { customLists[it.name.toString()] = it.enabled }
            when (val prog = list.progress) {
                is MediaListProgress.Anime -> {
                    repeatText = sanitizeWholeNumberInput(prog.repeated.toString(), max = null)
                }
                is MediaListProgress.Manga -> {
                    volumeProgressText = sanitizeWholeNumberInput(prog.volumeProgress.toString(), totalVolumes)
                    repeatText = sanitizeWholeNumberInput(prog.repeated.toString(), max = null)
                }
            }
            if (list.advancedScores.isNotEmpty()) {
                list.advancedScores.forEach { advancedScoresText[it.name] = formatAdvancedScore(it.score) }
            }
        }
    }

    /**
     * Applies business rules when the watch status changes.
     * - COMPLETED: set progress to total (if known) and end date to now
     * - CURRENT: set start date to now
     * - REPEATING: reset progress to 0
     */
    fun onStatusSelected(status: MediaListStatus) {
        selectedStatus = status
        val nowMillis = nowProvider()
        when (status) {
            MediaListStatus.COMPLETED -> {
                totalUnits?.let { total ->
                    progressText = total.toString()
                }
                selectedEndDate = dateHelper.convertToFuzzyDate(nowMillis)
            }
            MediaListStatus.CURRENT -> {
                selectedStartDate = dateHelper.convertToFuzzyDate(nowMillis)
            }
            MediaListStatus.REPEATING -> {
                progressText = "0"
                if (mediaType == MediaType.MANGA) {
                    volumeProgressText = "0"
                }
            }
            else -> Unit
        }
    }

    fun onStartDateSelected(millis: Long?) {
        selectedStartDate = millis?.let(dateHelper::convertToFuzzyDate)
        showStartDatePicker = false
    }

    fun onEndDateSelected(millis: Long?) {
        val computed = millis?.let(dateHelper::convertToFuzzyDate)
        // Guard: end date should not be before start date
        selectedEndDate =
            if (computed != null && selectedStartDate != null) {
                val startEpoch = dateHelper.convertToUnixTimeStamp(selectedStartDate!!)
                val endEpoch = dateHelper.convertToUnixTimeStamp(computed)
                if (endEpoch < startEpoch) selectedStartDate else computed
            } else {
                computed
            }
        showEndDatePicker = false
    }

    fun createSaveEntryParams(): MediaListTaskRouter.Param.SaveEntry =
        MediaListTaskRouter.Param.SaveEntry(
            id = initialMediaList?.id,
            mediaId = media.id,
            status = selectedStatus ?: MediaListStatus.PLANNING,
            score = parseScoreClamped(),
            progress = parseBoundedInt(progressText, totalUnits),
            startedAt = selectedStartDate,
            completedAt = selectedEndDate,
            private = isPrivate,
            customLists = customLists.filterValues { it }.keys.toList(),
            advancedScores = buildAdvancedScores(),
            priority = initialMediaList?.priority,
            scoreFormat = scoreFormat,
            scoreRaw = computeScoreRaw(),
            progressVolumes =
                when {
                    mediaType == MediaType.MANGA && selectedStatus == MediaListStatus.REPEATING -> 0
                    mediaType == MediaType.MANGA -> parseBoundedInt(volumeProgressText, totalVolumes) ?: initialVolumeProgress
                    else -> null
                },
            repeat = parseBoundedInt(repeatText, max = null) ?: initialMediaList?.progress?.repeated,
            notes = notesText,
            hiddenFromStatusLists = initialMediaList?.privacy?.isHidden,
        )

    @VisibleForTesting
    internal fun parseScoreClamped(): Float? {
        val raw = scoreText.toFloatOrNull() ?: return null
        val base =
            when (scoreFormat) {
                ScoreFormat.POINT_10, ScoreFormat.POINT_10_DECIMAL -> 10f
                ScoreFormat.POINT_100 -> 100f
                ScoreFormat.POINT_5 -> 5f
                ScoreFormat.POINT_3 -> 3f
            }
        return raw.coerceIn(0f, base)
    }

    @VisibleForTesting
    internal fun computeScoreRaw(): Int? {
        val clamped = parseScoreClamped() ?: return null
        val base =
            when (scoreFormat) {
                ScoreFormat.POINT_10, ScoreFormat.POINT_10_DECIMAL -> 10f
                ScoreFormat.POINT_100 -> 100f
                ScoreFormat.POINT_5 -> 5f
                ScoreFormat.POINT_3 -> 3f
            }
        val normalized = (clamped / base) * 100f
        return normalized.roundToInt().coerceIn(0, 100)
    }

    private fun buildAdvancedScores(): List<Float>? {
        if (advancedScoresText.isEmpty()) {
            return initialMediaList?.advancedScores?.map { it.score }
        }
        val initialOrder = initialMediaList?.advancedScores?.map { it.name } ?: advancedScoresText.keys.toList()
        return initialOrder.map { name ->
            advancedScoresText[name]?.toFloatOrNull() ?: 0f
        }
    }

    fun updateProgressText(value: String) {
        progressText = sanitizeWholeNumberInput(value, totalUnits)
    }

    fun updateScoreText(value: String) {
        scoreText = sanitizeScoreInput(value)
    }

    fun updateRepeatText(value: String) {
        repeatText = sanitizeWholeNumberInput(value, max = null)
    }

    fun updateVolumeProgressText(value: String) {
        volumeProgressText = sanitizeWholeNumberInput(value, totalVolumes)
    }

    fun toggleCustomList(
        name: String,
        enabled: Boolean,
    ) {
        customLists[name] = enabled
    }

    fun setAdvancedScore(
        name: String,
        value: String,
    ) {
        advancedScoresText[name] = sanitizeAdvancedScoreInput(value)
    }

    fun adjustProgress(delta: Int) {
        progressText = stepWholeNumber(progressText, delta, totalUnits)
    }

    fun adjustVolumeProgress(delta: Int) {
        volumeProgressText = stepWholeNumber(volumeProgressText, delta, totalVolumes)
    }

    fun adjustRepeat(delta: Int) {
        repeatText = stepWholeNumber(repeatText, delta, max = null)
    }

    fun adjustScore(delta: Int) {
        val updated =
            stepFormattedScore(
                currentText = scoreText,
                delta = delta,
                allowBlankAtZero = true,
            )
        scoreText = updated
    }

    fun clearScore() {
        scoreText = ""
    }

    fun setDiscreteScore(value: Int) {
        val clamped = value.coerceIn(0, scoreBase.roundToInt())
        scoreText =
            if (clamped == 0) {
                ""
            } else {
                clamped.toString()
            }
    }

    fun adjustAdvancedScore(
        name: String,
        delta: Int,
    ) {
        advancedScoresText[name] =
            stepFormattedScore(
                currentText = advancedScoresText[name].orEmpty(),
                delta = delta,
                allowBlankAtZero = false,
            )
    }

    fun createDeleteEntryParams(): MediaListTaskRouter.Param.DeleteEntry? = initialMediaList?.id?.let(MediaListTaskRouter.Param::DeleteEntry)

    private val scoreStep: Float
        get() = if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) 0.1f else 1f

    private val scoreBase: Float
        get() =
            when (scoreFormat) {
                ScoreFormat.POINT_10, ScoreFormat.POINT_10_DECIMAL -> 10f
                ScoreFormat.POINT_100 -> 100f
                ScoreFormat.POINT_5 -> 5f
                ScoreFormat.POINT_3 -> 3f
            }

    private fun formatInitialScore(score: Float): String {
        if (score <= 0f) {
            return ""
        }
        return formatScore(score.coerceIn(0f, scoreBase))
    }

    private fun sanitizeWholeNumberInput(
        value: String,
        max: Int?,
    ): String {
        val digitsOnly = value.filter(Char::isDigit)
        if (digitsOnly.isEmpty()) {
            return ""
        }
        val parsed = digitsOnly.toIntOrNull() ?: return digitsOnly
        val clamped = max?.let { parsed.coerceIn(0, it) } ?: parsed.coerceAtLeast(0)
        return clamped.toString()
    }

    private fun sanitizeScoreInput(value: String): String {
        val cleaned =
            when (scoreFormat) {
                ScoreFormat.POINT_10_DECIMAL ->
                    value
                        .replace(Regex("[^0-9.]"), "")
                        .trimStart('.')
                        .let { sanitized ->
                            val singleDot =
                                sanitized
                                    .replaceFirst("\\.".toRegex(), "#")
                                    .replace(".", "")
                                    .replace("#", ".")
                            val parts = singleDot.split('.')
                            val integer = parts.first().take(2)
                            val fractional = parts.getOrNull(1)?.take(1) ?: ""
                            if (fractional.isEmpty()) integer else "$integer.$fractional"
                        }
                else -> value.substringBefore('.').filter(Char::isDigit)
            }
        if (cleaned.isBlank()) {
            return ""
        }
        val parsed = cleaned.toFloatOrNull() ?: return cleaned
        return formatScore(parsed.coerceIn(0f, scoreBase))
    }

    private fun sanitizeAdvancedScoreInput(value: String): String {
        val cleaned =
            if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) {
                value
                    .replace(Regex("[^0-9.]"), "")
                    .trimStart('.')
                    .let { sanitized ->
                        val singleDot =
                            sanitized
                                .replaceFirst("\\.".toRegex(), "#")
                                .replace(".", "")
                                .replace("#", ".")
                        val parts = singleDot.split('.')
                        val integer = parts.first().take(2)
                        val fractional = parts.getOrNull(1)?.take(1) ?: ""
                        if (fractional.isEmpty()) integer else "$integer.$fractional"
                    }
            } else {
                value.substringBefore('.').filter(Char::isDigit)
            }
        if (cleaned.isBlank()) {
            return ""
        }
        val parsed = cleaned.toFloatOrNull() ?: return cleaned
        return formatAdvancedScore(parsed.coerceIn(0f, scoreBase))
    }

    private fun stepWholeNumber(
        currentText: String,
        delta: Int,
        max: Int?,
    ): String {
        val current = parseBoundedInt(currentText, max) ?: 0
        val stepped = (current + delta).coerceAtLeast(0)
        val clamped = max?.let { stepped.coerceAtMost(it) } ?: stepped
        return clamped.toString()
    }

    private fun stepFormattedScore(
        currentText: String,
        delta: Int,
        allowBlankAtZero: Boolean,
    ): String {
        val stepped =
            if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) {
                val currentTenths = ((currentText.toFloatOrNull() ?: 0f) * 10).roundToInt()
                val maxTenths = (scoreBase * 10).roundToInt()
                val nextTenths = (currentTenths + delta).coerceIn(0, maxTenths)
                nextTenths / 10f
            } else {
                val currentWhole = (currentText.toFloatOrNull() ?: 0f).roundToInt()
                val nextWhole = (currentWhole + delta).coerceIn(0, scoreBase.roundToInt())
                nextWhole.toFloat()
            }
        if (stepped <= 0f && allowBlankAtZero) {
            return ""
        }
        return formatScore(stepped)
    }

    private fun formatScore(value: Float): String =
        if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) {
            String.format(Locale.US, "%.1f", value)
        } else {
            value.roundToInt().toString()
        }

    private fun formatAdvancedScore(value: Float): String =
        if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) {
            String.format(Locale.US, "%.1f", value)
        } else {
            value.roundToInt().toString()
        }

    private fun parseBoundedInt(
        value: String,
        max: Int?,
    ): Int? {
        val parsed = value.toIntOrNull() ?: return null
        val clamped = max?.let { parsed.coerceIn(0, it) } ?: parsed.coerceAtLeast(0)
        return clamped
    }
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

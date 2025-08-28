package co.anitrend.medialist.editor.component.compose.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.navigation.MediaListEditorRouter
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Stable
class MediaListEditorState(
    media: Media?,
    val param: MediaListEditorRouter.MediaListEditorParam,
    val dateHelper: AniTrendDateHelper,
    val scope: CoroutineScope,
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

    val mediaTitle: String = media?.title?.userPreferred?.toString() ?: ""
    val totalUnits: Int? = when (val category = media?.category) {
        is Media.Category.Anime -> category.episodes
        is Media.Category.Manga -> category.chapters
        else -> null
    }
    private val initialMediaList: MediaList.Core? = media?.mediaList as? MediaList.Core

    init {
        initialMediaList?.let { list ->
            privateUpdate = list.privacy.isPrivate
            selectedStatus = list.status
            progressText = list.progress.progress.toString()
            scoreText = list.score.takeIf { it > 0 }?.toInt()?.toString() ?: ""
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

    fun buildMediaListCore(): MediaList.Core {
        return MediaList.Core(
            id = initialMediaList?.id ?: 0L,
            mediaId = param.mediaId,
            status = selectedStatus ?: MediaListStatus.PLANNING,
            score = scoreText.toFloatOrNull() ?: 0f,
            progress = when (param.mediaType) {
                MediaType.ANIME -> MediaListProgress.Anime(
                    episodeProgress = progressText.toIntOrNull() ?: 0,
                    repeatedCount = (initialMediaList?.progress as? MediaListProgress.Anime)?.repeatedCount ?: 0,
                )

                MediaType.MANGA -> MediaListProgress.Manga(
                    chapterProgress = progressText.toIntOrNull() ?: 0,
                    volumeProgress = (initialMediaList?.progress as? MediaListProgress.Manga)?.volumeProgress ?: 0,
                    repeatedCount = (initialMediaList?.progress as? MediaListProgress.Manga)?.repeatedCount ?: 0,
                )
            },
            startedOn = selectedStartDate.orEmpty(),
            finishedOn = selectedEndDate.orEmpty(),
            privacy = MediaListPrivacy(
                isPrivate = privateUpdate,
                notes = notesText.takeIf { it.isNotBlank() },
                isHidden = initialMediaList?.privacy?.isHidden ?: false,
            ),
            customLists = customLists.filterValues { it }.keys.map { MediaList.CustomList(it, true) },
            advancedScores = initialMediaList?.advancedScores ?: emptyList(),
            userId = initialMediaList?.userId ?: 0L,
            priority = initialMediaList?.priority,
            createdOn = initialMediaList?.createdOn,
        )
    }
}

@Composable
fun rememberMediaListEditorState(
    mediaData: LiveData<Media?>,
    param: MediaListEditorRouter.MediaListEditorParam,
    dateHelper: AniTrendDateHelper = koinInject<AniTrendDateHelper>(),
    scope: CoroutineScope = rememberCoroutineScope(),
): MediaListEditorState {
    val media by mediaData.observeAsState()
    // key on media so that if media object itself changes (e.g. different item loaded), state re-initializes
    return remember(media, param, scope) {
        MediaListEditorState(media, param, dateHelper, scope)
    }
}

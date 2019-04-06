package co.anitrend.core.model.group

import co.anitrend.core.repository.media.MediaListStatus

data class MediaListGroup(
    val entries: List<Any>,
    val name: String,
    val isCustomList: Boolean,
    val isSplitCompletedList: Boolean,
    @MediaListStatus
    val status: String
)
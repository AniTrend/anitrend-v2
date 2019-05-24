package co.anitrend.data.model.response.group

import co.anitrend.data.repository.medialist.MediaListStatus

/** [MediaListGroup](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistgroup.doc.html)
 * List group of anime or manga entries
 *
 * @param entries Media list entries
 * @param name name of the current group
 * @param isCustomList If the current group is a custom list
 * @param isSplitCompletedList If this grouping is split by types of of media, e.g movies, tv, specials, etc
 * @param status status of current group, one of [MediaListStatus]
 */
data class MediaListGroup(
    val entries: List<Any>,
    val name: String,
    val isCustomList: Boolean,
    val isSplitCompletedList: Boolean,
    @MediaListStatus
    val status: String
)
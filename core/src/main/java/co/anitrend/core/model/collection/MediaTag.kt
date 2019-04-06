package co.anitrend.core.model.collection

data class MediaTag(
    /** The id of the tag */
    val id: Int,
    /** The name of the tag */
    val name: String,
    /** A general description of the tag */
    val description: String?,
    /** The categories of tags this tag belongs to */
    val category: String?,
    /** The relevance ranking of the tag out of the 100 for this media */
    val rank: Int?,
    /** If the tag could be a spoiler for any media */
    val isGeneralSpoiler: Boolean?,
    /** If the tag is a spoiler for this media */
    val isMediaSpoiler: Boolean?,
    /** If the tag is only for adult 18+ media */
    val isAdult: Boolean?
)
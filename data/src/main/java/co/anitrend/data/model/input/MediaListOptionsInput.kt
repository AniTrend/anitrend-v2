package co.anitrend.data.model.input

import co.anitrend.data.model.contract.IGraphQuery

/** [MediaListOptionsInput](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistoptionsinput.doc.html)
 *
 * @param sectionOrder The order each list should be displayed in
 * @param splitCompletedSectionByFormat If the completed sections of the list should be separated by format
 * @param customLists The names of the user's custom lists
 * @param advancedScoring The names of the user's advanced scoring sections
 * @param advancedScoringEnabled If advanced scoring is enabled
 */
data class MediaListOptionsInput(
    val sectionOrder: List<String>? = null,
    val splitCompletedSectionByFormat: Boolean = false,
    val customLists: List<String>? = null,
    val advancedScoring: List<String>? = null,
    val advancedScoringEnabled: Boolean = false
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "sectionOrder" to sectionOrder,
        "splitCompletedSectionByFormat" to splitCompletedSectionByFormat,
        "customLists" to customLists,
        "advancedScoring" to advancedScoring,
        "advancedScoringEnabled" to advancedScoringEnabled
    )
}
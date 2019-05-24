package co.anitrend.data.model.response.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [MediaListTypeOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/medialisttypeoptions.doc.html)
 * A user's list options for anime or manga lists
 *
 * @param sectionOrder The order each list should be displayed in
 * @param splitCompletedSectionByFormat If the completed sections of the list should be separated by format
 * @param customLists The names of the user's custom lists
 * @param advancedScoring The names of the user's advanced scoring sections
 * @param advancedScoringEnabled If advanced scoring is enabled
 */
@Parcelize
data class MediaListTypeOptions(
    val sectionOrder: List<String>?,
    val splitCompletedSectionByFormat: Boolean,
    val customLists: List<String>?,
    val advancedScoring: List<String>?,
    val advancedScoringEnabled: Boolean
): Parcelable
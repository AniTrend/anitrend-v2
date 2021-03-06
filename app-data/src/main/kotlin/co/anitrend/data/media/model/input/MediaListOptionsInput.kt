/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.media.model.input

import co.anitrend.domain.common.graph.IGraphPayload
import kotlinx.android.parcel.Parcelize

/** [MediaListOptionsInput](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistoptionsinput.doc.html)
 *
 * @param sectionOrder The order each list should be displayed in
 * @param splitCompletedSectionByFormat If the completed sections of the list should be separated by format
 * @param customLists The names of the user's custom lists
 * @param advancedScoring The names of the user's advanced scoring sections
 * @param advancedScoringEnabled If advanced scoring is enabled
 */
@Parcelize
data class MediaListOptionsInput(
    val sectionOrder: List<String>? = null,
    val splitCompletedSectionByFormat: Boolean = false,
    val customLists: List<String>? = null,
    val advancedScoring: List<String>? = null,
    val advancedScoringEnabled: Boolean = false
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "sectionOrder" to sectionOrder,
        "splitCompletedSectionByFormat" to splitCompletedSectionByFormat,
        "customLists" to customLists,
        "advancedScoring" to advancedScoring,
        "advancedScoringEnabled" to advancedScoringEnabled
    )
}
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
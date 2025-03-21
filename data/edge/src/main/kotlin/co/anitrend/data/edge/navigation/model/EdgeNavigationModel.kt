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
package co.anitrend.data.edge.navigation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgeNavigationModel(
    @SerialName("criteria") val criteria: String,
    @SerialName("destination") val destination: String,
    @SerialName("group") val group: NavigationGroup,
    @SerialName("i18n") val i18n: String,
    @SerialName("icon") val icon: String,
) {
    @Serializable
    data class NavigationGroup(
        @SerialName("authenticated") val authenticated: Boolean,
        @SerialName("i18n") val i18n: String,
    )
}

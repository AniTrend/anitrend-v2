/*
 * Copyright (C) 2024 AniTrend
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
    @SerialName("id") val id: Long,
    @SerialName("destination") val destination: String,
    @SerialName("i18n") val label: String,
    @SerialName("icon") val icon: String,
    @SerialName("group") val group: Group,
) {
    @Serializable
    data class Group(
        @SerialName("authenticated") val authenticated: Boolean,
        @SerialName("i18n") val label: String,
        @SerialName("id") val id: Long,
    )
}

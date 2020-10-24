/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Name contract
 *
 * @property alternative Other names that might be referred to as (pen names)
 * @property first First name
 * @property full Full name
 * @property last Last name
 * @property native The full name in the native language
 */
@Serializable
internal data class SharedName(
    @SerialName("alternative") val alternative: List<String>?,
    @SerialName("first") val first: String?,
    @SerialName("full") val full: String?,
    @SerialName("last") val last: String?,
    @SerialName("native") val native: String?
)
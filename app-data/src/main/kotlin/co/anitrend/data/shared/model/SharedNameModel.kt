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
 * @property alternativeSpoiler Other names that might be referred to as but are spoilers
 * @property first First name
 * @property full Full name
 * @property last Last name
 * @property middle Middle name
 * @property native The full name in the native language
 * @property userPreferred The currently authenticated users preferred name language
 */
@Serializable
internal data class SharedNameModel(
    @SerialName("alternative") val alternative: List<String>? = null,
    @SerialName("alternativeSpoiler") val alternativeSpoiler: List<String>? = null,
    @SerialName("first") val first: String? = null,
    @SerialName("full") val full: String? = null,
    @SerialName("last") val last: String? = null,
    @SerialName("middle") val middle: String? = null,
    @SerialName("native") val native: String? = null,
    @SerialName("userPreferred") val userPreferred: String? = null
)
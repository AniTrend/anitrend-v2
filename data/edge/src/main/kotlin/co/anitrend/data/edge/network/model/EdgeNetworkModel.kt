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
package co.anitrend.data.edge.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network association information for a media item.
 *
 * Mirrors the original `SeriesNetworkType` formerly nested in `EdgeMediaModel`.
 * A media may have multiple networks (licensors / original networks / production, etc.).
 *
 * @param id Unique identifier of the network in the upstream provider (usually TMDB id).
 * @param name Display name of the network.
 * @param category Category or classification of the network (e.g. "network", "company").
 * @param isPrimary Whether this network is considered the primary/original airing network.
 * @param logoPath Optional relative/absolute path to the network logo image.
 * @param originCountry Two letter origin country code of the network.
 */
@Serializable
internal data class EdgeNetworkModel(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("isPrimary") val isPrimary: Boolean,
    @SerialName("logoPath") val logoPath: String? = null,
    @SerialName("originCountry") val originCountry: String,
)

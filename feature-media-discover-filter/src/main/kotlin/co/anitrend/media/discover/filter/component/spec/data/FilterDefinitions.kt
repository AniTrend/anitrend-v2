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
package co.anitrend.media.discover.filter.component.spec.data

import co.anitrend.domain.media.enums.MediaCountry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.media.discover.filter.component.spec.FilterDefinition
import co.anitrend.navigation.MediaDiscoverRouter.MediaDiscoverParam
import co.anitrend.navigation.model.sorting.Sorting

// Media Status (supports single or multi-selection)
internal val statusDefinition =
    FilterDefinition<MediaStatus, MediaDiscoverParam>(
        getSingleValue = { it.status },
        setSingleValue = { param, newValue -> param.copy(status = newValue) },
        getMultiValue = { it.status_in },
        setMultiValue = { param, newList -> param.copy(status_in = newList) },
        label = { it.alias.toString() },
    )

// Media Type (single-selection only)
internal val typeDefinition =
    FilterDefinition<MediaType, MediaDiscoverParam>(
        getSingleValue = { it.type },
        setSingleValue = { param, newValue -> param.copy(type = newValue) },
        getMultiValue = { null },
        setMultiValue = { param, _ -> param },
        label = { it.alias.toString() },
    )

// Media Season (single-selection only)
internal val seasonDefinition =
    FilterDefinition<MediaSeason, MediaDiscoverParam>(
        getSingleValue = { it.season },
        setSingleValue = { param, newValue -> param.copy(season = newValue) },
        getMultiValue = { null },
        setMultiValue = { param, _ -> param },
        label = { it.alias.toString() },
    )

// Media Format (supports single or multi-selection)
internal val formatDefinition =
    FilterDefinition<MediaFormat, MediaDiscoverParam>(
        getSingleValue = { it.format },
        setSingleValue = { param, newValue -> param.copy(format = newValue) },
        getMultiValue = { it.format_in },
        setMultiValue = { param, newList -> param.copy(format_in = newList) },
        label = { it.alias.toString() },
    )

// Media Source (supports single or multi-selection)
internal val sourceDefinition =
    FilterDefinition<MediaSource, MediaDiscoverParam>(
        getSingleValue = { it.source },
        setSingleValue = { param, newValue -> param.copy(source = newValue) },
        getMultiValue = { it.source_in },
        setMultiValue = { param, newList -> param.copy(source_in = newList) },
        label = { it.alias.toString() },
    )

// Media Licensor (supports single or multi-selection)
internal val licensedByDefinition =
    FilterDefinition<MediaLicensor, MediaDiscoverParam>(
        getSingleValue = { it.licensedBy },
        setSingleValue = { param, newValue -> param.copy(licensedBy = newValue) },
        getMultiValue = { it.licensedBy_in },
        setMultiValue = { param, newList -> param.copy(licensedBy_in = newList) },
        label = { it.title.toString() },
    )

// Country of Origin is stored as a CharSequence in the parameter,
// but we match it against MediaCountry entries.
internal val countryDefinition =
    FilterDefinition<MediaCountry, MediaDiscoverParam>(
        getSingleValue = { param ->
            MediaCountry.entries.find { it.name == param.countryOfOrigin }
        },
        setSingleValue = { param, newValue -> param.copy(countryOfOrigin = newValue?.alias) },
        getMultiValue = { null },
        setMultiValue = { param, _ -> param },
        label = { it.alias.toString() },
    )

// Season Year (an integer filter, single-selection only)
internal val seasonYearDefinition =
    FilterDefinition<Int, MediaDiscoverParam>(
        getSingleValue = { it.seasonYear },
        setSingleValue = { param, newValue -> param.copy(seasonYear = newValue) },
        getMultiValue = { null },
        setMultiValue = { param, _ -> param },
        label = { "Season Year: $it" },
    )

// Media Sort
internal val sortDefinition =
    FilterDefinition<Sorting<MediaSort>, MediaDiscoverParam>(
        getSingleValue = { null },
        setSingleValue = { param, _ -> param },
        getMultiValue = { it.sort },
        setMultiValue = { param, newList -> param.copy(sort = newList) },
        label = {
            "${it.sortable.alias} (${it.order.name})"
        },
    )

internal val filterDefinitions =
    listOf(
        statusDefinition,
        typeDefinition,
        seasonDefinition,
        formatDefinition,
        sourceDefinition,
        licensedByDefinition,
        countryDefinition,
        seasonYearDefinition,
        sortDefinition,
    )

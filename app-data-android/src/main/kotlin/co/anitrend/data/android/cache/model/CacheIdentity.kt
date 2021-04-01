/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.android.cache.model

import co.anitrend.data.android.cache.helper.instantInPast
import org.threeten.bp.Instant

/**
 * Identifier for caching resources
 *
 * @property id Unique identifier for the resource, typically could be a model id a network request
 * @property key Unique key for the identifier, this can be considered as a grouping key for the [id]
 * @property expiresAt Time duration which the cached item should be valid for. Default is 3 hours in the past from now
 */
interface CacheIdentity {
    val id: Long
    val key: String
    val expiresAt: Instant
        get() = instantInPast(hours = 3)
}
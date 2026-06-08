/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.staff.cache

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.helper.CanonicalCacheIdentity
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import co.anitrend.domain.staff.model.StaffParam
import org.threeten.bp.Instant

internal class StaffCache(
    override val localSource: CacheLocalSource,
    override val request: CacheRequest = CacheRequest.STAFF,
) : CacheStorePolicy() {
    override suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant,
    ): Boolean = isRequestBefore(identity, expiresAfter)

    sealed class Identity : CacheIdentity {
        class Paged(
            val param: StaffParam.Paged? = null,
            override val id: Long = param.cacheIdentityValue(),
            override val key: String = "staff_paged",
        ) : Identity()
    }
}

private fun StaffParam.Paged?.cacheIdentityValue(): Long =
    this
        ?.toCanonicalKey()
        ?.let(CanonicalCacheIdentity::idFromCanonicalKey)
        ?: 0L

private fun StaffParam.Paged.toCanonicalKey(): String {
    val entries =
        buildMap<String, String> {
            id_in
                ?.sorted()
                ?.takeIf(List<Long>::isNotEmpty)
                ?.let { put("id_in", it.joinToString(",")) }
            id_not?.let { put("id_not", it.toString()) }
            id_not_in
                ?.sorted()
                ?.takeIf(List<Long>::isNotEmpty)
                ?.let { put("id_not_in", it.joinToString(",")) }
            search
                ?.trim()
                ?.lowercase()
                ?.takeIf(String::isNotBlank)
                ?.let { put("search", it) }
            sort
                ?.map { it.name }
                ?.takeIf(List<String>::isNotEmpty)
                ?.let { put("sort", it.joinToString(",")) }
            isBirthday?.let { put("is_birthday", it.toString()) }
        }

    return entries
        .toSortedMap()
        .entries
        .joinToString("|") { (key, value) -> "$key=$value" }
}

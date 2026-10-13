/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.edge.navigation.mapper

import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.data.edge.navigation.converters.EdgeNavigationModelConverter
import co.anitrend.data.edge.navigation.datasource.EdgeNavigationLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EdgeNavigationMapperTest {
    @Test
    fun repeatedConfigRefreshReplacesNavigationEntries() =
        runTest {
            val localSource = InMemoryEdgeNavigationLocalSource()
            val mapper =
                EdgeNavigationMapper(
                    localSource = localSource,
                    converter = EdgeNavigationModelConverter(),
                )

            mapper.onEmbedded(listOf(navigation("home"), navigation("discover"), navigation("home")))
            mapper.persistEmbedded()
            assertEquals(2, localSource.entries.size)

            mapper.onEmbedded(listOf(navigation("discover"), navigation("airing"), navigation("about"), navigation("discover")))
            mapper.persistEmbedded()

            assertEquals(3, localSource.entries.size)
            assertEquals(setOf("discover", "airing", "about"), localSource.entries.map(EdgeNavigationEntity::key).toSet())
        }

    private fun navigation(key: String) =
        GetConfigData.ConfigNavigation(
            criteria = key,
            destination = key,
            group =
                GetConfigData.ConfigNavigationGroup(
                    authenticated = false,
                    i18n = "navigation.group.guest",
                ),
            i18n = "navigation.$key",
            icon = "ic_$key",
            key = key,
        )

    private class InMemoryEdgeNavigationLocalSource : EdgeNavigationLocalSource() {
        val entries = mutableListOf<EdgeNavigationEntity>()
        private var nextId = 1L

        override suspend fun count(): Int = entries.size

        override suspend fun clear() {
            entries.clear()
        }

        override suspend fun clearByConfigId(configId: Long) {
            entries.removeAll { it.configId == configId }
        }

        override suspend fun insertOrReplaceByKey(entities: List<EdgeNavigationEntity>) {
            entities.forEach { entity ->
                val conflictIndex =
                    entries.indexOfFirst { existing ->
                        existing.configId == entity.configId && existing.key == entity.key
                    }
                if (conflictIndex >= 0) {
                    entries.removeAt(conflictIndex)
                }
                append(entity)
            }
        }

        override suspend fun insert(attribute: EdgeNavigationEntity): Long = append(attribute)

        override suspend fun insert(attribute: List<EdgeNavigationEntity>): List<Long> =
            attribute.map(::append)

        override suspend fun update(attribute: EdgeNavigationEntity) {
            replace(attribute)
        }

        override suspend fun update(attribute: List<EdgeNavigationEntity>) {
            attribute.forEach(::replace)
        }

        override suspend fun delete(attribute: EdgeNavigationEntity) {
            entries.removeAll { it.id == attribute.id }
        }

        override suspend fun delete(attribute: List<EdgeNavigationEntity>) {
            val ids = attribute.map(EdgeNavigationEntity::id).toSet()
            entries.removeAll { it.id in ids }
        }

        override suspend fun upsert(attribute: EdgeNavigationEntity) {
            upsert(listOf(attribute))
        }

        override suspend fun upsert(attribute: List<EdgeNavigationEntity>) {
            attribute.forEach { entity ->
                if (entity.id == 0L) {
                    append(entity)
                } else {
                    replace(entity)
                }
            }
        }

        private fun append(entity: EdgeNavigationEntity): Long {
            val id = nextId++
            entries += entity.copy(id = id)
            return id
        }

        private fun replace(entity: EdgeNavigationEntity) {
            val index = entries.indexOfFirst { it.id == entity.id }
            if (index < 0) {
                entries += entity.copy(id = nextId++)
            } else {
                entries[index] = entity
            }
        }
    }
}

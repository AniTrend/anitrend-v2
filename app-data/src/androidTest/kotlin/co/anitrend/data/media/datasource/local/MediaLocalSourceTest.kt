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

package co.anitrend.data.media.datasource.local

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.cash.turbine.test
import co.anitrend.data.core.CoreTestSuite
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.media.model.container.MediaModelContainer
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class MediaLocalSourceTest : CoreTestSuite() {

    private val pageModel: GraphQLResponse<MediaModelContainer.Paged>? by lazy {
        "media_paged_response.json".load()
    }

    private suspend fun populateDatabase(model: MediaModelContainer.Paged) {
        val mapper = get<MediaMapper.Paged>()
        val entities = mapper.onResponseMapFrom(model)
        assertEquals("", model.page.media.size, entities.size)
        mapper.onResponseDatabaseInsert(entities)
    }

    @Before
    fun setUp() {
        assertNotNull(pageModel?.data)
        runTest(dispatchers.io) {
            populateDatabase(pageModel!!.data!!)
        }
    }

    @Test
    fun getMediaById() = runTest(dispatchers.io) {
        val id: Long = 16498
        val title = MediaEntity.Title(
            english = "Attack on Titan",
            original = "進撃の巨人",
            romaji = "Shingeki no Kyojin",
            userPreferred = "Shingeki no Kyojin"
        )

        val entityFlow = store.mediaDao().mediaByIdFlow(id)

        entityFlow.test {
            val entity = expectMostRecentItem()
            // for some reason tests clear the native field
            assertEquals("", title, entity?.media?.title)

            val total = store.mediaDao().count()
            assertEquals("", 50, total)
        }
    }

    @Test
    fun getMediaWithAiringById() = runTest(dispatchers.io) {
        val id: Long = 5114
        val title = MediaEntity.Title(
            english = "Fullmetal Alchemist: Brotherhood",
            original = "鋼の錬金術師 FULLMETAL ALCHEMIST",
            romaji = "Hagane no Renkinjutsushi: Fullmetal Alchemist",
            userPreferred = "Hagane no Renkinjutsushi: Fullmetal Alchemist"
        )

        val entityFlow = store.mediaDao().mediaByIdFlow(id)

        entityFlow.test {
            val entity = expectMostRecentItem()
            // for some reason tests clear the native field
            assertEquals("", title, entity?.media?.title)

            assertNotNull(entity?.nextAiring)
        }
    }
}

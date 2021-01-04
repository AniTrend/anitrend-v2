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
import co.anitrend.data.api.model.GraphQLResponse
import co.anitrend.data.core.CoreTestSuite
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.paged.MediaPagedCombinedMapper
import co.anitrend.data.media.model.page.MediaPageModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class MediaLocalSourceTest : CoreTestSuite() {

    private val pageModel: GraphQLResponse<MediaPageModel>? by lazy {
        "media_paged_response.json".load()
    }

    private suspend fun populateDatabase(model: MediaPageModel) {
        val mapper = get<MediaPagedCombinedMapper>()
        val entities = mapper.onResponseMapFrom(model)
        assertEquals(model.page.media.size, entities.size)
        mapper.onResponseDatabaseInsert(entities)
    }

    @Before
    fun setUp() {
        assertNotNull(pageModel?.data)
        runBlocking(dispatchers.io) {
            populateDatabase(pageModel!!.data!!)
        }
    }

    @Test
    fun getMediaById() = runBlocking(dispatchers.io) {
        val id: Long = 16498
        val title = MediaEntity.Title(
            english = "Attack on Titan",
            native = "進撃の巨人",
            romaji = "Shingeki no Kyojin",
            userPreferred = "Shingeki no Kyojin"
        )

        val entity = store.mediaDao().mediaById(id)
        // for some reason tests clear the native field
        assertEquals(title, entity?.media?.title)

        val total = store.mediaDao().count()
        assertEquals(50, total)
    }

    @Test
    fun getMediaWithAiringById() = runBlocking(dispatchers.io) {
        val id: Long = 5114
        val title = MediaEntity.Title(
            english = "Fullmetal Alchemist: Brotherhood",
            native = "鋼の錬金術師 FULLMETAL ALCHEMIST",
            romaji = "Hagane no Renkinjutsushi: Fullmetal Alchemist",
            userPreferred = "Hagane no Renkinjutsushi: Fullmetal Alchemist"
        )

        val entity = store.mediaDao().mediaById(id)
        // for some reason tests clear the native field
        assertEquals(title, entity?.media?.title)

        assertNotNull(entity?.nextAiring)
    }
}
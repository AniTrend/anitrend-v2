/*
 * Copyright (C) 2020 AniTrend
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
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.Test
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
        runTest {
            populateDatabase(pageModel!!.data!!)
        }
    }

    @Test
    fun getMediaById() =
        runTest {
            val id: Long = 16498
            val title =
                MediaEntity.Title(
                    english = "Attack on Titan",
                    original = "進撃の巨人",
                    romaji = "Shingeki no Kyojin",
                    userPreferred = "Shingeki no Kyojin",
                )

            val entityFlow = store.mediaDao().mediaByIdFlow(id)

            entityFlow.test {
                val entity = awaitItem()
                // for some reason tests clear the native field
                assertEquals("", title, entity?.media?.title)

                val total = store.mediaDao().count()
                assertEquals("", 50, total)
            }
        }

    @Test
    fun getMediaWithAiringById() =
        runTest {
            val id = 21L
            val entityFlow = store.mediaDao().mediaByIdFlow(id)

            entityFlow.test {
                val entity = awaitItem()
                assertEquals("", id, entity?.media?.id)
                assertNotNull(entity?.nextAiring)
            }
        }

    @Test
    fun carouselAiringSoonQueryFindsMediaFromEmbeddedSchedule() =
        runTest {
            store.airingScheduleDao().clear()
            store.mediaDao().clear()

            val mediaModels = pageModel!!.data!!.page.media
            val mapper = get<MediaMapper.EmbedWithMediaList>()
            val entities = mapper.onResponseMapFrom(mediaModels)
            mapper.onResponseDatabaseInsert(entities)

            val mediaIds =
                store
                    .carouselDao()
                    .airingSoonFlow(
                        mediaType = MediaType.ANIME,
                        pageSize = mediaModels.size,
                        currentTime = 0L,
                    ).first()
                    .map { it.media.id }

            assertEquals("", listOf(21L), mediaIds)
        }
}

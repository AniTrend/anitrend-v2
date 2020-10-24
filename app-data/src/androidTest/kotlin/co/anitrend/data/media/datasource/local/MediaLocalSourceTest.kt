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
        "media_paged_response.json".load(
            GraphQLResponse.serializer(
                MediaPageModel.serializer()
            )
        )
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
        val id: Long = 112301
        val title = MediaEntity.Title(
            english = "The Misfit of Demon King Academy: History’s Strongest Demon King Reincarnates and Goes to School with His Descendants",
            native = "魔王学院の不適合者 ～史上最強の魔王の始祖、転生して子孫たちの学校へ通う～",
            romaji = "Maou Gakuin no Futekigousha: Shijou Saikyou no Maou no Shiso, Tensei shite Shison-tachi no Gakkou e Kayou",
            userPreferred = "Maou Gakuin no Futekigousha: Shijou Saikyou no Maou no Shiso, Tensei shite Shison-tachi no Gakkou e Kayou"
        )

        val entity = store.mediaDao().mediaById(id)
        // for some reason tests clear the native field
        assertEquals(title, entity?.title)

        val total = store.mediaDao().count()
        assertEquals(50, total)
    }

    @Test
    fun getMediaWithAiringById() = runBlocking(dispatchers.io) {
        val id: Long = 21
        val title = MediaEntity.Title(
            english = "One Piece",
            native = "ワンピース",
            romaji = "One Piece",
            userPreferred = "One Piece"
        )

        val entity = store.mediaDao().mediaByIdWithAiring(id)
        // for some reason tests clear the native field
        assertEquals(title, entity?.media?.title)

        assertNotNull(entity?.nextAiring)
    }
}
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
package co.anitrend.data.user.koin

import androidx.room.RoomDatabase
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.user.mapper.UserProfileFeedMapper
import co.anitrend.data.user.mapper.UserProfileFeedWriterContract
import co.anitrend.data.user.mapper.UserProfileOverviewMapper
import co.anitrend.data.user.mapper.UserProfileOverviewWriterContract
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class UserModulesTest {
    @Test
    fun userProfileOverviewMapperResolvesFromUserModules() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.userProfileFavouriteMediaDao() } returns mockk(relaxed = true)
        every { store.statusDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                    // transaction() needs a RoomDatabase even in the narrow plain-unit Koin graph.
                    single<RoomDatabase> { mockk(relaxed = true) }
                    factoryOf(::mockMediaEmbed)
                },
                userModules,
            )
        }

        assertNotNull(getKoin().get<UserProfileOverviewWriterContract>())
        assertNotNull(getKoin().get<UserProfileOverviewMapper>())
        stopKoin()
    }

    @Test
    fun userProfileFeedMapperResolvesFromUserModules() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.userProfileFavouriteMediaDao() } returns mockk(relaxed = true)
        every { store.userProfileReviewDao() } returns mockk(relaxed = true)
        every { store.reviewDao() } returns mockk(relaxed = true)
        every { store.statusDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                    // transaction() and the feed writer path both require these local data sources.
                    single<RoomDatabase> { mockk(relaxed = true) }
                    factoryOf(::mockMediaEmbed)
                },
                userModules,
            )
        }

        assertNotNull(getKoin().get<UserProfileFeedWriterContract>())
        assertNotNull(getKoin().get<UserProfileFeedMapper>())
        stopKoin()
    }
}

private fun mockMediaEmbed(): MediaMapper.Embed = mockk(relaxed = true)

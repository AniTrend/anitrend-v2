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
package co.anitrend.data.medialist.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.customlist.mapper.CustomListMapper
import co.anitrend.data.customscore.mapper.CustomScoreMapper
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.medialist.mapper.MediaListWriterContract
import co.anitrend.data.user.mapper.UserMapper
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class MediaListModulesTest {
    @Test
    fun mediaListModulesResolveEntryWriterPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.mediaListDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                    factoryOf(::mockCustomListMapper)
                    factoryOf(::mockCustomScoreMapper)
                    factoryOf(::mockMediaEmbedWithAiring)
                    factoryOf(::mockUserEmbed)
                },
                mediaListModules,
            )
        }

        assertNotNull(getKoin().get<MediaListWriterContract>())
        stopKoin()
    }
}

private fun mockCustomListMapper(): CustomListMapper = mockk(relaxed = true)

private fun mockCustomScoreMapper(): CustomScoreMapper = mockk(relaxed = true)

private fun mockMediaEmbedWithAiring(): MediaMapper.EmbedWithAiring = mockk(relaxed = true)

private fun mockUserEmbed(): UserMapper.Embed = mockk(relaxed = true)

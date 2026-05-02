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
package co.anitrend.data.media.koin

import co.anitrend.data.airing.mapper.AiringMapper
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.genre.mapper.GenreMapper
import co.anitrend.data.link.mapper.LinkMapper
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.medialist.mapper.MediaListMapper
import co.anitrend.data.rank.mapper.RankMapper
import co.anitrend.data.tag.mapper.TagMapper
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class MediaModulesTest {
    @Test
    fun mediaModulesResolveDetailMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.mediaDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                    factoryOf(::mockAiringEmbed)
                    factoryOf(::mockGenreEmbed)
                    factoryOf(::mockLinkEmbed)
                    factoryOf(::mockMediaListEmbed)
                    factoryOf(::mockRankEmbed)
                    factoryOf(::mockTagEmbed)
                },
                mediaModules,
            )
        }

        assertNotNull(getKoin().get<MediaMapper.Detail>())
        stopKoin()
    }
}

private fun mockAiringEmbed(): AiringMapper.Embed = mockk(relaxed = true)

private fun mockGenreEmbed(): GenreMapper.Embed = mockk(relaxed = true)

private fun mockLinkEmbed(): LinkMapper.Embed = mockk(relaxed = true)

private fun mockMediaListEmbed(): MediaListMapper.Embed = mockk(relaxed = true)

private fun mockRankEmbed(): RankMapper.Embed = mockk(relaxed = true)

private fun mockTagEmbed(): TagMapper.Embed = mockk(relaxed = true)

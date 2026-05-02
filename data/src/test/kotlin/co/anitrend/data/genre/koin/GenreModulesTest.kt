package co.anitrend.data.genre.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.genre.mapper.GenreMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class GenreModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun genreModulesResolveEmbedMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.genreConnectionDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testGenreModule(store),
                genreModules,
            )
        }

        assertNotNull(getKoin().get<GenreMapper.Embed>())
    }
}

private fun testGenreModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

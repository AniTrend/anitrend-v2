package co.anitrend.data.rank.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.rank.mapper.RankMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class RankModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun rankModulesResolveEmbedMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.rankDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testRankModule(store),
                rankModules,
            )
        }

        assertNotNull(getKoin().get<RankMapper.Embed>())
    }
}

private fun testRankModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

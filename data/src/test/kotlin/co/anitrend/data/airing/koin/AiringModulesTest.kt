package co.anitrend.data.airing.koin

import co.anitrend.data.airing.mapper.AiringMapper
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.media.mapper.MediaMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class AiringModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun airingModulesResolvePagedMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.airingScheduleDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testAiringModule(store),
                airingModules,
            )
        }

        assertNotNull(getKoin().get<AiringMapper.Paged>())
    }
}

private fun testAiringModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
        factory { mockk<MediaMapper.Embed>(relaxed = true) }
    }

package co.anitrend.data.recommendation.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.recommendation.mapper.MediaRecommendationMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class RecommendationModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun recommendationModulesResolveMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.mediaRecommendationConnectionDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testRecommendationModule(store),
                *recommendationModules.toTypedArray(),
            )
        }

        assertNotNull(getKoin().get<MediaRecommendationMapper>())
    }
}

private fun testRecommendationModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
        single<MediaConverter> { mockk(relaxed = true) }
    }

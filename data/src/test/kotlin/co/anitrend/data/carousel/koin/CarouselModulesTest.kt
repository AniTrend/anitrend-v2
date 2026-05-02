package co.anitrend.data.carousel.koin

import co.anitrend.data.carousel.mapper.CarouselMapper
import co.anitrend.data.media.mapper.MediaMapper
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class CarouselModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun carouselModulesResolveMapperPath() {
        stopKoin()

        startKoin {
            modules(
                testCarouselModule,
                carouselModules,
            )
        }

        assertNotNull(getKoin().get<CarouselMapper>())
    }
}

private val testCarouselModule =
    module {
        factory { mockk<MediaMapper.EmbedWithMediaList>(relaxed = true) }
    }

package co.anitrend.data.studio.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.studio.mapper.MediaStudioMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class StudioModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun studioModulesResolveMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.mediaStudioConnectionDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testStudioModule(store),
                *studioModules.toTypedArray(),
            )
        }

        assertNotNull(getKoin().get<MediaStudioMapper>())
    }
}

private fun testStudioModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

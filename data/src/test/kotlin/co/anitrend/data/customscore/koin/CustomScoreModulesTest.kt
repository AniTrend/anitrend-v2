package co.anitrend.data.customscore.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.customscore.mapper.CustomScoreMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class CustomScoreModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun customScoreModulesResolveMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.customScoreDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testCustomScoreModule(store),
                customScoreModules,
            )
        }

        assertNotNull(getKoin().get<CustomScoreMapper>())
    }
}

private fun testCustomScoreModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

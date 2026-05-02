package co.anitrend.data.customlist.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.customlist.mapper.CustomListMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class CustomListModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun customListModulesResolveMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.customListDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testCustomListModule(store),
                customListModules,
            )
        }

        assertNotNull(getKoin().get<CustomListMapper>())
    }
}

private fun testCustomListModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

package co.anitrend.data.link.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.link.mapper.LinkMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class LinkModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun linkModulesResolveEmbedMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.linkDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testLinkModule(store),
                linkModules,
            )
        }

        assertNotNull(getKoin().get<LinkMapper.Embed>())
    }
}

private fun testLinkModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

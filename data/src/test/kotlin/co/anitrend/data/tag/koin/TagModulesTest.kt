package co.anitrend.data.tag.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.tag.mapper.TagMapper
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class TagModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun tagModulesResolveEmbedMapperPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.tagConnectionDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                testTagModule(store),
                tagModules,
            )
        }

        assertNotNull(getKoin().get<TagMapper.Embed>())
    }
}

private fun testTagModule(store: IAniTrendStore) =
    module {
        single<IAniTrendStore> { store }
    }

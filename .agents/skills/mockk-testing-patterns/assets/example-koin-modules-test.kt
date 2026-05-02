package co.anitrend.example.testing

import io.mockk.mockk
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.test.assertNotNull
import kotlin.test.Test

private interface AnalyticsLogger

private class EpisodePresenter(
    private val analyticsLogger: AnalyticsLogger,
)

private val episodeModule: Module = module {
    factory { EpisodePresenter(get()) }
}

class EpisodeModuleTest : KoinTest {
    @get:Rule
    val mockProviderRule = MockProviderRule.create { mockk() }

    @Test
    fun `koin module resolves graph`() {
        checkModules {
            modules(episodeModule)
        }
    }

    @Test
    fun `koin module resolves concrete contract from narrow module set`() {
        stopKoin()

        startKoin {
            modules(episodeModule)
        }

        assertNotNull(getKoin().get<EpisodePresenter>())
        stopKoin()
    }
}

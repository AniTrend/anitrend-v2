package co.anitrend.studio.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.navigation.StudioRouter
import co.anitrend.studio.component.viewmodel.StudioViewModel
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class StudioFeatureModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun studioFeatureModuleResolvesStudioDetailBindings() {
        stopKoin()

        startKoin {
            modules(
                module {
                    single<StudioDetailInteractor> { mockk(relaxed = true) }
                },
            )
        }
        moduleHelper.loadModules()

        assertNotNull(getKoin().get<StudioRouter.Provider>())
        assertNotNull(getKoin().get<StudioViewModel>())
    }
}

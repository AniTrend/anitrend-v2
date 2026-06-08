package co.anitrend.staff.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper.Companion.loadModules
import co.anitrend.navigation.StaffRouter
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class StaffFeatureModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun staffFeatureModuleResolvesStaffRouterProvider() {
        stopKoin()

        startKoin {
            modules(emptyList())
        }
        moduleHelper.loadModules()

        assertNotNull(getKoin().get<StaffRouter.Provider>())
    }
}

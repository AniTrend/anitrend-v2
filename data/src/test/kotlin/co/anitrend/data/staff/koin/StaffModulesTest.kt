package co.anitrend.data.staff.koin

import co.anitrend.data.staff.converter.StaffConverter
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class StaffModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun staffModulesResolveConverterPath() {
        stopKoin()

        startKoin {
            modules(*staffModules.toTypedArray())
        }

        assertNotNull(getKoin().get<StaffConverter>())
    }
}

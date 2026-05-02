/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.data.android.koin

import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.core.device.IDeviceInfo
import co.anitrend.data.user.settings.IUserSettings
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.dsl.module

class DataModulesResolutionTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun dataModulesResolveAuthenticationBootstrapContractsInPlainUnitKoin() {
        startKoin {
            androidContext(mockk(relaxed = true))
            modules(
                dataModules,
                testDataRootModule,
            )
        }

        assertNotNull(getKoin().get<IAuthenticationHelper>())
    }
}

private val testDataRootModule =
    module {
        // Supply only the runtime collaborators needed to boot auth-related data graph bindings.
        single<IDeviceInfo> { mockk(relaxed = true) }
        single<IUserSettings> { mockk(relaxed = true) }
        single<IAuthenticationSettings> { mockk(relaxed = true) }
    }

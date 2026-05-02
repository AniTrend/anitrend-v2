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
package co.anitrend.core.koin

import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.data.android.network.model.NetworkMessage
import co.anitrend.data.core.device.IDeviceInfo
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.dsl.module

class CoreModulesResolutionTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun coreModulesResolveStartupGraph() {
        startKoin {
            androidContext(mockk(relaxed = true))
            modules(
                coreModules,
                testCoreRootModule,
            )
        }

        assertNotNull(getKoin().get<IStateLayoutConfig>())
        assertNotNull(getKoin().get<NetworkMessage>())
    }
}

private val testCoreRootModule =
    module {
        single<IDeviceInfo> { mockk(relaxed = true) }
    }

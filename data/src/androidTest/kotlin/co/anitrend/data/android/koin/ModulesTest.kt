/*
 * Copyright (C) 2022 AniTrend
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

import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.chuckerteam.chucker.api.ChuckerInterceptor
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.settings.refresh.IRefreshBehaviourSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.data.testModules
import co.anitrend.data.android.network.model.NetworkMessage
import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule

class ModulesTest : KoinTest {
    @get:Rule
    val mockProviderRule = MockProviderRule.create { mockk() }

    @After
    fun clearGlobalKoin() {
        stopKoin()
    }

    @Test
    fun verifyKoinDependencyGraph() {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        val context =
            object : ContextWrapper(instrumentationContext) {
                override fun getApplicationInfo(): ApplicationInfo =
                    super.getApplicationInfo().apply {
                        nonLocalizedLabel = "AniTrend test"
                    }
            }
        checkModules(
            parameters = {
                withParameter<OkHttpClient.Builder> { HttpLoggingInterceptor.Level.BODY }
                withParameter<ChuckerInterceptor> { emptySet<String>() }
            },
        ) {
            allowOverride(true)
            androidContext(context)
            modules(
                testModules +
                    module {
                        single<IAuthenticationSettings> { mockk(relaxed = true) }
                        single<IUserSettings> { mockk(relaxed = true) }
                        single<IRefreshBehaviourSettings> { mockk(relaxed = true) }
                        single {
                            NetworkMessage(
                                unrecoverableErrorTittle = "Unrecoverable",
                                connectivityErrorTittle = "Connectivity",
                                connectivityErrorMessage = "Network unavailable",
                            )
                        }
                    },
            )
        }
    }
}

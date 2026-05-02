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
package co.anitrend.data.auth.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.auth.mapper.AuthenticatedUserWriterContract
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.mapper.UserMapper
import co.anitrend.data.user.settings.IUserSettings
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class AuthModulesTest {
    @Test
    fun authenticatedUserWriterResolvesFromAuthModules() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.userDao() } returns mockk<UserLocalSource>(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                    single<IUserSettings> { mockk(relaxed = true) }
                    factoryOf(::mockGeneralOptionEmbed)
                    factoryOf(::mockMediaOptionEmbed)
                    factoryOf(::mockNotificationEmbed)
                },
                authModules,
            )
        }

        assertNotNull(org.koin.java.KoinJavaComponent.getKoin().get<AuthenticatedUserWriterContract>())
        stopKoin()
    }
}

private fun mockGeneralOptionEmbed(): UserMapper.GeneralOptionEmbed = mockk(relaxed = true)

private fun mockMediaOptionEmbed(): UserMapper.MediaOptionEmbed = mockk(relaxed = true)

private fun mockNotificationEmbed(): UserMapper.NotificationEmbed = mockk(relaxed = true)

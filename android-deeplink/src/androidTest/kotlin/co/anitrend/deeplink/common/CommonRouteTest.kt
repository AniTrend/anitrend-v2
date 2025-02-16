/*
 * Copyright (C) 2022  AniTrend
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
package co.anitrend.deeplink.common

import android.app.Instrumentation
import android.content.Intent
import androidx.core.net.toUri
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.deeplink.component.screen.DeepLinkScreen
import co.anitrend.deeplink.environment.AniTrendEnvironment
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import kotlin.test.BeforeTest

abstract class CommonRouteTest {
    private val basePackage = "co.anitrend"

    @get:Rule
    protected val rule = activityScenarioRule<DeepLinkScreen>()

    protected val instrumentation: Instrumentation by lazy {
        InstrumentationRegistry.getInstrumentation()
    }

    protected val settings = mockk<IUserSettings>()

    protected val environment = mockk<AniTrendEnvironment>()

    @BeforeTest
    fun setUp() {
        every { settings.authenticatedUserId.value } returns IAuthenticationSettings.INVALID_USER_ID
        every { settings.isAuthenticated.value } returns false
        every { environment.settings } returns settings
        every { environment.isAuthenticated } returns false
        every { environment.context } returns instrumentation.context
    }

    protected fun String.toIntent(): Intent {
        val fullyQualifiedPatched = "$basePackage.$this"
        return intentOf {
            action = Intent.ACTION_VIEW
            data = fullyQualifiedPatched.toUri()
        }
    }

    protected fun intentOf(action: Intent.() -> Unit): Intent {
        val intent = Intent()
        intent.action()
        return intent
    }
}

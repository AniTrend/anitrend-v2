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

import android.content.Intent
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.deeplink.environment.AniTrendEnvironment
import co.anitrend.deeplink.environment.contract.IAniTrendEnvironment
import com.kingsleyadio.deeplink.DeepLinkUri
import org.koin.test.KoinTest

abstract class CommonRouteTest : KoinTest {

    private val webBaseUrl = "https://anilist.co"
    private val appBaseUrl = "app.anitrend://"
    private val basePackage = "co.anitrend"

    protected val instrumentationRegistry by lazy {
        InstrumentationRegistry.getInstrumentation()
    }

    protected val environment: IAniTrendEnvironment by lazy {
        AniTrendEnvironment(
            instrumentationRegistry.context,
            false,
            IAuthenticationSettings.INVALID_USER_ID
        )
    }

    protected fun webLinkOf(path: String): DeepLinkUri {
        return DeepLinkUri.parse("$webBaseUrl/$path")
    }

    protected fun appLinkOf(path: String): DeepLinkUri {
        return DeepLinkUri.parse("$appBaseUrl/$path")
    }

    protected fun String.toIntent(): Intent {
        val fullyQualifiedPatched = "$basePackage.$this"
        return Intent(Intent.ACTION_VIEW, fullyQualifiedPatched.toUri())
    }
}
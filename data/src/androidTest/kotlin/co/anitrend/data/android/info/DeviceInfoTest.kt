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
package co.anitrend.data.android.info

import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceInfoTest {
    @Test
    fun platformUserAgentIsLoadedOnlyOnFirstAccess() {
        var providerCalls = 0
        val deviceInfo =
            DeviceInfo(
                platformUserAgent = {
                    providerCalls += 1
                    "Test platform"
                },
            )

        assertEquals(0, providerCalls)

        val expectedUserAgent =
            "Test platform (${Build.MANUFACTURER} ${Build.MODEL}) " +
                "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) " +
                Build.SUPPORTED_ABIS.firstOrNull()
        val actualUserAgent = deviceInfo.userAgent

        assertEquals(expectedUserAgent, actualUserAgent)
        assertEquals(1, providerCalls)
        assertEquals(actualUserAgent, deviceInfo.userAgent)
        assertEquals(1, providerCalls)
    }
}

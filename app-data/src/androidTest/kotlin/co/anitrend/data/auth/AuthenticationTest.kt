/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.auth

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.BuildConfig
import co.anitrend.data.auth.helper.AUTHENTICATION_URI
import co.anitrend.data.auth.helper.AuthenticationHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URLDecoder

@RunWith(AndroidJUnit4ClassRunner::class)
class AuthenticationTest {

    @Test
    fun callBackUrlIsCorrect() {
        // This test may fail due to Uri class not being mock-able
        val expected = "https://${BuildConfig.apiAuthUrl}/authorize?client_id=${BuildConfig.clientId}&response_type=token"
        val actual = URLDecoder.decode(AUTHENTICATION_URI.toString(), "UTF-8")

        assertEquals(expected, actual)
    }
}

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

package co.anitrend.deeplink.component.route

import android.content.Intent
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.deeplink.common.CommonRouteTest
import com.kingsleyadio.deeplink.DeepLinkParser
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AppRouteTest : CommonRouteTest() {

    private val deepLinkParser: DeepLinkParser<Intent?> by lazy {
        DeepLinkParser.of<Intent?>(environment)
            .addRoute(DiscoverRoute)
            .addRoute(SocialRoute)
            .addRoute(SuggestionsRoute)
            .addRoute(SettingsRoute)
            .addRoute(ProfileRoute)
            .addRoute(UpdatesRoute)
            .addRoute(AboutRoute)
            .addRoute(OAuthRoute)
            .addFallbackAction(FallbackAction)
            .build()
    }

    @Test
    fun verifyDiscoverAppRoute() {
        val expected = "media.discover.component.screen.MediaDiscoverScreen".toIntent()
        val actual = deepLinkParser.parse(appLinkOf("discover"))

        Assert.assertEquals(expected, actual)
    }
}
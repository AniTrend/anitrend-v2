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
import co.anitrend.deeplink.common.CommonRouteTest
import com.kingsleyadio.deeplink.DeepLinkParser
import org.junit.Assert.assertEquals
import org.junit.Test

class WebRouteTest : CommonRouteTest() {
    private val deepLinkParser: DeepLinkParser<Intent?> by lazy {
        DeepLinkParser.of<Intent?>(environment)
            .addRoute(MainRoute)
            .addRoute(ActivityRoute)
            .addRoute(ForumRoute)
            .addRoute(ReviewRoute)
            .addRoute(NewsRoute)
            .addRoute(EpisodesRoute)
            .addRoute(ForumDiscoverRoute)
            .addRoute(RecommendationRoute)
            .addRoute(CharacterRoute)
            .addRoute(StudioRoute)
            .addRoute(StaffRoute)
            .addRoute(MediaRoute)
            .addRoute(SearchRoute)
            .addRoute(MediaListRoute)
            .addRoute(UserRoute)
            .addRoute(UserStatsRoute)
            .addRoute(UserFavouritesRoute)
            .addRoute(UserReviewRoute)
            .addFallbackAction(FallbackAction)
            .build()
    }

    @Test
    fun verifyHomeWebRoute() {
        val expected = "component.screen.MainScreen".toIntent()
        val actual = deepLinkParser.parse(webLinkOf("home"))

        assertEquals(expected, actual)
    }
}

/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.media.provider

import android.content.Context
import android.content.Intent
import co.anitrend.media.component.screen.MediaPeopleScreen
import co.anitrend.media.component.screen.MediaRecommendationsScreen
import co.anitrend.media.component.screen.MediaRelationsScreen
import co.anitrend.media.component.screen.MediaScreen
import co.anitrend.media.component.screen.MediaStatsScreen
import co.anitrend.media.component.screen.MediaStudiosScreen
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.MediaRecommendationsRouter
import co.anitrend.navigation.MediaRelationsRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.MediaStatsRouter
import co.anitrend.navigation.MediaStudiosRouter

class FeatureProvider : MediaRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaScreen::class.java)
}

class PeopleFeatureProvider : MediaPeopleRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaPeopleScreen::class.java)
}

class RelationsFeatureProvider : MediaRelationsRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaRelationsScreen::class.java)
}

class RecommendationsFeatureProvider : MediaRecommendationsRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaRecommendationsScreen::class.java)
}

class StudiosFeatureProvider : MediaStudiosRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaStudiosScreen::class.java)
}

class StatsFeatureProvider : MediaStatsRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaStatsScreen::class.java)
}

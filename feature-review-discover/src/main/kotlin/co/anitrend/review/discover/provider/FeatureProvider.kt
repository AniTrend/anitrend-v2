/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.review.discover.provider

import android.content.Context
import android.content.Intent
import co.anitrend.review.discover.component.content.ReviewDiscoverContent
import co.anitrend.review.discover.component.screen.ReviewDiscoverScreen
import co.anitrend.navigation.ReviewDiscoverRouter

class FeatureProvider : ReviewDiscoverRouter.Provider {
    override fun activity(context: Context?) =
        Intent(context, ReviewDiscoverScreen::class.java)
        
    override fun fragment() = ReviewDiscoverContent::class.java
}
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
package co.anitrend.media.discover.provider

import android.content.Context
import android.content.Intent
import co.anitrend.media.discover.component.content.MediaDiscoverContent
import co.anitrend.media.discover.component.screen.MediaDiscoverScreen
import co.anitrend.media.discover.component.sheet.MediaDiscoverFilterSheet
import co.anitrend.navigation.MediaDiscoverRouter

internal class FeatureProvider : MediaDiscoverRouter.Provider {
    override fun activity(context: Context?) = Intent(context, MediaDiscoverScreen::class.java)

    override fun fragment() = MediaDiscoverContent::class.java

    override fun sheet() = MediaDiscoverFilterSheet::class.java
}

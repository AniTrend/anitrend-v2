/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.media.discover.filter.provider

import co.anitrend.media.discover.filter.component.content.GeneralContent
import co.anitrend.media.discover.filter.component.content.GenreContent
import co.anitrend.media.discover.filter.component.content.SortingContent
import co.anitrend.media.discover.filter.component.content.TagContent
import co.anitrend.navigation.MediaDiscoverFilterRouter

internal class FeatureProvider : MediaDiscoverFilterRouter.Provider {
    override fun sorting() = SortingContent::class.java

    override fun general() = GeneralContent::class.java

    override fun genre() = GenreContent::class.java

    override fun tag() = TagContent::class.java
}

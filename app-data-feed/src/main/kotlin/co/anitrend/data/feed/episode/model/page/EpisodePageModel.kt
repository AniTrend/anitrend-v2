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

package co.anitrend.data.feed.episode.model.page

import co.anitrend.data.feed.episode.model.EpisodeModelItem
import co.anitrend.data.feed.contract.model.IRssChannelModel
import co.anitrend.data.feed.contract.model.contract.IRssContainer
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
internal data class EpisodePageModel @JvmOverloads constructor(
    @field:Element(
        name = "channel",
        required = true
    )
    override var channel: ChannelModel? = null
) : IRssContainer<EpisodePageModel.ChannelModel> {

    @Root(name = "channel", strict = false)
    internal data class ChannelModel @JvmOverloads constructor(
        @field:ElementList(
            name = "item",
            required = false,
            inline = true
        )
        override var items: List<EpisodeModelItem>? = null
    ) : IRssChannelModel<EpisodeModelItem>
}
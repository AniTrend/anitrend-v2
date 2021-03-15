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

package co.anitrend.data.episode.model

import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.rss.RCF822Date
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
internal data class EpisodeModelItem @JvmOverloads constructor(
    @field:Element(name = "title")
    var title: String = String.empty(),
    @field:Element(name = "guid")
    var guid: String = String.empty(),
    @field:Element(name = "description", required = false)
    var description: String? = null,
    @field:Element(name = "mediaId")
    var mediaId: Int = 0,
    @field:Element(name = "premiumPubDate")
    var premiumAvailableDate: RCF822Date = String.empty(),
    @field:Element(name = "freePubDate")
    var freeAvailableDate: RCF822Date = String.empty(),
    @field:Element(name = "seriesTitle", required = false)
    var seriesTitle: String = String.empty(),
    @field:Element(name = "episodeTitle", required = false)
    var episodeTitle: String? = null,
    @field:Element(name = "episodeNumber", required = false)
    var episodeNumber: String? = null,
    @field:Element(name = "publisher", required = false)
    var publisher: String? = null,
    @field:Element(name = "season", required = false)
    var season: String? = null,
    @field:Element(name = "duration", required = false)
    var duration: Int? = null,
    @field:Element(name = "subtitleLanguages", required = false)
    var subtitleLanguages: String? = null,
    @field:Element(name = "restriction", required = false)
    var restriction: RestrictionModel? = null,
    @field:Element(name = "keywords", required = false)
    var keywords: String? = null,
    @field:Element(name = "rating", required = false)
    var rating: String? = null,
    @field:ElementList(name = "thumbnail", inline = true, required = false)
    var thumbnails : List<ThumbnailModel>? = null,
) {

    @Root(name = "thumbnail", strict = false)
    data class ThumbnailModel @JvmOverloads constructor(
        @field:Attribute(name = "url")
        var url: String = String.empty(),
        @field:Attribute(name = "width")
        var width: Int = 0,
        @field:Attribute(name = "height")
        var height: Int = 0
    )

    @Root(name = "restriction", strict = false)
    data class RestrictionModel @JvmOverloads constructor(
        @field:Attribute(name = "relationship")
        var relationship: String = String.empty(),
        @field:Attribute(name = "type")
        var type: String = String.empty(),
        @field:Element(name = "elements", required = false)
        var elements: String = String.empty()
    )
}
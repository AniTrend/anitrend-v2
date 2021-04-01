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

package co.anitrend.data.feed.news.model

import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.feed.contract.RCF822Date
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
internal data class NewsModelItem @JvmOverloads constructor(
    @field:Element(name = "title")
    var title: String = String.empty(),
    @field:Element(name = "author")
    var author: String = String.empty(),
    @field:Element(name = "description")
    var description: String = String.empty(),
    @field:Element(name = "encoded")
    var contentEncoded: String = String.empty(),
    @field:Element(name = "pubDate")
    var publishedOn: RCF822Date = String.empty(),
    @field:Element(name = "link")
    var referenceLink: String = String.empty()
)
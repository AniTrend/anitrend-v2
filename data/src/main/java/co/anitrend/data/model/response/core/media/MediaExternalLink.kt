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

package co.anitrend.data.model.response.core.media

import co.anitrend.data.entity.contract.IEntity

/** [MediaExternalLink](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaexternallink.doc.html)
 * An external link to another site related to the media
 *
 * @param id The id of the external link
 * @param site The site location of the external link
 * @param url The url of the external link
 */
data class MediaExternalLink(
    val site: String,
    val url: String,
    override val id: Long
) : IEntity
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

package co.anitrend.data.link.model

import co.anitrend.data.core.common.Identity
import co.anitrend.domain.media.enums.ExternalLinkType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [MediaExternalLink](https://anilist.github.io/ApiV2-GraphQL-Docs/mediaexternallink.doc.html)
 * An external link to another site related to the media or staff member
 *
 * @param icon The icon image url of the site. Not available for all links. Transparent PNG 64x64
 * @param language Language the site content is in. See Staff language field for values.
 * @param url The url of the external link or base url of link source
 */
@Serializable
data class LinkModel(
    @SerialName("color") val color: String?,
    @SerialName("icon") val icon: String?,
    @SerialName("isDisabled") val isDisabled: Boolean?,
    @SerialName("language") val language: String?,
    @SerialName("notes") val notes: String?,
    @SerialName("siteId") val siteId: Int?,
    @SerialName("type") val linkType: ExternalLinkType?,
    @SerialName("site") val site: String,
    @SerialName("url") val url: String,
    @SerialName("id") override val id: Long
) : Identity
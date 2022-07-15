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

package co.anitrend.domain.media.entity.attribute.link

import co.anitrend.domain.media.enums.ExternalLinkType

data class MediaExternalLink(
    override val color: CharSequence?,
    override val icon: CharSequence?,
    override val isDisabled: Boolean?,
    override val language: CharSequence?,
    override val notes: CharSequence?,
    override val siteId: Int?,
    override val linkType: ExternalLinkType?,
    override val site: CharSequence,
    override val url: CharSequence,
    override val id: Long
) : IMediaExternalLink
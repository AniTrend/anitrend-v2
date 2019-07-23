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

package co.anitrend.data.model.response.core.medialist

import androidx.room.PrimaryKey
import co.anitrend.data.model.response.core.medialist.contract.IMediaList

/** [MediaList](https://anilist.github.io/ApiV2-GraphQL-Docs/medialist.doc.html)
 * List of anime or manga
 *
 *
 */
data class MediaList(
    @PrimaryKey
    override val id: Long
) : IMediaList
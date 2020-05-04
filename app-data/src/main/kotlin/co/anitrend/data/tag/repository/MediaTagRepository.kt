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

package co.anitrend.data.tag.repository

import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.model.UserInterfaceState.Companion.create
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.data.tag.datasource.MediaTagSource
import co.anitrend.domain.tag.entities.Tag
import co.anitrend.domain.tag.repositories.IMediaTagRepository

internal class MediaTagRepository(
    private val source: MediaTagSource
) : SupportRepository(source),
    IMediaTagRepository<UserInterfaceState<List<Tag>>> {

    /**
     * @return media genres
     */
    override fun getMediaTags() =
        source.create(
            model = source()
        )
}
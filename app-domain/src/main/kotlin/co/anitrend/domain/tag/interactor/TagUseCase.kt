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

package co.anitrend.domain.tag.interactor

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.tag.model.TagParam
import co.anitrend.domain.tag.repository.ITagRepository

abstract class TagUseCase<R: UiState<*>>(
    protected val repository: ITagRepository<R>
) {

    /**
     * @return media tags user interface state
     */
    fun getMediaTags(param: TagParam) =
        repository.getMediaTags(param)
}

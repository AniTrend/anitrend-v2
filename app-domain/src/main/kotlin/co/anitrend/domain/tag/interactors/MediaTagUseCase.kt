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

package co.anitrend.domain.tag.interactors

import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.usecases.ISupportUseCase
import co.anitrend.domain.tag.repositories.IMediaTagRepository

abstract class MediaTagUseCase<R: IUserInterfaceState<*>>(
    protected val repository: IMediaTagRepository<R>
) : ISupportUseCase<Nothing?, R> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: Nothing?): R {
        return repository.getMediaTags()
    }
}
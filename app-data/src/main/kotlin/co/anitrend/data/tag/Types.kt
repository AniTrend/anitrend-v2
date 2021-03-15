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

package co.anitrend.data.tag

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.model.remote.TagContainerModel
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.domain.tag.interactor.TagUseCase
import co.anitrend.domain.tag.repository.ITagRepository

internal typealias TagController = GraphQLController<TagContainerModel, List<TagEntity>>
internal typealias TagListRepository = ITagRepository<DataState<List<Tag>>>

typealias TagInteractor = TagUseCase<DataState<List<Tag>>>
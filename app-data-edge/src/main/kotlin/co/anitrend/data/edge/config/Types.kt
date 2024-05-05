/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.data.edge.config

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.model.remote.EdgeConfigModel
import co.anitrend.domain.config.entity.Config
import co.anitrend.domain.config.interactor.ConfigUseCase
import co.anitrend.domain.config.repository.IConfigRepository

internal typealias EdgeConfigController = GraphQLController<EdgeConfigModel, EdgeConfigEntity>

internal typealias ConfigRepository = IConfigRepository.Config<DataState<Config>>

typealias GetConfigInteractor = ConfigUseCase.Config<DataState<Config>>

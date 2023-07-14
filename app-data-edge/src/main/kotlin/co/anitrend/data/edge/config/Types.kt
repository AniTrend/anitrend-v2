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

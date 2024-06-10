package co.anitrend.data.edge.config.usecase

import co.anitrend.data.edge.config.ConfigRepository
import co.anitrend.data.edge.config.GetConfigInteractor

internal class EdgeConfigInteractor(
    repository: ConfigRepository
) : GetConfigInteractor(repository)

package co.anitrend.data.edge.config.repository

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.edge.config.ConfigRepository
import co.anitrend.data.edge.config.source.contract.EdgeConfigSource
import co.anitrend.domain.config.entity.Config

internal class EdgeConfigRepository(
    private val source: EdgeConfigSource
) : SupportRepository(source), ConfigRepository {
    override fun getConfig(): DataState<Config> =
        source create source()
}

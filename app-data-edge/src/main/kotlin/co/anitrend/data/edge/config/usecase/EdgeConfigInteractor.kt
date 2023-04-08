package co.anitrend.data.edge.config.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.data.edge.config.ConfigRepository
import co.anitrend.data.edge.config.GetConfigInteractor

internal class EdgeConfigInteractor(
    repository: ConfigRepository
) : GetConfigInteractor(repository) {
    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        repository as ISupportRepository
        repository.onCleared()
    }
}

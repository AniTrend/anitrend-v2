package co.anitrend.domain.config.interactor

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.config.repository.IConfigRepository

sealed class ConfigUseCase : IUseCase {
    abstract class Config<State: UiState<*>>(
        protected val repository: IConfigRepository.Config<State>
    ): ConfigUseCase() {
        operator fun invoke() = repository.getConfig()
    }
}

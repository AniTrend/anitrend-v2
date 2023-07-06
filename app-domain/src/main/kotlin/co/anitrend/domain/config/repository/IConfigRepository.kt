package co.anitrend.domain.config.repository

import co.anitrend.arch.domain.state.UiState

sealed interface IConfigRepository {
    interface Config<State: UiState<*>> : IConfigRepository {
        fun getConfig(): State
    }
}

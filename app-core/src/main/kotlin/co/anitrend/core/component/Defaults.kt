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
package co.anitrend.core.component

import androidx.lifecycle.liveData
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.R

object FeatureUnavailable {
    val loadState = liveData<LoadState> { emit(LoadState.Error(details = NotImplementedError("Feature is currently unavailable"))) }
    val config =
        StateLayoutConfig(
            loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
        )
}

object FeatureReady {
    val loadState = liveData<LoadState> { emit(LoadState.Idle()) }
    val config =
        StateLayoutConfig(
            loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
            loadingMessage = R.string.label_text_loading,
        )
}

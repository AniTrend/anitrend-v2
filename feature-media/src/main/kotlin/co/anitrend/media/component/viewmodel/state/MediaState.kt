/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.media.component.viewmodel.state

import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.media.GetDetailMediaInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.navigation.MediaRouter

class MediaState(
    private val interactor: GetDetailMediaInteractor,
    private val settings: IUserSettings
) : AniTrendViewModelState<Media>() {

    operator fun invoke(parameter: MediaRouter.MediaParam) {
        val param = MediaParam.Detail(
            id = parameter.id,
            type = parameter.type,
            scoreFormat = settings.scoreFormat.value
        )
        val result = interactor(param)
        state.postValue(result)
    }
}

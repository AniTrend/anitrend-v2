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

package co.anitrend.medialist.editor.component.sheet.viewmodel.state

import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.media.GetDetailMediaInteractor
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.navigation.MediaListEditorRouter

class MediaListEditorState(
    private val interactor: GetDetailMediaInteractor
): AniTrendViewModelState<Media>() {

    operator fun invoke(param: MediaListEditorRouter.MediaListEditorParam) {
        val query = MediaParam.Detail(
            id = param.mediaId,
            type = param.mediaType,
            scoreFormat = param.scoreFormat,
        )

        val result = interactor(query)

        state.postValue(result)
    }
}

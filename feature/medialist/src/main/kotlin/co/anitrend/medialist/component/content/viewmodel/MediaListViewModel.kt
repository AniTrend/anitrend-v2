/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.medialist.component.content.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.medialist.GetPagingMediaListInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.extensions.nameOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MediaListViewModel(
    private val interactor: GetPagingMediaListInteractor,
    private val settings: IUserSettings,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val savedStateKey = nameOf<MediaListRouter.MediaListParam>()

    private val initialParam = requireNotNull(savedStateHandle.get<MediaListRouter.MediaListParam>(savedStateKey))

    private val mutableParams = MutableStateFlow(initialParam.withDefaultSection())

    val params: StateFlow<MediaListRouter.MediaListParam> = mutableParams.asStateFlow()

    val media: Flow<PagingData<Media>> = params.flatMapLatest(::query).cachedIn(viewModelScope)

    init {
        savedStateHandle[savedStateKey] = mutableParams.value
    }

    fun selectSection(section: MediaListInfo) {
        val updated = params.value.withSection(section)
        if (updated == params.value) {
            return
        }

        savedStateHandle[savedStateKey] = updated
        mutableParams.value = updated
    }

    private fun query(param: MediaListRouter.MediaListParam): Flow<PagingData<Media>> = interactor(param.asQuery(settings.scoreFormat.value))
}

private fun MediaListRouter.MediaListParam.withDefaultSection(): MediaListRouter.MediaListParam =
    if (customListName != null || status != null) {
        this
    } else {
        copy(status = MediaListStatus.CURRENT)
    }

private fun MediaListRouter.MediaListParam.withSection(section: MediaListInfo): MediaListRouter.MediaListParam =
    if (section.isCustomList) {
        copy(
            customListName = section.name,
            status = null,
            status_in = null,
            status_not = null,
            status_not_in = null,
        )
    } else {
        copy(
            customListName = null,
            status = MediaListStatus.valueOf(section.name),
            status_in = null,
            status_not = null,
            status_not_in = null,
        )
    }

private fun MediaListRouter.MediaListParam.asQuery(scoreFormat: co.anitrend.domain.medialist.enums.ScoreFormat) =
    MediaListParam.Paged(
        customListName = customListName,
        mediaId_in = mediaId_in,
        mediaId_not_in = mediaId_not_in,
        isFollowing = isFollowing,
        userId_in = userId_in,
        compareWithAuthList = compareWithAuthList,
        scoreFormat = scoreFormat,
        type = type,
        userId = userId,
        userName = userName,
        completedAt = completedAt,
        completedAt_greater = completedAt_greater,
        completedAt_lesser = completedAt_lesser,
        completedAt_like = completedAt_like,
        notes = notes,
        notes_like = notes_like,
        sort = sort,
        startedAt = startedAt,
        startedAt_greater = startedAt_greater,
        startedAt_lesser = startedAt_lesser,
        startedAt_like = startedAt_like,
        status = status,
        status_in = status_in,
        status_not = status_not,
        status_not_in = status_not_in,
    )

/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.media.discover.component.content.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.arch.extension.ext.extra
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.nameOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MediaDiscoverViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: GetPagingMediaInteractor,
) : ViewModel() {
    val default by savedStateHandle.extra(
        key = nameOf<MediaDiscoverRouter.MediaDiscoverParam>(),
        default = MediaDiscoverRouter::MediaDiscoverParam,
    )

    private val mutableParams = MutableStateFlow(default)

    val params: StateFlow<MediaDiscoverRouter.MediaDiscoverParam> = mutableParams.asStateFlow()

    val media: Flow<PagingData<Media>> = params.flatMapLatest(::query).cachedIn(viewModelScope)

    /**
     * Handle param changes by settings the new [param] to the [state]
     */
    fun setParam(param: MediaDiscoverRouter.MediaDiscoverParam) {
        savedStateHandle[MediaDiscoverFilterRouter.RESULT_LISTENER_KEY] = param
        mutableParams.value = param
    }

    fun getParam(): MediaDiscoverRouter.MediaDiscoverParam = params.value

    private fun query(param: MediaDiscoverRouter.MediaDiscoverParam): Flow<PagingData<Media>> = interactor(param.asQuery())
}

private fun MediaDiscoverRouter.MediaDiscoverParam.asQuery() =
    MediaParam.Find(
        averageScore = averageScore,
        averageScore_greater = averageScore_greater,
        averageScore_lesser = averageScore_lesser,
        averageScore_not = averageScore_not,
        chapters = chapters,
        chapters_greater = chapters_greater,
        chapters_lesser = chapters_lesser,
        countryOfOrigin = countryOfOrigin,
        duration = duration,
        duration_greater = duration_greater,
        duration_lesser = duration_lesser,
        endDate = endDate,
        endDate_greater = endDate_greater,
        endDate_lesser = endDate_lesser,
        endDate_like = endDate_like,
        episodes = episodes,
        episodes_greater = episodes_greater,
        episodes_lesser = episodes_lesser,
        format = format,
        format_in = format_in,
        format_not = format_not,
        format_not_in = format_not_in,
        genre = genre,
        genre_in = genre_in,
        genre_not_in = genre_not_in,
        id = id,
        idMal = idMal,
        idMal_in = idMal_in,
        idMal_not = idMal_not,
        idMal_not_in = idMal_not_in,
        id_in = id_in,
        id_not = id_not,
        id_not_in = id_not_in,
        isAdult = isAdult,
        licensedBy = licensedBy,
        licensedBy_in = licensedBy_in,
        minimumTagRank = minimumTagRank,
        onList = onList,
        popularity = popularity,
        popularity_greater = popularity_greater,
        popularity_lesser = popularity_lesser,
        popularity_not = popularity_not,
        search = search,
        season = season,
        seasonYear = seasonYear,
        sort = sort,
        source = source,
        source_in = source_in,
        startDate = startDate,
        startDate_greater = startDate_greater,
        startDate_lesser = startDate_lesser,
        startDate_like = startDate_like,
        status = status,
        status_in = status_in,
        status_not = status_not,
        status_not_in = status_not_in,
        tag = tag,
        tagCategory = tagCategory,
        tagCategory_in = tagCategory_in,
        tagCategory_not_in = tagCategory_not_in,
        tag_in = tag_in,
        tag_not_in = tag_not_in,
        type = type,
        volumes = volumes,
        volumes_greater = volumes_greater,
        volumes_lesser = volumes_lesser,
    )

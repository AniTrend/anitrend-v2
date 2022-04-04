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

package co.anitrend.episode.component.sheet.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.ext.empty
import co.anitrend.core.extensions.hook
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.episode.R
import co.anitrend.episode.component.sheet.viewmodel.state.EpisodeSheetState
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class EpisodeSheetViewModel(
    val state: EpisodeSheetState
) : ViewModel() {

    init {
        hook(state)
    }

    val model = MutableLiveData<String>()

    fun buildHtml(episode: Episode, context: Context) {
        viewModelScope.launch {
            val description = episode.description
            val content = if (description.isNullOrBlank())
                context.getString(R.string.label_episode_has_no_summary, episode.about.episodeTitle)
            else episode.description ?: String.empty()

            val document = Jsoup.parse(content)
            model.postValue(document.html())
        }
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        state.onCleared()
        super.onCleared()
    }
}
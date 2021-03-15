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

package co.anitrend.media.discover.component.sheet.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import co.anitrend.media.discover.component.sheet.action.MediaFilterAction.*
import co.anitrend.navigation.MediaDiscoverRouter.Param

internal class MediaFilterController {

    private fun onTagFilters(tag: Tag?, filter: LiveData<Param>) {
        filter.value?.tagCategory_in = tag?.tagCategory_in
        filter.value?.tagCategory_not_in = tag?.tagCategory_not_in
        filter.value?.tag_in = tag?.tag_in
        filter.value?.tag_not_in = tag?.tag_not_in
    }

    private fun onGenreFilters(tag: Genre?, filter: LiveData<Param>) {
        filter.value?.genre_in = tag?.genre_in
        filter.value?.genre_not_in = tag?.genre_not_in
    }

    /**
     * Listens in for results on [setFragmentResultListener]
     */
    fun registerResultCallbacks(fragment: Fragment, filter: LiveData<Param>) {
        fragment.setFragmentResultListener(Tag.KEY) {
                key: String, bundle: Bundle ->
            val tag = bundle.getParcelable<Tag>(key)
            onTagFilters(tag, filter)
        }
        fragment.setFragmentResultListener(Genre.KEY) {
                key: String, bundle: Bundle ->
            val genre = bundle.getParcelable<Genre>(key)
            onGenreFilters(genre, filter)
        }
    }
}
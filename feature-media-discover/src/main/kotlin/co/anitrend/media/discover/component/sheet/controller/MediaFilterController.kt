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
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.navigation.MediaDiscoverFilterRouter.Action
import co.anitrend.navigation.MediaDiscoverRouter.Param
import com.google.android.material.tabs.TabLayout

internal class MediaFilterController(tabLayout: TabLayout) {

    private val sortTab by lazy(UNSAFE) { tabLayout.getTabAt(0) }
    private val generalTab by lazy(UNSAFE) { tabLayout.getTabAt(1) }
    private val genreTab by lazy(UNSAFE) { tabLayout.getTabAt(2) }
    private val tagTab by lazy(UNSAFE) { tabLayout.getTabAt(3) }

    private fun onTagFilters(tag: Action.Tag?, filter: LiveData<Param>) {
        if (tag?.isDefault() == true)
            tagTab?.orCreateBadge
        else tagTab?.removeBadge()
        filter.value?.tagCategory_in = tag?.tagCategory_in
        filter.value?.tagCategory_not_in = tag?.tagCategory_not_in
        filter.value?.tag_in = tag?.tag_in
        filter.value?.tag_not_in = tag?.tag_not_in
    }

    private fun onGenreFilters(genre: Action.Genre?, filter: LiveData<Param>) {
        if (genre?.isDefault() == true)
            genreTab?.orCreateBadge
        else genreTab?.removeBadge()
        filter.value?.genre_in = genre?.genre_in
        filter.value?.genre_not_in = genre?.genre_not_in
    }

    private fun onGeneralFilter(general: Action.General?, filter: LiveData<Param>) {
        if (general?.isDefault() == true)
            generalTab?.orCreateBadge
        else generalTab?.removeBadge()
    }

    private fun onSortFilter(general: Action.Sort?, filter: LiveData<Param>) {
        if (general?.isDefault() == true)
            sortTab?.orCreateBadge
        else sortTab?.removeBadge()
        filter.value?.sort = general?.sort
    }

    /**
     * Listens in for results on [setFragmentResultListener]
     */
    fun registerResultCallbacks(fragment: Fragment, filter: LiveData<Param>) {
        fragment.setFragmentResultListener(Action.Sort.KEY) {
                key: String, bundle: Bundle ->
            val sort = bundle.getParcelable<Action.Sort>(key)
            onSortFilter(sort, filter)
        }
        fragment.setFragmentResultListener(Action.General.KEY) {
                key: String, bundle: Bundle ->
            val general = bundle.getParcelable<Action.General>(key)
            onGeneralFilter(general, filter)
        }
        fragment.setFragmentResultListener(Action.Tag.KEY) {
                key: String, bundle: Bundle ->
            val tag = bundle.getParcelable<Action.Tag>(key)
            onTagFilters(tag, filter)
        }
        fragment.setFragmentResultListener(Action.Genre.KEY) {
                key: String, bundle: Bundle ->
            val genre = bundle.getParcelable<Action.Genre>(key)
            onGenreFilters(genre, filter)
        }
    }
}
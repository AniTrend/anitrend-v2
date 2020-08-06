/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.search.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.extensions.commit
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.core.ui.fragment.model.FragmentItem
import co.anitrend.multisearch.model.Search
import co.anitrend.search.databinding.SearchScreenBinding
import co.anitrend.search.ui.fragment.SearchContentScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

class SearchScreen : AnitrendActivity() {

    private val binding by lazy(UNSAFE) {
        SearchScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomAppBar)
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            binding.multiSearch.searchChangeFlow()
                .filterNotNull()
                .onEach { search ->
                    when (search) {
                        is Search.TextChanged -> {

                        }
                        is Search.Removed -> {

                        }
                        is Search.Selected -> {

                        }
                    }
                }.collect()
        }
        onUpdateUserInterface()
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(fragment = SearchContentScreen::class.java)
            .commit(binding.searchContent, this) {}
    }
}
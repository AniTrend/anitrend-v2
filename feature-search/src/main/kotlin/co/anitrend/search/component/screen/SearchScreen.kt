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

package co.anitrend.search.component.screen

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.ui.commit
import co.anitrend.core.component.screen.AnitrendScreen
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.multisearch.model.Search
import co.anitrend.search.databinding.SearchScreenBinding
import co.anitrend.search.component.content.SearchContent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

class SearchScreen : AnitrendScreen<SearchScreenBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        setSupportActionBar(requireBinding().bottomAppBar)
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
            binding?.multiSearch?.searchChangeFlow()
                ?.filterNotNull()
                ?.onEach { search ->
                    when (search) {
                        is Search.TextChanged -> {

                        }
                        is Search.Removed -> {

                        }
                        is Search.Selected -> {

                        }
                    }
                }
                ?.collect()
        }
        onUpdateUserInterface()
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(fragment = SearchContent::class.java)
            .commit(requireBinding().searchContent, this)
    }

    /**
     * {@inheritDoc}
     *
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEARCH, GOOGLE_ACTION_SEARCH -> {
                val query = intent.getStringExtra(SearchManager.QUERY)
                if (query != null && query.isNotEmpty()) {
                    // TODO: Add support in multi-search to search programmatically
                    //requireBinding().multiSearch.addIfNotExists(query)
                }
            }
        }
    }

    companion object {
        /**
         * Google specific search action, e.g. assistant or google search
         */
        const val GOOGLE_ACTION_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION"
    }
}
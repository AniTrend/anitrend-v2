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
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.multisearch.model.MultiSearchChangeListener
import co.anitrend.search.R
import co.anitrend.search.koin.injectFeatureModules
import co.anitrend.search.presenter.SearchPresenter
import kotlinx.android.synthetic.main.search_screen.*
import org.koin.android.ext.android.inject

class SearchScreen : AnitrendActivity<Nothing, SearchPresenter>() {

    private val searchChangeListener =
        object : MultiSearchChangeListener {
            /**
             * Called when a search item has been selected
             *
             * @param index character index that has been changed
             * @param charSequence stream of characters including changes
             */
            override fun onItemSelected(index: Int, charSequence: CharSequence) {

            }

            /**
             * Called when an IME action of done is triggered
             *
             * @param index character index that has been changed
             * @param charSequence stream of characters including changes
             */
            override fun onSearchComplete(index: Int, charSequence: CharSequence) {

            }

            /**
             * Called when a search item has been removed
             *
             * @param index
             */
            override fun onSearchItemRemoved(index: Int) {

            }

            /**
             * Called when text has been changed
             *
             * @param index character index that has been changed
             * @param charSequence stream of characters including changes
             */
            override fun onTextChanged(index: Int, charSequence: CharSequence) {

            }
        }

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter by inject<SearchPresenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)
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
        injectFeatureModules()
        multiSearch.setSearchViewListener(searchChangeListener)
    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {

    }
}
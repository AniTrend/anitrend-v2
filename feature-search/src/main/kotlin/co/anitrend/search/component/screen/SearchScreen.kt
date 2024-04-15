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
import androidx.activity.compose.setContent
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.FeatureReady
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.navigation.SearchRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.component.compose.SearchScreenContent
import co.anitrend.search.component.presenter.SearchPresenter

class SearchScreen : AniTrendScreen() {

    private val presenter by inject<SearchPresenter>()
    private val param by extra<SearchRouter.SearchParam>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper<IParam>(
                    stateFlow = FeatureReady.loadState,
                    config = FeatureReady.config,
                    param = param ?: SearchRouter.SearchParam(),
                    onClick = {},
                ) {
                    SearchScreenContent(
                        query = param?.query.orEmpty(),
                        onQueryChange = {},
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        onBackClick = ::onBackPressed,
                    )
                }
            }
        }
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
        setIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEARCH, GOOGLE_ACTION_SEARCH -> {
                val query = intent.getStringExtra(SearchManager.QUERY)
                if (!query.isNullOrEmpty()) {
                    // TODO: Add view model event to emit to for a new search query
                    return
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

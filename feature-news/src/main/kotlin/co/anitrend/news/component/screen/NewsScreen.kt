/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.news.component.screen

import android.os.Bundle
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.core.component.screen.AniTrendBoundScreen
import co.anitrend.core.extensions.handleViewIntent
import co.anitrend.core.extensions.stackTrace
import co.anitrend.core.ui.inject
import co.anitrend.navigation.NewsRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.news.R
import co.anitrend.news.component.screen.viewmodel.NewsScreenViewModel
import co.anitrend.news.databinding.NewsScreenBinding
import co.anitrend.news.presenter.NewsPresenter
import io.noties.markwon.Markwon
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import org.koin.core.qualifier.named

class NewsScreen : AniTrendBoundScreen<NewsScreenBinding>() {

    private val presenter by inject<NewsPresenter>()

    private val viewModel by inject<NewsScreenViewModel>()

    private val markwon by inject<Markwon>(
        named(MarkdownFlavour.STANDARD)
    )

    private val param by extra<NewsRouter.NewsParam>(
        key = nameOf<NewsRouter.NewsParam>()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewsScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        setSupportActionBar(requireBinding().bottomAppBar)
        setUpViewModelObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.news_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_in_browser -> {
                runCatching {
                    binding?.root?.handleViewIntent(
                        requireNotNull(param?.link)
                    )
                }.stackTrace()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireBinding().shareAction.setOnClickListener {
            val shareCompat = param?.let { entity ->
                presenter.createShareContent(entity, this)
            }?.createChooserIntent()

            runCatching {
                startActivity(shareCompat)
            }.stackTrace()
        }
        BetterLinkMovementMethod.linkify(Linkify.ALL, this)
            .setOnLinkClickListener { view, url ->
                runCatching {
                    view.handleViewIntent(url)
                }.stackTrace()
                true
            }
        onFetchDataInitialize()
    }

    private fun setUpViewModelObserver() {
        val content = requireBinding().newsContent.newsContentTextView
        viewModel.model.observe(this) {
            markwon.setMarkdown(content, it)
        }
    }

    private fun onFetchDataInitialize() {
        viewModel(requireNotNull(param))
    }
}

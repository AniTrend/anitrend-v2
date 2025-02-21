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
package co.anitrend.news.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.extensions.handleViewIntent
import co.anitrend.core.extensions.stackTrace
import co.anitrend.core.ui.inject
import co.anitrend.navigation.NewsRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.news.component.screen.compose.NewsReaderScreen
import co.anitrend.news.component.screen.viewmodel.NewsScreenViewModel
import co.anitrend.news.presenter.NewsPresenter
import io.noties.markwon.Markwon
import org.koin.core.qualifier.named

class NewsScreen : AniTrendScreen() {
    private val presenter by inject<NewsPresenter>()
    private val viewModel by inject<NewsScreenViewModel>()
    private val markwon by inject<Markwon>(named(MarkdownFlavour.STANDARD))
    private val param by extra<NewsRouter.NewsParam>(key = nameOf<NewsRouter.NewsParam>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = viewModel.loadState,
                    param = param,
                    onLoad = viewModel::invoke,
                ) {
                    NewsReaderScreen(
                        state = viewModel.documentHtml,
                        transformer = { markwon.toMarkdown(it) },
                        onBackPress = onBackPressedDispatcher::onBackPressed,
                        onOpenInWebClick = {
                            param?.link?.also { handleViewIntent(it) }
                        },
                        onShareClick = {
                            val shareCompat =
                                param
                                    ?.let { entity ->
                                        presenter.createShareContent(entity, this)
                                    }?.createChooserIntent()

                            runCatching {
                                startActivity(shareCompat)
                            }.stackTrace()
                        },
                        onUrlClick = { url ->
                            handleViewIntent(url)
                        },
                    )
                }
            }
        }
    }
}

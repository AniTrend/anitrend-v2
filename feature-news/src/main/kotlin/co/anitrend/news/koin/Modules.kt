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

package co.anitrend.news.koin

import androidx.browser.customtabs.CustomTabsIntent
import co.anitrend.common.news.ui.adapter.NewsPagedAdapter
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.NewsRouter
import co.anitrend.news.component.content.NewsContent
import co.anitrend.news.component.content.viewmodel.NewsContentViewModel
import co.anitrend.news.component.content.viewmodel.state.NewsContentState
import co.anitrend.news.component.screen.viewmodel.NewsScreenViewModel
import co.anitrend.news.plugin.NewsTagPlugin
import co.anitrend.news.presenter.NewsPresenter
import co.anitrend.news.provider.FeatureProvider
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.linkify.LinkifyPlugin
import org.commonmark.node.Paragraph
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

private val coreModule = module {
    single(named(MarkdownFlavour.STANDARD)) {
        val builder = get<Markwon.Builder>()
        builder.usePlugin(NewsTagPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .usePlugin(
                object : AbstractMarkwonPlugin() {
                    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                        builder.on(
                            Paragraph::class.java
                        ) { visitor, _ ->
                            // TODO: Will remove this after multiple UI tests
                            Timber.v("visitor for paragraph: $visitor")
                        }
                    }
                }
            ).build()
    }
}

private val fragmentModule = module {
    fragment {
        NewsContent(
            presenter = get(),
            stateConfig = get(),
            supportViewAdapter = NewsPagedAdapter(
                resources = androidContext().resources,
                stateConfiguration = get()
            )
        )
    }
}

private val viewModelModule = module {
    viewModel {
        NewsContentViewModel(
            state = NewsContentState(
                useCase = get()
            )
        )
    }
    viewModel {
        NewsScreenViewModel()
    }
}

private val presenterModule = module {
    factory {
        NewsPresenter(
            context = get(),
            settings = get(),
            customTabs = get<CustomTabsIntent.Builder>().build(),
        )
    }
}

private val featureModule = module {
    factory<NewsRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(coreModule, fragmentModule, viewModelModule, presenterModule, featureModule)
)
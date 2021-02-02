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

package co.anitrend.news.presenter

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.locale.AniTrendLocale.Companion.asLocaleString
import co.anitrend.domain.news.entity.News
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.NewsRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.model.NavPayload
import co.anitrend.news.component.screen.NewsScreen
import timber.log.Timber
import java.util.*

class NewsPresenter(
    context: Context,
    settings: Settings,
    private val customTabs: CustomTabsIntent
) : CorePresenter(context, settings) {

    /**
     * Retrieves the current locale based on preferences
     */
    fun getCurrentLocaleString(): String {
        return when (val locale = settings.locale.value) {
            AniTrendLocale.AUTOMATIC -> {
                val default = Locale.getDefault()
                default.asLocaleString()
            }
            else -> locale.asLocaleString()
        }
    }

    fun createShareContent(
        param: NewsRouter.Param,
        screen: NewsScreen
    ): ShareCompat.IntentBuilder {
        val payloadContent = StringBuilder(
            requireNotNull(param.description)
        ).append("\n\n").append(param.link)

        return ShareCompat.IntentBuilder(screen)
            .setType("text/plain")
            .setSubject(param.title)
            .setHtmlText(payloadContent.toString())
    }

    fun handleViewIntent(view: View, url: String) {
        if (url.startsWith("https://img1.ak.crunchyroll")) {
            ViewCompat.setTransitionName(view, url)
            ImageViewerRouter.startActivity(
                context = view.context,
                navPayload = NavPayload(
                    key = ImageViewerRouter.Param.KEY,
                    parcel = ImageViewerRouter.Param(url)
                )
            )
        } else {
            runCatching {
                customTabs.launchUrl(view.context, url.toUri())
            }.onFailure {
                Timber.tag(moduleTag).w(it, "Unable to open custom tabs")
                startViewIntent(url.toUri())
            }
        }
    }
}
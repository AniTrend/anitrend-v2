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
import androidx.core.app.ShareCompat
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.navigation.NewsRouter
import co.anitrend.news.component.screen.NewsScreen

class NewsPresenter(
    context: Context,
    settings: Settings
) : CorePresenter(context, settings) {

    fun createShareContent(
        param: NewsRouter.NewsParam,
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
}

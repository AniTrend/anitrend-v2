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

package co.anitrend.auth.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import co.anitrend.auth.R
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.auth.helper.AuthenticationType
import co.anitrend.data.auth.helper.authenticationUri
import co.anitrend.navigation.AccountTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import timber.log.Timber

class AuthPresenter(
    context: Context,
    settings: Settings,
    private val clientId: String,
    private val customTabs: CustomTabsIntent
) : CorePresenter(context, settings) {

    fun authorizationIssues(activity: FragmentActivity) {
        // Open FAQ page with information about what to do when a user cannot log in
        runCatching {
            customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            customTabs.launchUrl(activity, Uri.parse(context.getString(R.string.app_faq_page_link)))
        }.onFailure {
            Timber.w(it, "Unable to open custom tabs")
            startViewIntent(Uri.parse(context.getString(R.string.app_faq_page_link)))
        }
    }

    fun authorizeWithAniList(activity: FragmentActivity) {
        runCatching {
            customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            customTabs.launchUrl(activity, authenticationUri(AuthenticationType.TOKEN, clientId))
        }.onFailure {
            Timber.w(it, "Unable to open custom tabs")
            startViewIntent(authenticationUri(AuthenticationType.TOKEN, clientId))
        }
    }

    fun runSignOutWorker() {
        AccountTaskRouter.forSignOutWorker()
            .createOneTimeUniqueWorker(
                context,
                AccountTaskRouter.Param(
                    settings.authenticatedUserId.value
                )
            ).enqueue()
    }

    fun runSignInWorker(id: Long) {
        AccountTaskRouter.forSignInWorker()
            .createOneTimeUniqueWorker(
                context,
                AccountTaskRouter.Param(id)
            ).enqueue()
    }
}
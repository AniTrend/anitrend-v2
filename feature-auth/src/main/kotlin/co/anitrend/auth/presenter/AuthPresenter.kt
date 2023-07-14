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
import androidx.core.net.UriCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.extensions.analytics
import co.anitrend.core.android.extensions.keys
import co.anitrend.core.android.extensions.tags
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.extensions.startViewIntent
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
        val uri = Uri.parse(context.getString(co.anitrend.core.android.R.string.app_faq_page_link))
        runCatching {
            Timber.analytics {
                logCurrentState(
                    tag = Timber.tags.action("authorization_issue"),
                    bundle = bundleOf(
                        Timber.keys.DATA to UriCompat.toSafeString(uri)
                    )
                )
            }
            customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            customTabs.launchUrl(activity, uri)
        }.onFailure {
            Timber.w(it, "Unable to open custom tabs")
            context.startViewIntent(uri)
        }
    }

    fun authorizeWithAniList(activity: FragmentActivity) {
        val uri = authenticationUri(AuthenticationType.TOKEN, clientId)
        runCatching {
            Timber.analytics {
                logCurrentState(
                    tag = Timber.tags.action("authorization_issue"),
                    bundle = bundleOf(
                        Timber.keys.DATA to UriCompat.toSafeString(uri)
                    )
                )
            }
            customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            customTabs.launchUrl(activity, uri)
        }.onFailure {
            Timber.w(it, "Unable to open custom tabs")
            context.startViewIntent(uri)
        }
    }

    fun runSignOutWorker() {
        val param = AccountTaskRouter.Param(
            settings.authenticatedUserId.value
        )
        AccountTaskRouter.forSignOutWorker()
            .createOneTimeUniqueWorker(context, param)
            .enqueue()
    }

    fun runSignInWorker(id: Long) {
        val param = AccountTaskRouter.Param(id)
        AccountTaskRouter.forSignInWorker()
            .createOneTimeUniqueWorker(context, param)
            .enqueue()
    }
}

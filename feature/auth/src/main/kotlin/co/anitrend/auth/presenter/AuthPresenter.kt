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
package co.anitrend.auth.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.UriCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import co.anitrend.auth.R
import co.anitrend.auth.model.Authentication
import co.anitrend.android.core.extensions.analytics
import co.anitrend.android.core.extensions.keys
import co.anitrend.android.core.extensions.tags
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.extensions.startViewIntent
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.auth.helper.AuthenticationType
import co.anitrend.data.auth.helper.authenticationUri
import co.anitrend.navigation.AccountTaskRouter
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import timber.log.Timber
import androidx.core.net.toUri

class AuthPresenter(
    context: Context,
    settings: Settings,
    private val clientId: String,
    private val customTabs: CustomTabsIntent,
) : CorePresenter(context, settings) {
    fun onIntentData(param: AuthRouter.AuthParam?): Authentication? {
        if (param == null) {
            Timber.d("AuthRouter.Param is null, no new intent data available. Skipping checks")
            return null
        }

        Timber.d("AuthRouter.Param change triggered from on new intent: $param")

        return runCatching {
            Authentication.Authenticate(
                requireNotNull(param.accessToken),
                requireNotNull(param.tokenType),
                requireNotNull(param.expiresIn),
            )
        }.onFailure {
            Authentication.Error(
                title =
                    param.errorTitle
                        ?: context.getString(R.string.auth_error_default_title),
                message =
                    param.errorDescription
                        ?: context.getString(R.string.auth_error_default_message),
            )
        }.getOrDefault(Authentication.Idle)
    }

    fun authorizationIssues(activity: FragmentActivity) {
        // Open FAQ page with information about what to do when a user cannot log in
        val uri = context.getString(co.anitrend.android.core.R.string.app_faq_page_link).toUri()
        runCatching {
            Timber.analytics {
                logCurrentState(
                    tag = Timber.tags.action("authorization_issue"),
                    bundle =
                        bundleOf(
                            Timber.keys.DATA to UriCompat.toSafeString(uri),
                        ),
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
                    bundle =
                        bundleOf(
                            Timber.keys.DATA to UriCompat.toSafeString(uri),
                        ),
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
        val param =
            AccountTaskRouter.AccountParam(
                settings.authenticatedUserId.value,
            )
        AccountTaskRouter
            .forSignOutWorker()
            .createOneTimeUniqueWorker(context, param)
            .enqueue()
    }

    fun runSignInWorker(id: Long) {
        val param = AccountTaskRouter.AccountParam(id)
        AccountTaskRouter
            .forSignInWorker()
            .createOneTimeUniqueWorker(context, param)
            .enqueue()
    }
}

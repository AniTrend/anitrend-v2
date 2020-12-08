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
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.theme.extensions.isEnvironmentNightMode
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import co.anitrend.auth.R
import co.anitrend.auth.component.viewmodel.state.AuthState
import co.anitrend.auth.model.Authentication
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.data.arch.railway.extension.evaluate
import co.anitrend.data.auth.helper.AUTHENTICATION_URI
import co.anitrend.data.auth.helper.AuthenticationType
import co.anitrend.data.auth.helper.authenticationUri
import co.anitrend.data.auth.settings.IAuthenticationSettings

class AuthPresenter(
    context: Context,
    settings: Settings,
    private val customTabs: CustomTabsIntent,
) : CorePresenter(context, settings) {

    fun useAnonymousAccount(activity: FragmentActivity) {
        settings.isAuthenticated.value = false
        settings.authenticatedUserId.value = IAuthenticationSettings.INVALID_USER_ID
        activity.finish()
    }

    fun authorizationIssues(activity: FragmentActivity) {
        // Open FAQ page with information about what to do when a user cannot log in
        customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabs.launchUrl(activity, Uri.parse(context.getString(R.string.app_faq_page_link)))
    }

    fun authorizeWithAniList(activity: FragmentActivity, viewModelState: AuthState) {
        customTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabs.launchUrl(activity, authenticationUri(AuthenticationType.TOKEN))

        viewModelState.authenticationFlow.value = Authentication.Pending
    }

    fun onStateChange(authentication: Authentication, state: AuthState, stateLayout: SupportStateLayout) {
        when (authentication) {
            is Authentication.Authenticating -> {
                stateLayout.networkMutableStateFlow.value = NetworkState.Loading
                state(authentication)
            }
            is Authentication.Error -> stateLayout.networkMutableStateFlow.value =
                NetworkState.Error(
                    heading = authentication.title,
                    message = authentication.message
                )
            else -> { /** ignored */ }
        }
    }
}
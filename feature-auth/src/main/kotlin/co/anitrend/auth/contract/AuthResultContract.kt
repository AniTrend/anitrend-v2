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

package co.anitrend.auth.contract

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.browser.customtabs.CustomTabsIntent
import co.anitrend.auth.R
import co.anitrend.auth.model.Authentication
import co.anitrend.data.auth.helper.*
import timber.log.Timber

class AuthResultContract(
    private val resources: Resources
) : ActivityResultContract<String, Authentication>() {

    /** Create an intent that can be used for [Activity.startActivityForResult]  */
    override fun createIntent(context: Context, input: String): Intent {
        val uri = authenticationUri(AuthenticationType.TOKEN, input)
        return Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            action = Intent.ACTION_VIEW
            data = uri
        }
    }

    /** Convert result obtained from [Activity.onActivityResult] to O  */
    override fun parseResult(resultCode: Int, intent: Intent?): Authentication {
        val data = intent?.data
            ?: return Authentication.Error(
                title = resources.getString(R.string.auth_error_default_title),
                message = resources.getString(R.string.auth_error_default_message)
            )

        // APP_URL/callback#access_token=TOKEN_HERE&token_type=TOKEN_TYPE&expires_in=EXPIRES_IN_HERE
        // Why are we even using fragments :not_like:
        val uri = Uri.parse(data.toString().replaceFirst('#', '?'))

        return runCatching {
            Authentication.Authenticating(
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_KEY was not found in -> $uri"
                },
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_TYPE_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_TYPE_KEY was not found in -> $uri"
                },
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY was not found in -> $uri"
                }.toLong()
            )
        }.getOrElse {
            Timber.w(it)
            Authentication.Error(
                title = uri.getQueryParameter(CALLBACK_QUERY_ERROR_KEY)
                    ?: resources.getString(R.string.auth_error_default_title),
                message = uri.getQueryParameter(CALLBACK_QUERY_ERROR_DESCRIPTION_KEY)
                    ?: resources.getString(R.string.auth_error_default_message)
            )
        }
    }
}
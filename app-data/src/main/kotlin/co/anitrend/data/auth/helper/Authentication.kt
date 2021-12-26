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

package co.anitrend.data.auth.helper

import android.net.Uri
import co.anitrend.data.BuildConfig

const val CALLBACK_QUERY_TOKEN_KEY = "access_token"
const val CALLBACK_QUERY_TOKEN_TYPE_KEY = "token_type"
const val CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY = "expires_in"

const val CALLBACK_QUERY_ERROR_KEY = "error"
const val CALLBACK_QUERY_ERROR_DESCRIPTION_KEY = "error_description"

enum class AuthenticationType(val type: String) {
    TOKEN("token"),
    CODE("code")
}

/**
 * @param authenticationType Authentication method
 *
 * @return [Uri] of authentication provider
 */
fun authenticationUri(authenticationType: AuthenticationType, clientId: String): Uri {
    return Uri.parse(
        "https://${BuildConfig.aniListAuth}/authorize?client_id=$clientId&response_type=${authenticationType.type}"
    )
}
/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.data.android.network.cookie

import android.webkit.CookieManager
import co.anitrend.data.android.network.cookie.contract.AndroidCookieJar
import okhttp3.Cookie
import okhttp3.HttpUrl
import timber.log.Timber

class ApplicationCookieJar(
    override val cookieManager: CookieManager
) : AndroidCookieJar() {

    /**
     * Load cookies from the jar for an HTTP request to [url]. This method returns a possibly
     * empty list of cookies for the network request.
     *
     * Simple implementations will return the accepted cookies that have not yet expired and that
     * [match][Cookie.matches] [url].
     */
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieManager.getCookies(url)
    }

    /**
     * Saves [cookies] from an HTTP response to this store according to this jar's policy.
     *
     * Note that this method may be called a second time for a single HTTP response if the response
     * includes a trailer. For this obscure HTTP feature, [cookies] contains only the trailer's
     * cookies.
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieManager.setCookies(url ,cookies)
    }

    override fun purge() {
        cookieManager.removeAllCookies {
            Timber.v("Removed cookie result: $it")
        }
    }
}
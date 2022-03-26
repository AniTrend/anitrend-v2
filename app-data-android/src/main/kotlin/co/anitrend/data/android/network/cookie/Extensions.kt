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
import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * Get a list of cookies in the cookie manager
 *
 * @param url The url to lookup
 */
fun CookieManager.getCookies(url: HttpUrl): List<Cookie> {
    val key = url.toString()
    val cookies = getCookie(key)

    return if (!cookies.isNullOrBlank()) {
        val cookieList = cookies.split(';')
        cookieList.mapNotNull { Cookie.parse(url, it) }
    } else emptyList()
}

/**
 * Save a list of cookies in the manager
 *
 * @param url The url to lookup
 * @param cookies Collection of cookies to save
 */
fun CookieManager.setCookies(url: HttpUrl, cookies: List<Cookie>) {
    val key = url.toString()
    cookies.forEach {
        val cookie = it.toString()
        setCookie(key, cookie)
    }
}
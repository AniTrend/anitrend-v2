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

package co.anitrend.data.api.converter.response

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * GraphQL response body converter to unwrap nested object results,
 * resulting in a smaller generic tree for requests
 */
internal class AniGraphResponseConverter<T>(
    private val type: Type?,
    private val gson: Gson
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val response: T?
        val responseString = value.string()
        response = gson.fromJson<T>(responseString, type)
        return response
    }
}
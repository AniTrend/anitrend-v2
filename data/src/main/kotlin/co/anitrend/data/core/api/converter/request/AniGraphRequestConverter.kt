/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.core.api.converter.request

import co.anitrend.data.BuildConfig
import co.anitrend.data.core.AniTrendExperimentalFeature
import co.anitrend.data.util.GraphUtil.minify
import com.google.gson.Gson
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

internal class AniRequestConverter(
    private val gson: Gson,
) : Converter<GraphQLOperationRequest<*>, RequestBody> {
    @OptIn(AniTrendExperimentalFeature::class)
    internal fun serialize(
        value: GraphQLOperationRequest<*>,
        shrinkQuery: Boolean = !BuildConfig.DEBUG,
    ): String = gson.toJson(normalize(value, shrinkQuery))

    override fun convert(value: GraphQLOperationRequest<*>): RequestBody = serialize(value).toRequestBody(JSON_MIME_TYPE)

    @OptIn(AniTrendExperimentalFeature::class)
    private fun normalize(
        value: GraphQLOperationRequest<*>,
        shrinkQuery: Boolean,
    ): GraphQLOperationRequest<*> {
        val operationName = value.operationName.ifBlank { "<unknown>" }
        val normalizedQuery =
            value.query
                .takeIf(String::isNotBlank)
                ?.minify(shrinkQuery)
                ?: error("GraphQL request query is blank for operation: $operationName")

        return value.copy(query = normalizedQuery)
    }

    companion object {
        internal val JSON_MIME_TYPE = "application/json".toMediaType()
        internal val XML_MIME_TYPE = "application/xml".toMediaType()
    }
}

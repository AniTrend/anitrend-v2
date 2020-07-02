/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.api.converter.request

import co.anitrend.data.BuildConfig
import co.anitrend.data.arch.AniTrendExperimentalFeature
import co.anitrend.data.util.graphql.GraphUtil.minify
import com.google.gson.Gson
import io.github.wax911.library.annotation.processor.contract.AbstractGraphProcessor
import io.github.wax911.library.converter.request.GraphRequestConverter
import io.github.wax911.library.model.request.QueryContainerBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

internal class AniRequestConverter(
    methodAnnotations: Array<out Annotation>,
    processor: AbstractGraphProcessor,
    gson: Gson
) : GraphRequestConverter(methodAnnotations, processor, gson) {

    /**
     * Converter for the request body, gets the GraphQL query from the method annotation
     * and constructs a GraphQL request body to send over the network.
     *
     * @param containerBuilder The constructed builder method of your query with variables
     */
    @AniTrendExperimentalFeature
    override fun convert(containerBuilder: QueryContainerBuilder): RequestBody {
        val query = graphProcessor.getQuery(methodAnnotations)?.minify(BuildConfig.DEBUG)
        val queryContainer = containerBuilder.setQuery(query).build()

        val queryJson = gson.toJson(queryContainer)
        return queryJson.toRequestBody(MIME_TYPE)
    }

    companion object {
        internal val MIME_TYPE = "application/json".toMediaTypeOrNull()
    }
}
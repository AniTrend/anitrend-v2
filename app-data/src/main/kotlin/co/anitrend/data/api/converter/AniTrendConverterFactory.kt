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

package co.anitrend.data.api.converter

import co.anitrend.data.api.converter.request.AniRequestConverter
import co.anitrend.data.arch.GRAPHQL
import co.anitrend.data.arch.JSON
import co.anitrend.data.arch.XML
import com.google.gson.Gson
import io.github.wax911.library.annotation.processor.contract.AbstractGraphProcessor
import io.github.wax911.library.converter.GraphConverter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

internal class AniTrendConverterFactory(
    private val jsonFactory: Converter.Factory,
    private val xmlFactory: Converter.Factory,
    processor: AbstractGraphProcessor,
    gson: Gson
) : GraphConverter(processor, gson) {

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     *
     * @param parameterAnnotations All the annotation applied to request parameters
     * @param methodAnnotations All the annotation applied to the requesting method
     * @param retrofit The retrofit object representing the response
     * @param type The type of the parameter of the request
     *
     * @see AniRequestConverter
     */
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        for (annotation in methodAnnotations) {
            when (annotation) {
                is XML -> {
                    return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
                }
                is GRAPHQL -> {
                    return AniRequestConverter(methodAnnotations, graphProcessor, gson)
                }
                is JSON -> {
                    return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
                }
            }
        }
        return GsonConverterFactory.create(gson).requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     *
     * @param annotations All the annotation applied to the requesting Call method
     * @param retrofit The retrofit object representing the response
     * @param type The generic type declared on the Call method
     *
     * @see Retrofit
     * @see GraphConverter.responseBodyConverter
     */
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            when (annotation) {
                is XML -> {
                    return xmlFactory.responseBodyConverter(type, annotations, retrofit)
                }
                is JSON, is GRAPHQL -> {
                    return jsonFactory.responseBodyConverter(type, annotations, retrofit)
                }
            }
        }
        return GsonConverterFactory.create(gson).responseBodyConverter(type, annotations, retrofit)
    }
}
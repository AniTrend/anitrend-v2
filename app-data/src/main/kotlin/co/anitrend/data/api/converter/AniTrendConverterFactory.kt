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

import android.content.Context
import co.anitrend.data.api.converter.request.AniRequestConverter
import co.anitrend.data.arch.JSON
import co.anitrend.data.arch.XML
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.github.wax911.library.converter.GraphConverter
import io.github.wax911.library.model.request.QueryContainerBuilder
import io.github.wax911.library.util.LogLevel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

internal class AniTrendConverterFactory(
    context: Context
) : GraphConverter(context) {

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
        type: Type?,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit?
    ): Converter<QueryContainerBuilder, RequestBody>? =
        AniRequestConverter(
            methodAnnotations = methodAnnotations,
            graphProcessor = graphProcessor,
            gson = gson
        )

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     *
     * @param annotations All the annotation applied to the requesting Call method
     * @param retrofit The retrofit object representing the response
     * @param type The generic type declared on the Call method
     *
     * @see Retrofit
     * @see io.github.wax911.library.converter.response.GraphResponseConverter
     */
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            when (annotation) {
                // TODO: Replace this with kotlinx-serializer
                /*is XML -> {
                    return SimpleXmlConverterFactory.createNonStrict(
                        Persister(AnnotationStrategy())
                    ).responseBodyConverter(type!!, annotations, retrofit)
                }*/
                is JSON -> {
                    return GsonConverterFactory.create(GSON)
                        .responseBodyConverter(type!!, annotations, retrofit)
                }
            }
        }
        // Default for GraphQl marked queries
        return super.responseBodyConverter(type, annotations, retrofit)
    }

    companion object {
        private val GSON = GsonBuilder()
            .generateNonExecutableJson()
            .setLenient()
            .create()

        private val exclusionStrategy = object : ExclusionStrategy {
            /**
             * @param clazz the class object that is under test
             * @return true if the class should be ignored; otherwise false
             */
            override fun shouldSkipClass(clazz: Class<*>?) = false

            /**
             * @param f the field object that is under test
             * @return true if the field should be ignored; otherwise false
             */
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.name?.equals("operationName") ?: false
                        ||
                        f?.name?.equals("extensions") ?: false
            }
        }

        /**
         * Allows you to provide your own Gson configuration which will be used when serialize or
         * deserialize response and request bodies.
         *
         * @param context any valid application context
         */
        fun create(context: Context) =
            AniTrendConverterFactory(context).apply {
                gson = GsonBuilder()
                    .addSerializationExclusionStrategy(exclusionStrategy)
                    .generateNonExecutableJson()
                    .setLenient()
                    .create()
                setLogLevel(LogLevel.ERROR)
            }
    }
}
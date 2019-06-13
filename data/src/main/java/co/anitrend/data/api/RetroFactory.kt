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

package co.anitrend.data.api

import co.anitrend.data.BuildConfig
import co.anitrend.data.api.interceptor.AuthInterceptor
import co.anitrend.data.api.interceptor.ClientInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.wax911.library.converter.GraphConverter
import io.wax911.support.data.factory.contract.IRetrofitFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Retrofit factory provides a Gson instance and creates endpoint services
 */
class RetroFactory(
    baseUrl: String = BuildConfig.apiUrl,
    authInterceptor: AuthInterceptor?,
    graphConverter: GraphConverter
) : IRetrofitFactory {


    private val retrofit: Retrofit by lazy {
        val httpClient = createHttpClient(
            HttpLoggingInterceptor.Level.BODY
        )

        if (authInterceptor != null)
            httpClient.authenticator(authInterceptor)

        Retrofit.Builder().client(
            httpClient.build()
        ).addConverterFactory(
            graphConverter
        ).baseUrl(
            baseUrl
        ).build()
    }

    /**
     * Creates a standard HttpBuilder with most common likely used configuration and optionally
     * will include http logging based off a given log level.
     * @see HttpLoggingInterceptor.level
     *
     * @param logLevel Mandatory log level that the logging http interceptor should use
     */
    private fun createHttpClient(logLevel: HttpLoggingInterceptor.Level): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(35, TimeUnit.SECONDS)
                .connectTimeout(35, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(ClientInterceptor())
        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                        .setLevel(logLevel)
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }
        return okHttpClientBuilder
    }

    /**
     * Generates retrofit service classes
     *
     * @param serviceClass The interface class method representing your request to use
     */
    override fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    companion object {
        val gson: Gson by lazy {
            GsonBuilder()
                .enableComplexMapKeySerialization()
                .generateNonExecutableJson()
                .serializeNulls()
                .setLenient()
                .create()
        }
    }
}
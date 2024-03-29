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

package co.anitrend.data.relation.api

import co.anitrend.data.android.network.cache.CacheHelper
import co.anitrend.data.core.api.factory.IEndpointFactory
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.core.extensions.defaultBuilder
import co.anitrend.data.relation.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

internal class RelationApiFactory : IEndpointFactory {
    override val endpointType = object : IEndpointType {
        override val url: HttpUrl = BuildConfig.relationYunaMoe.toHttpUrl()
    }

    override fun okHttpConfig(scope: Scope): OkHttpClient {
        val builder = scope.defaultBuilder()
        builder.cache(
            CacheHelper.createCache(
                scope.androidContext(),
                "relation"
            )
        )
        return builder.build()
    }
}
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

package co.anitrend.data.trakt.koin

import co.anitrend.data.android.network.cache.CacheHelper
import co.anitrend.data.trakt.BuildConfig
import co.anitrend.data.trakt.api.TraktApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val coreModule = module {
    single {
        TraktApi(
            apiKey = getProperty("traktClientId"),
            clientSecret = getProperty("traktClientSecret"),
            redirectUri = BuildConfig.traktRedirect,
            isStaging = false,
            cache = CacheHelper.createCache(
                androidContext(),
                "trakt"
            )
        )
    }
}

val traktModules = listOf(coreModule)
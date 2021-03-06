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

package co.anitrend.data

import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.arch.extension.dispatchers.SupportDispatcher
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.airing.converters.AiringModelConverter
import co.anitrend.data.airing.mapper.detail.AiringScheduleMapper
import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.extension.db
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.mapper.paged.MediaPagedCombinedMapper
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockWebServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module

private val data = module {
    single<ISupportDispatcher> {
        SupportDispatcher()
    }
    single {
        MockWebServer()
    }
    single {
        Json {
            coerceInputValues = true
            isLenient = true
        }
    }
}

private val store = module {
    single {
        AniTrendStore.create(
            androidContext(),
            emptyArray()
        )
    } binds IAniTrendStore.BINDINGS
}

private val provider = module {
    factory {
        androidContext().contentResolver
    }
}

private val mapper = module {
    single {
        AiringScheduleMapper(
            localSource = db().airingScheduleDao(),
            converter = get()
        )
    }
    single {
        MediaPagedCombinedMapper(
            localSource = db().mediaDao(),
            converter = get(),
            airingConverter = get(),
            airingLocalSource = db().airingScheduleDao(),
            context = get<ISupportDispatcher>().io
        )
    }
}

private val converter = module {
    factory {
        MediaModelConverter()
    }
    factory {
        AiringModelConverter()
    }
}

internal val testModules = listOf(data, store, provider, mapper, converter)

internal fun initializeKoin() = startKoin {
    androidContext(
        InstrumentationRegistry.getInstrumentation().context
    )
    modules(testModules)
}
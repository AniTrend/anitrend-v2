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

package co.anitrend.data.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.data.api.RetroFactory
import co.anitrend.data.api.converter.AniGraphConverter
import co.anitrend.data.api.interceptor.AuthInterceptor
import co.anitrend.data.auth.AuthenticationHelper
import co.anitrend.data.dao.DatabaseHelper
import co.anitrend.data.util.Settings
import io.wax911.support.data.auth.contract.ISupportAuthentication
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.extension.util.SupportConnectivityHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModules = module {
    factory {
        Settings(
            context = androidContext()
        )
    }

    single {
        DatabaseHelper.newInstance(
            applicationContext = androidContext()
        )
    }

    factory<ISupportAuthentication> {
        AuthenticationHelper(
            connectivityHelper = get(),
            jsonWebTokenDao = get<DatabaseHelper>().jsonWebTokenDao(),
            settings = get()
        )
    }
}

val dataNetworkModules = module {
    factory {
        AuthInterceptor(
            authenticationHelper = get()
        )
    }

    factory {
        SupportConnectivityHelper(
            androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager?
        )
    }

    factory {
        AniGraphConverter.create(
            context = androidContext()
        )
    }

    single<IRetrofitFactory> {
        RetroFactory(
            authInterceptor = get<AuthInterceptor>(),
            graphConverter = get<AniGraphConverter>()
        )
    }
}

val dataRepositoryModules = module {

}
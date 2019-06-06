package co.anitrend.data.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.data.api.NetworkClient
import co.anitrend.data.api.RetroFactory
import co.anitrend.data.api.converter.AniGraphConverter
import co.anitrend.data.api.interceptor.AuthInterceptor
import co.anitrend.data.auth.AuthenticationHelper
import co.anitrend.data.dao.DatabaseHelper
import co.anitrend.data.util.Settings
import io.wax911.support.data.auth.contract.ISupportAuthentication
import io.wax911.support.data.controller.contract.ISupportRequestClient
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
    factory<ISupportRequestClient> {
        NetworkClient()
    }

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
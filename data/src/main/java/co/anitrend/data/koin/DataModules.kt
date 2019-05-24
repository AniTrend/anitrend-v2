package co.anitrend.data.koin

import co.anitrend.data.api.NetworkClient
import co.anitrend.data.auth.AuthenticationHelper
import co.anitrend.data.auth.contract.IAuthenticationHelper
import co.anitrend.data.dao.DatabaseHelper
import co.anitrend.data.util.Settings
import io.wax911.support.core.controller.SupportRequestClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModules = module {

    factory<IAuthenticationHelper> {
        AuthenticationHelper(
            databaseHelper = get()
        )
    }

    factory {
        Settings.newInstance(
            arg = androidContext()
        )
    }

    factory<SupportRequestClient> {
        NetworkClient()
    }

    single {
        DatabaseHelper.newInstance(
            arg = androidContext()
        )
    }
}

val dataRepositoryModules = module {

}
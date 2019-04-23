package co.anitrend.core.koin

import co.anitrend.core.api.NetworkClient
import co.anitrend.core.auth.AuthenticationHelper
import co.anitrend.core.auth.contract.IAuthenticationHelper
import co.anitrend.core.dao.DatabaseHelper
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.util.Settings
import io.wax911.support.core.controller.SupportRequestClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModules = module {
    factory<SupportRequestClient> { NetworkClient() }
    factory { Settings.newInstance(androidContext()) }
    single { DatabaseHelper.newInstance(androidContext()) }
    factory<IAuthenticationHelper> { AuthenticationHelper(get()) }
}

val corePresenterModules = module {
    factory { CorePresenter(androidContext()) }
}

val coreViewModelModules = module {

}

val coreRepositoryModules = module {

}
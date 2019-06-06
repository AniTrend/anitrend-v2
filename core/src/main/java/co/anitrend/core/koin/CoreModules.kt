package co.anitrend.core.koin

import co.anitrend.core.presenter.CorePresenter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModules = module {

}

val corePresenterModules = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

val coreViewModelModules = module {

}
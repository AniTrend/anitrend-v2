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

package co.anitrend.search.koin

import co.anitrend.search.presenter.SearchPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


private val presenterModule = module {
    factory {
        SearchPresenter(
            context = androidContext(),
            settings = get()
        )
    }
}

private val viewModelModule = module {

}

private val modules = listOf(presenterModule)

private val koinModules by lazy {
    loadKoinModules(modules)
}

/**
 * loads koin modules with the current
 */
fun injectFeatureModules() = koinModules
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

package co.anitrend.core.koin

import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.viewmodel.MediaGenreViewModel
import co.anitrend.core.viewmodel.MediaTagViewModel
import co.anitrend.data.usecase.media.MediaGenreUseCaseImpl
import co.anitrend.data.usecase.media.MediaTagUseCaseImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val commonAppModules = module {

}

private val presenterModules = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

private val viewModelModules = module {
    viewModel {
        MediaGenreViewModel(
            useCase = get<MediaGenreUseCaseImpl>()
        )
    }

    viewModel {
        MediaTagViewModel(
            useCase = get<MediaTagUseCaseImpl>()
        )
    }
}

val coreModules = listOf(commonAppModules, presenterModules, viewModelModules)
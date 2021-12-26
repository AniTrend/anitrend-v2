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

package co.anitrend.data.account.koin

import co.anitrend.data.account.AccountInteractor
import co.anitrend.data.account.repository.AccountRepositoryImpl
import co.anitrend.data.account.usecase.AccountUseCaseImpl
import org.koin.dsl.module

private val useCaseModule = module {
    factory<AccountInteractor> {
        AccountUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        AccountRepositoryImpl(
            source = get()
        )
    }
}

internal val accountModules = listOf(
    useCaseModule, repositoryModule
)
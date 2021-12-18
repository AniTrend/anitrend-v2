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

package co.anitrend.data.staff.koin

import co.anitrend.data.staff.converter.StaffConverter
import co.anitrend.data.staff.converter.StaffEntityConverter
import co.anitrend.data.staff.converter.StaffModelConverter
import org.koin.dsl.module

private val sourceModule = module {

}

private val converterModule = module {
    factory {
        StaffConverter()
    }
    factory {
        StaffModelConverter()
    }
    factory {
        StaffEntityConverter()
    }
}

private val mapperModule = module {

}

private val useCaseModule = module {

}

private val repositoryModule = module {

}

internal val staffModules = listOf(
    sourceModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)
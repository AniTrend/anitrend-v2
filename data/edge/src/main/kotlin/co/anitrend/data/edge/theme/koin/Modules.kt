/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.theme.koin

import co.anitrend.data.edge.core.extensions.edgeStore
import co.anitrend.data.edge.theme.converter.EdgeThemeConverter
import co.anitrend.data.edge.theme.mapper.EdgeThemeMapper
import org.koin.dsl.module

internal val edgeThemeModule =
    module {
        factory { EdgeThemeConverter() }
        factory {
            EdgeThemeMapper(
                localSource = edgeStore().edgeThemeDao(),
                converter = get(),
            )
        }
    }

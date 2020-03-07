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

package co.anitrend.data.extensions

import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.arch.AniTrendExperimentalFeature
import co.anitrend.data.arch.controller.GraphQLController
import co.anitrend.data.arch.mapper.GraphQLMapper
import com.google.gson.reflect.TypeToken
import org.koin.core.context.GlobalContext
import java.lang.reflect.Type

/**
 * Extension to get any type of given generic type
 */
internal inline fun <reified T> typeTokenOf(): Type =
    object : TypeToken<T>() {}.type

@AniTrendExperimentalFeature
internal inline fun <reified T> koinOf() =
    GlobalContext().get().get<T>()

/**
 * Extension to help us create a controller from a a mapper instance
 */
internal fun <S, D> GraphQLMapper<S, D>.controller(
    connectivity: SupportConnectivity,
    dispatchers: SupportDispatchers
) =
    GraphQLController.newInstance(
        responseMapper = this,
        supportConnectivity = connectivity,
        supportDispatchers = dispatchers
    )
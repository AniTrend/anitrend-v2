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

package co.anitrend.data.arch.mapper

import co.anitrend.arch.data.mapper.SupportResponseMapper
import co.anitrend.data.arch.railway.OutCome
import io.github.wax911.library.model.body.GraphContainer
import timber.log.Timber

/**
 * GraphQLMapper specific mapper, assures that all requests respond with [GraphContainer] as the root tree object.
 * Making it easier for us to implement error logging and provide better error messages
 */
internal abstract class GraphQLMapper<S, D> : SupportResponseMapper<S, D>() {

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    protected open suspend fun persistChanges(data: D): OutCome<Nothing?> {
        // optional
        return OutCome.Pass(null)
    }

    /**
     * If [data] is a type of [Collection], checks if empty otherwise checks nullability
     */
    protected fun <T> checkValidity(data: T?): OutCome<T> {
        if (data == null)
            return OutCome.Fail(
                listOf(Throwable("Data validity failed data is null"))
            )

        if (data is Collection<*> && data.isEmpty())
            return OutCome.Fail(
                listOf(Throwable("Data validity failed collection is empty"))
            )

        return OutCome.Pass(data)
    }

    /**
     * Logs [exceptions] of failed operations
     */
    protected fun handleException(exceptions: List<Throwable>) {
        exceptions.forEach {
            Timber.tag(moduleTag).w(it, "Unhandled exception thrown")
        }
    }
}
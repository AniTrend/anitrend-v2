/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.thexem.single.model.local

/**
 * @property origin Source the query is from e.g. 'anidb' or 'tvdb'
 */
internal sealed class XemQuery {
    abstract val origin: String

    /**
     * Mapping information about a series
     *
     * @param id Id of the [origin]
     */
    data class All(
        val id: Long,
        override val origin: String = "anidb",
    ) : XemQuery()

    /**
     * Mapping information about a series
     *
     * @param id Id of the [origin]
     * @param season Seasons number to get mappings for
     * @param episode Episode number to get mappings for
     */
    data class Single(
        val id: Long,
        val season: Int,
        val episode: Int? = null,
        override val origin: String = "anidb",
    ) : XemQuery()

    /**
     * @param language A language string like 'us' or 'jp' default is 'all'
     * @param defaultNames Should the default names be added to the list: 1(yes) or 0(no)
     */
    data class Names(
        val language: String = "all",
        val defaultNames: Int = 1,
        override val origin: String = "anidb",
    ) : XemQuery()
}

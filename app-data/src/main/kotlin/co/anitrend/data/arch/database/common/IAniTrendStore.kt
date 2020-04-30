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

package co.anitrend.data.arch.database.common

import co.anitrend.data.auth.datasource.local.JsonWebTokenLocalSource
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource

interface IAniTrendStore {
    fun jsonWebTokenDao(): JsonWebTokenLocalSource
    fun mediaTagDao(): MediaTagLocalSource
    fun mediaGenreDao(): MediaGenreLocalSource
}
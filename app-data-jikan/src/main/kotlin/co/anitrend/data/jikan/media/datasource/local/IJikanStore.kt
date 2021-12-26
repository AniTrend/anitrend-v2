/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.jikan.media.datasource.local

import co.anitrend.data.jikan.author.datasource.local.JikanAuthorLocalSource
import co.anitrend.data.jikan.licensor.datasource.local.JikanLicensorLocalSource
import co.anitrend.data.jikan.producer.datasource.local.JikanProducerLocalSource
import co.anitrend.data.jikan.studio.datasource.local.JikanStudioLocalSource

interface IJikanStore {
    fun jikanDao(): JikanLocalSource
    fun jikanAuthorDao(): JikanAuthorLocalSource
    fun jikanLicensorDao(): JikanLicensorLocalSource
    fun jikanProducerDao(): JikanProducerLocalSource
    fun jikanStudioDao(): JikanStudioLocalSource
}
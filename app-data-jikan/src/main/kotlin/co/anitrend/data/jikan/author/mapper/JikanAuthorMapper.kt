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

package co.anitrend.data.jikan.author.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.jikan.author.datasource.local.JikanAuthorLocalSource
import co.anitrend.data.jikan.author.entity.JikanAuthorEntity
import co.anitrend.data.jikan.contract.JikanItem
import co.anitrend.data.jikan.media.converters.JikanAuthorModelConverter
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel

internal sealed class JikanAuthorMapper : DefaultMapper<JikanMediaModel, JikanAuthorEntity>() {

    class Embed(
        override val localSource: JikanAuthorLocalSource,
        override val converter: JikanAuthorModelConverter
    ) : EmbedMapper<JikanItem, JikanAuthorEntity>()
}
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
package co.anitrend.data.character.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.character.converter.CharacterModelConverter
import co.anitrend.data.character.datasource.local.CharacterLocalSource
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.model.container.CharacterContainer

internal sealed class CharacterMapper<S, D> : DefaultMapper<S, D>() {
    protected abstract val localSource: CharacterLocalSource
    protected abstract val converter: CharacterModelConverter

    class Paged(
        override val localSource: CharacterLocalSource,
        override val converter: CharacterModelConverter,
    ) : CharacterMapper<CharacterContainer.Paged, List<CharacterEntity>>() {
        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(source: CharacterContainer.Paged): List<CharacterEntity> =
            converter.convertFrom(source.page.characters)

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<CharacterEntity>) {
            localSource.upsert(data)
        }
    }
}

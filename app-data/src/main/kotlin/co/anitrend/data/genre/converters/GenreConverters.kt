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

package co.anitrend.data.genre.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.domain.genre.entity.Genre
import io.wax911.emojify.manager.IEmojiManager

internal class GenreEntityConverter(
    override val fromType: (GenreEntity) -> Genre = ::transform,
    override val toType: (Genre) -> GenreEntity = { throw NotImplementedError() }
) : SupportConverter<GenreEntity, Genre>() {
    private companion object : ISupportTransformer<GenreEntity, Genre> {
        override fun transform(source: GenreEntity) = Genre.Core(
            name = source.genre,
            emoji = source.emoji,
            id = source.id
        )
    }
}

internal class GenreModelConverter(
    emojiManager: IEmojiManager,
    override val fromType: (String) -> GenreEntity = { transform(it, emojiManager) },
    override val toType: (GenreEntity) -> String = { throw NotImplementedError() }
) : SupportConverter<String, GenreEntity>() {

    private companion object {

        fun IEmojiManager.withAlias(alias: String): String {
            val emoji = getForAlias(alias)
            return emoji?.emoji.orEmpty()
        }

        fun IEmojiManager.getEmojiFor(genre: String): String {
            return when (genre) {
                "Action" -> withAlias("cowboy")
                "Adventure" -> withAlias("rocket")
                "Comedy" -> withAlias("rofl")
                "Drama" -> withAlias("speak_no_evil")
                "Ecchi" -> withAlias("smirk")
                "Fantasy" -> withAlias("art")
                "Hentai" -> withAlias("eggplant")
                "Horror" -> withAlias("scream")
                "Mahou Shoujo" -> withAlias("woman_mage")
                "Mecha" -> withAlias("robot_face")
                "Music" -> withAlias("saxophone")
                "Mystery" -> withAlias("detective")
                "Psychological" -> withAlias("weary")
                "Romance" -> withAlias("couplekiss")
                "Sci-Fi" -> withAlias("space_invader")
                "Slice of Life" -> withAlias("couple")
                "Sports" -> withAlias("basketball")
                "Supernatural" -> withAlias("ghost")
                "Thriller" -> withAlias("dagger")
                else -> withAlias("question")
            }
        }

        fun transform(source: String, emojiManager: IEmojiManager) = GenreEntity(
            genre = source,
            emoji = emojiManager.getEmojiFor(source)
        )
    }
}
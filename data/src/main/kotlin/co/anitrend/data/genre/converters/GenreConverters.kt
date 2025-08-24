/*
 * Copyright (C) 2020 AniTrend
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
import co.anitrend.data.genre.model.GenreCollection
import co.anitrend.domain.genre.entity.Genre
import io.wax911.emojify.EmojiManager

internal class GenreEntityConverter(
    override val fromType: (GenreEntity) -> Genre = ::transform,
    override val toType: (Genre) -> GenreEntity = { throw NotImplementedError() },
) : SupportConverter<GenreEntity, Genre>() {
    private companion object : ISupportTransformer<GenreEntity, Genre> {
        override fun transform(source: GenreEntity) =
            Genre.Core(
                name = source.genre,
                emoji = source.emoji,
                id = source.id,
            )
    }
}

internal class GenreModelConverter(
    emojiManager: EmojiManager,
    override val fromType: (GenreCollection.GenreModel) -> GenreEntity = { transform(it, emojiManager) },
    override val toType: (GenreEntity) -> GenreCollection.GenreModel = { throw NotImplementedError() },
) : SupportConverter<GenreCollection.GenreModel, GenreEntity>() {
    private companion object {
        fun EmojiManager.withShortCode(alias: String): String {
            val emoji = getForTag(alias)?.firstOrNull()
            return emoji?.emoji.orEmpty()
        }

        fun EmojiManager.getEmojiFor(genre: String): String =
            when (genre) {
                "Action" -> withShortCode("cowboy")
                "Adventure" -> withShortCode("rocket")
                "Comedy" -> withShortCode("rofl")
                "Drama" -> withShortCode("speak_no_evil")
                "Ecchi" -> withShortCode("smirk")
                "Fantasy" -> withShortCode("art")
                "Hentai" -> withShortCode("eggplant")
                "Horror" -> withShortCode("scream")
                "Mahou Shoujo" -> withShortCode("woman_mage")
                "Mecha" -> withShortCode("robot_face")
                "Music" -> withShortCode("saxophone")
                "Mystery" -> withShortCode("detective")
                "Psychological" -> withShortCode("weary")
                "Romance" -> withShortCode("couplekiss")
                "Sci-Fi" -> withShortCode("space_invader")
                "Slice of Life" -> withShortCode("couple")
                "Sports" -> withShortCode("basketball")
                "Supernatural" -> withShortCode("ghost")
                "Thriller" -> withShortCode("dagger")
                else -> withShortCode("question")
            }

        fun transform(
            source: GenreCollection.GenreModel,
            emojiManager: EmojiManager,
        ) = GenreEntity(
            id = source.id,
            genre = source.genre,
            // TODO: when we update to android-emojify 2.1.0 change this to `emojiManage.getShortCodeFor`
            emoji =
                emojiManager.getEmojiFor(
                    source.genre,
                ),
        )
    }
}

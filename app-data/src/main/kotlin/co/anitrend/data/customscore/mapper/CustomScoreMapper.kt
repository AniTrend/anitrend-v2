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

package co.anitrend.data.customscore.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.customscore.datasource.CustomScoreLocalSource
import co.anitrend.data.customscore.entity.CustomScoreEntity
import co.anitrend.data.medialist.model.MediaListModel

internal class CustomScoreMapper(
    override val localSource: CustomScoreLocalSource
) : EmbedMapper<CustomScoreMapper.Item, CustomScoreEntity>() {

    override val converter = object : SupportConverter<Item, CustomScoreEntity>() {
        /**
         * Function reference from converting from [M] to [E] which will
         * be called by [convertFrom]
         */
        override val fromType: (Item) -> CustomScoreEntity = {
            CustomScoreEntity(
                score = it.score,
                scoreName = it.scoreName,
                mediaListId = it.mediaListId,
                userId = it.userId
            )
        }

        /**
         * Function reference from converting from [E] to [M] which will
         * be called by [convertTo]
         */
        override val toType: (CustomScoreEntity) -> Item
            get() = throw NotImplementedError()

    }

    data class Item(
        val score: Float,
        val scoreName: String,
        val mediaListId: Long,
        val userId: Long
    )

    companion object {
        fun asItem(source: MediaListModel) =
            source.advancedScores?.map {
                Item(
                    score = it.value,
                    scoreName = it.key,
                    mediaListId = source.id,
                    userId = source.userId
                )
            }.orEmpty()

        fun asItem(source: List<MediaListModel>) =
            source.flatMap { mediaList ->
                asItem(mediaList)
            }
    }
}
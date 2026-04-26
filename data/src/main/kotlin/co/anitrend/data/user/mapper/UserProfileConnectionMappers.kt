/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.user.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.user.datasource.local.connection.UserProfileFavouriteMediaLocalSource
import co.anitrend.data.user.datasource.local.connection.UserProfileReviewLocalSource
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaType

internal sealed class UserProfileConnectionMapper {
    internal class FavouriteEmbed(
        override val localSource: UserProfileFavouriteMediaLocalSource,
    ) : EmbedMapper<FavouriteEmbed.Item, UserProfileFavouriteMediaEntity>() {
        override val converter =
            object : SupportConverter<Item, UserProfileFavouriteMediaEntity>() {
                override val fromType: (Item) -> UserProfileFavouriteMediaEntity = { item ->
                    UserProfileFavouriteMediaEntity(
                        userId = item.userId,
                        mediaId = item.mediaId,
                        category = item.category,
                        sortIndex = item.sortIndex,
                    )
                }

                override val toType: (UserProfileFavouriteMediaEntity) -> Item
                    get() = throw NotImplementedError()
            }

        internal data class Item(
            val userId: Long,
            val mediaId: Long,
            val category: MediaType,
            val sortIndex: Int,
        )
    }

    internal class ReviewEmbed(
        override val localSource: UserProfileReviewLocalSource,
    ) : EmbedMapper<ReviewEmbed.Item, UserProfileReviewEntity>() {
        override val converter =
            object : SupportConverter<Item, UserProfileReviewEntity>() {
                override val fromType: (Item) -> UserProfileReviewEntity = { item ->
                    UserProfileReviewEntity(
                        userId = item.userId,
                        reviewId = item.reviewId,
                        sortIndex = item.sortIndex,
                        mediaId = item.mediaId,
                    )
                }

                override val toType: (UserProfileReviewEntity) -> Item
                    get() = throw NotImplementedError()
            }

        internal data class Item(
            val userId: Long,
            val reviewId: Long,
            val sortIndex: Int,
            val mediaId: Long,
        )
    }
}

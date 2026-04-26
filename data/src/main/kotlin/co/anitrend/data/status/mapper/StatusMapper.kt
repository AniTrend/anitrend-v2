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
package co.anitrend.data.status.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal sealed class StatusMapper {
    internal sealed class Activity : StatusMapper() {
        internal class Embed(
            override val localSource: StatusLocalSource,
        ) : EmbedMapper<Embed.Item, StatusEntity.ListStatus>() {
            override val converter =
                object : SupportConverter<Item, StatusEntity.ListStatus>() {
                    override val fromType: (Item) -> StatusEntity.ListStatus = { item ->
                        with(item.activity) {
                            StatusEntity.ListStatus(
                                id = id,
                                userId = item.userId,
                                sortIndex = item.sortIndex,
                                createdAt = createdAt,
                                status = status,
                                progress = progress,
                                siteUrl = siteUrl,
                                type = type,
                                mediaId = media?.id,
                            )
                        }
                    }

                    override val toType: (StatusEntity.ListStatus) -> Item
                        get() = throw NotImplementedError()
                }

            internal data class Item(
                val userId: Long,
                val sortIndex: Int,
                val activity: UserSidecarModelContainer.ListActivityPayload,
            )

            companion object {
                fun asItems(
                    userId: Long,
                    source: kotlin.collections.List<UserSidecarModelContainer.ListActivityPayload>,
                ) = source.mapIndexed { index, activity ->
                    Item(
                        userId = userId,
                        sortIndex = index,
                        activity = activity,
                    )
                }
            }
        }
    }
}

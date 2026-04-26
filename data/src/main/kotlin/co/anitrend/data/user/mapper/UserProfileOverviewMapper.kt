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

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.status.mapper.StatusMapper
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.domain.media.enums.MediaType

internal class UserProfileOverviewMapper(
    private val favouriteEmbedMapper: UserProfileConnectionMapper.FavouriteEmbed,
    private val statusEmbedMapper: StatusMapper.Activity.Embed,
    private val mediaEmbedMapper: EmbedMapper<MediaModel, MediaEntity>,
    private val transactionRunner: TransactionRunner,
) : DefaultMapper<UserSidecarModelContainer.Overview, Unit>() {
    override suspend fun persist(data: Unit) {
        mediaEmbedMapper.persistEmbedded()
        favouriteEmbedMapper.persistEmbedded()
        statusEmbedMapper.persistEmbedded()
    }

    override suspend fun onResponseDatabaseInsert(mappedData: Unit) {
        transactionRunner.run {
            super.onResponseDatabaseInsert(mappedData)
        }
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Overview) {
        val userId = requireNotNull(source.user?.id) { "Overview response missing user id" }

        val animeEdges =
            source.user.favourites
                ?.anime
                ?.edges
                .orEmpty()
                .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }
        val mangaEdges =
            source.user.favourites
                ?.manga
                ?.edges
                .orEmpty()
                .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }

        val mediaNodes =
            buildList {
                addAll(animeEdges.mapNotNull { it.node })
                addAll(mangaEdges.mapNotNull { it.node })
                addAll(
                    source.page
                        ?.activities
                        .orEmpty()
                        .mapNotNull { it.media },
                )
            }
        mediaEmbedMapper.onEmbedded(mediaNodes)

        favouriteEmbedMapper.onEmbedded(
            buildList {
                animeEdges.forEachIndexed { index, edge ->
                    val node = edge.node ?: return@forEachIndexed
                    add(
                        UserProfileConnectionMapper.FavouriteEmbed.Item(
                            userId = userId,
                            mediaId = node.id,
                            category = MediaType.ANIME,
                            sortIndex = index,
                        ),
                    )
                }
                mangaEdges.forEachIndexed { index, edge ->
                    val node = edge.node ?: return@forEachIndexed
                    add(
                        UserProfileConnectionMapper.FavouriteEmbed.Item(
                            userId = userId,
                            mediaId = node.id,
                            category = MediaType.MANGA,
                            sortIndex = index,
                        ),
                    )
                }
            },
        )

        statusEmbedMapper.onEmbedded(
            StatusMapper.Activity.Embed.asItems(
                userId = userId,
                source = source.page?.activities.orEmpty(),
            ),
        )
    }
}

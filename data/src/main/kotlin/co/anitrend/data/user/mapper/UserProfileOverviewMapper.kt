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

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.datasource.local.connection.UserProfileFavouriteMediaLocalSource
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal class UserProfileOverviewMapper(
    private val favouriteMediaLocalSource: UserProfileFavouriteMediaLocalSource,
    private val statusLocalSource: StatusLocalSource,
) : DefaultMapper<UserSidecarModelContainer.Overview, Unit>() {

    private var pendingFavourites: List<UserProfileFavouriteMediaEntity> = emptyList()
    private var pendingActivities: List<StatusEntity.ListStatus> = emptyList()

    override suspend fun persist(data: Unit) {
        favouriteMediaLocalSource.upsert(pendingFavourites)
        statusLocalSource.upsert(pendingActivities)
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Overview): Unit {
        val userId = requireNotNull(source.user?.id) { "Overview response missing user id" }

        val animeEdges = source.user.favourites?.anime?.edges.orEmpty()
            .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }
        val mangaEdges = source.user.favourites?.manga?.edges.orEmpty()
            .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }

        pendingFavourites = buildList {
            animeEdges.forEachIndexed { index, edge ->
                val node = edge.node ?: return@forEachIndexed
                add(node.toFavouriteEntity(userId, "ANIME", index))
            }
            mangaEdges.forEachIndexed { index, edge ->
                val node = edge.node ?: return@forEachIndexed
                add(node.toFavouriteEntity(userId, "MANGA", index))
            }
        }

        pendingActivities = source.page?.activities.orEmpty().mapIndexed { index, activity ->
            activity.toListStatusEntity(userId, index)
        }
    }

    companion object {
        internal fun UserSidecarModelContainer.MediaPreviewPayload.toFavouriteEntity(
            userId: Long,
            category: String,
            sortIndex: Int,
        ) = UserProfileFavouriteMediaEntity(
            userId = userId,
            mediaId = id,
            category = category,
            sortIndex = sortIndex,
            titleRomaji = title?.romaji,
            titleEnglish = title?.english,
            titleNative = title?.nativeTitle,
            titleUserPreferred = title?.userPreferred,
            coverColor = image?.color,
            coverLarge = image?.large,
            coverMedium = image?.medium,
            type = type,
            format = format,
            status = status,
            episodes = episodes,
            chapters = chapters,
            volumes = volumes,
            isFavourite = isFavourite,
            meanScore = meanScore,
            averageScore = averageScore,
            siteUrl = siteUrl,
            mediaListStatus = mediaList?.status,
            mediaListProgress = mediaList?.progress,
            mediaListVolumeProgress = mediaList?.progressVolumes,
        )

        internal fun UserSidecarModelContainer.ListActivityPayload.toListStatusEntity(
            userId: Long,
            sortIndex: Int,
        ) = StatusEntity.ListStatus(
            id = id,
            userId = userId,
            sortIndex = sortIndex,
            createdAt = createdAt,
            status = status,
            progress = progress,
            siteUrl = siteUrl,
            type = type,
            mediaId = media?.id,
            mediaTitleRomaji = media?.title?.romaji,
            mediaTitleEnglish = media?.title?.english,
            mediaTitleNative = media?.title?.nativeTitle,
            mediaTitleUserPreferred = media?.title?.userPreferred,
            mediaCoverColor = media?.image?.color,
            mediaCoverLarge = media?.image?.large,
            mediaCoverMedium = media?.image?.medium,
            mediaType = media?.type,
            mediaFormat = media?.format,
            mediaStatus = media?.status,
            mediaEpisodes = media?.episodes,
            mediaChapters = media?.chapters,
            mediaVolumes = media?.volumes,
            mediaIsFavourite = media?.isFavourite,
            mediaMeanScore = media?.meanScore,
            mediaAverageScore = media?.averageScore,
            mediaSiteUrl = media?.siteUrl,
            mediaListStatus = mediaListStatus ?: media?.mediaList?.status,
            mediaListProgress = mediaListProgress ?: media?.mediaList?.progress,
            mediaListVolumeProgress = mediaListVolumeProgress ?: media?.mediaList?.progressVolumes,
        )
    }
}


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

package co.anitrend.data.medialist.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.common.extension.toFuzzyDateInt
import co.anitrend.data.common.extension.toFuzzyDateModel
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus

internal class MediaListModelConverter(
    override val fromType: (MediaListModel) -> MediaListEntity = ::transform,
    override val toType: (MediaListEntity) -> MediaListModel = { throw NotImplementedError() }
) : SupportConverter<MediaListModel, MediaListEntity>() {
    private companion object : ISupportTransformer<MediaListModel, MediaListEntity> {
        override fun transform(source: MediaListModel) = when (source) {
            is MediaListModel.Extended -> MediaListEntity(
                mediaType = source.media.type,
                advancedScores = source.advancedScores?.map {
                    MediaListEntity.AdvancedScore(it.key, it.value)
                }.orEmpty(),
                customLists = source.customLists?.map {
                    MediaListEntity.CustomList(
                        name = it.name,
                        enabled = it.enabled
                    )
                }.orEmpty(),
                completedAt = source.completedAt?.toFuzzyDateInt(),
                createdAt = source.createdAt,
                hiddenFromStatus = source.hiddenFromStatusLists ?: false,
                mediaId = source.mediaId,
                notes = source.notes,
                priority = source.priority,
                hidden = source.private ?: false,
                progress = source.progress ?: 0,
                progressVolumes = source.progressVolumes ?: 0,
                repeatCount = source.repeat ?: 0,
                score = source.score ?: 0f,
                startedAt = source.startedAt?.toFuzzyDateInt(),
                status = source.status ?: MediaListStatus.PLANNING,
                updatedAt = source.updatedAt,
                userId = source.userId,
                id = source.id
            )
            is MediaListModel.Core -> MediaListEntity(
                mediaType = source.category.type,
                advancedScores = source.advancedScores?.map {
                    MediaListEntity.AdvancedScore(it.key, it.value)
                }.orEmpty(),
                customLists = source.customLists?.map {
                    MediaListEntity.CustomList(
                        name = it.name,
                        enabled = it.enabled
                    )
                }.orEmpty(),
                completedAt = source.completedAt?.toFuzzyDateInt(),
                createdAt = source.createdAt,
                hiddenFromStatus = source.hiddenFromStatusLists ?: false,
                mediaId = source.mediaId,
                notes = source.notes,
                priority = source.priority,
                hidden = source.private ?: false,
                progress = source.progress ?: 0,
                progressVolumes = source.progressVolumes ?: 0,
                repeatCount = source.repeat ?: 0,
                score = source.score ?: 0f,
                startedAt = source.startedAt?.toFuzzyDateInt(),
                status = source.status ?: MediaListStatus.PLANNING,
                updatedAt = source.updatedAt,
                userId = source.userId,
                id = source.id
            )
        }
    }
}

internal class MediaListEntityConverter(
    override val fromType: (MediaListEntity) -> MediaList = ::transform,
    override val toType: (MediaList) -> MediaListEntity = { throw NotImplementedError() }
) : SupportConverter<MediaListEntity, MediaList>() {
    private companion object : ISupportTransformer<MediaListEntity, MediaList> {
        private fun createMediaListProgress(source: MediaListEntity): MediaListProgress {
            return when (source.mediaType) {
                MediaType.ANIME -> MediaListProgress.Anime(
                    episodeProgress = source.progress,
                    repeatedCount = source.repeatCount
                )
                MediaType.MANGA -> MediaListProgress.Manga(
                    chapterProgress = source.progress,
                    volumeProgress = source.progressVolumes,
                    repeatedCount = source.repeatCount
                )
            }
        }

        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: MediaListEntity) = MediaList.Core(
            advancedScores = source.advancedScores.map {
                MediaList.AdvancedScore(it.name, it.score)
            },
            customLists = source.customLists.map {
                MediaList.CustomList(
                    name = it.name,
                    enabled = it.enabled
                )
            },
            userId = source.userId,
            priority = source.priority,
            createdOn = source.createdAt,
            startedOn = source.startedAt?.toFuzzyDateModel().asFuzzyDate(),
            finishedOn = source.completedAt?.toFuzzyDateModel().asFuzzyDate(),
            mediaId = source.mediaId,
            score = source.score,
            status = source.status,
            progress = createMediaListProgress(source),
            privacy = MediaListPrivacy(
                isPrivate = source.hidden,
                isHidden = source.hiddenFromStatus,
                notes = source.notes,
            ),
            id = source.id
        )
    }
}

internal class MediaListEntityViewConverter(
    override val fromType: (MediaListEntityView) -> MediaList = ::transform,
    override val toType: (MediaList) -> MediaListEntityView = { throw NotImplementedError() }
) : SupportConverter<MediaListEntityView, MediaList>() {
    private companion object : ISupportTransformer<MediaListEntityView, MediaList> {
        private fun createMediaListProgress(source: MediaListEntityView): MediaListProgress {
            val mediaType = when (source) {
                is MediaListEntityView.Core -> source.mediaList.mediaType
                is MediaListEntityView.WithMedia -> source.media.type
            }
            return when (mediaType) {
                MediaType.ANIME -> MediaListProgress.Anime(
                    episodeProgress = source.mediaList.progress,
                    repeatedCount = source.mediaList.repeatCount
                )
                MediaType.MANGA -> MediaListProgress.Manga(
                    chapterProgress = source.mediaList.progress,
                    volumeProgress = source.mediaList.progressVolumes,
                    repeatedCount = source.mediaList.repeatCount
                )
            }
        }

        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: MediaListEntityView) = MediaList.Core(
            advancedScores = source.mediaList.advancedScores.map {
                MediaList.AdvancedScore(it.name, it.score)
            },
            customLists = source.mediaList.customLists.map {
                MediaList.CustomList(
                    name = it.name,
                    enabled = it.enabled
                )
            },
            userId = source.mediaList.userId,
            priority = source.mediaList.priority,
            createdOn = source.mediaList.createdAt,
            startedOn = source.mediaList.startedAt?.toFuzzyDateModel().asFuzzyDate(),
            finishedOn = source.mediaList.completedAt?.toFuzzyDateModel().asFuzzyDate(),
            mediaId = source.mediaList.mediaId,
            score = source.mediaList.score,
            status = source.mediaList.status,
            progress = createMediaListProgress(source),
            privacy = MediaListPrivacy(
                isPrivate = source.mediaList.hidden,
                isHidden = source.mediaList.hiddenFromStatus,
                notes = source.mediaList.notes,
            ),
            id = source.mediaList.id
        )
    }
}
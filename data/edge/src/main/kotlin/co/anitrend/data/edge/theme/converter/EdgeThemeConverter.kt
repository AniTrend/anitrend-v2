/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.theme.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.core.extensions.requireIntegralInt
import co.anitrend.data.edge.core.extensions.requireIntegralLong
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.theme.EdgeThemeEmbedded
import co.anitrend.data.edge.theme.entity.EdgeThemeEntryEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeVideoEntity
import co.anitrend.data.edge.theme.model.name
import co.anitrend.data.edge.theme.model.video

/**
 * Converts a (mediaId, ThemeModel) pair to [EdgeThemeEntity].
 */
internal class EdgeThemeConverter : SupportConverter<EdgeThemeEmbedded, EdgeThemeEntity>() {
    data class PersistableTheme(
        val theme: EdgeThemeEntity,
        val entries: List<EdgeThemeEntryEntity>,
        val videos: List<EdgeThemeVideoEntity>,
    )

    fun convertFromOrNull(source: EdgeThemeEmbedded): PersistableTheme? {
        val (mediaId, model) = source
        val themeId = stableThemeId(model)
        val themeName = model.name.orEmpty()
        val fallbackVideo = model.video.orEmpty()

        if (themeName.isBlank() && fallbackVideo.isBlank()) {
            return null
        }

        val themeEntity =
            EdgeThemeEntity(
                mediaId = mediaId,
                themeId = themeId,
                slug = model.slug,
                type = model.type.name,
                sequence = model.sequence.requireIntegralInt("theme sequence") ?: 0,
                songId = model.song?.id.requireIntegralLong("theme song id"),
                songTitle = themeName,
                id = themeId,
            )

        val entryEntities = mutableListOf<EdgeThemeEntryEntity>()
        val videoEntities = mutableListOf<EdgeThemeVideoEntity>()

        model.animethemeentries
            .orEmpty()
            .filterNotNull()
            .forEach { entry ->
                val entryId = stableEntryId(entry)
                entryEntities +=
                    EdgeThemeEntryEntity(
                        themeId = themeId,
                        entryId = entryId,
                        episodes = entry.episodes,
                        notes = entry.notes,
                        nsfw = entry.nsfw,
                        spoiler = entry.spoiler,
                        version = entry.version.requireIntegralInt("theme entry version") ?: 0,
                        id = entryId,
                    )

                entry.videos
                    .orEmpty()
                    .filterNotNull()
                    .forEach { video ->
                        val link = video.link
                        if (link.isBlank()) {
                            return@forEach
                        }

                        val videoId = stableVideoId(video)
                        videoEntities +=
                            EdgeThemeVideoEntity(
                                entryId = entryId,
                                videoId = videoId,
                                link = link,
                                resolution = video.resolution.requireIntegralInt("theme video resolution"),
                                source = video.source,
                                subbed = video.subbed,
                                lyrics = video.lyrics,
                                nc = video.nc,
                                uncen = video.uncen,
                                tags = video.tags,
                                overlap = video.overlap,
                                audioId = video.audio?.id.requireIntegralLong("theme video audio id"),
                                audioLink = video.audio?.link,
                                id = videoId,
                            )
                    }
            }

        return PersistableTheme(themeEntity, entryEntities, videoEntities)
    }

    override val fromType: (EdgeThemeEmbedded) -> EdgeThemeEntity = { pair ->
        convertFromOrNull(pair)?.theme ?: EdgeThemeEntity(
            mediaId = pair.first,
            themeId = stableThemeId(pair.second),
            slug = pair.second.slug,
            type = pair.second.type.name,
            sequence = pair.second.sequence.requireIntegralInt("theme sequence") ?: 0,
            songId =
                pair.second.song
                    ?.id
                    .requireIntegralLong("theme song id"),
            songTitle = pair.second.name.orEmpty(),
            id = stableThemeId(pair.second),
        )
    }
    override val toType: (EdgeThemeEntity) -> EdgeThemeEmbedded = { throw NotImplementedError() }

    private companion object {
        /**
         * Upstream theme ids are non-null in the generated shape, so the stable
         * identifier is always the upstream id string.
         */
        fun stableThemeId(model: GetMediaByIdData.SeriesAnimethemes): String = model.id.requireIntegralLong("theme id").toString()

        fun stableEntryId(entry: GetMediaByIdData.SeriesAnimethemesAnimethemeentries): String =
            entry.id.requireIntegralLong("theme entry id").toString()

        fun stableVideoId(video: GetMediaByIdData.SeriesAnimethemesAnimethemeentriesVideos): String =
            video.id.requireIntegralLong("theme video id").toString()
    }
}

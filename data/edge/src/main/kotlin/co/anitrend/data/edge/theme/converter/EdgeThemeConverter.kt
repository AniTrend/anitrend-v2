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
import co.anitrend.data.edge.theme.EdgeThemeEmbedded
import co.anitrend.data.edge.theme.entity.EdgeThemeEntryEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeVideoEntity
import co.anitrend.data.edge.theme.model.EdgeThemeModel

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
                type = model.type.orEmpty(),
                sequence = model.sequence ?: 0,
                songId = model.song?.id,
                songTitle = themeName,
                id = themeId,
            )

        val entryEntities = mutableListOf<EdgeThemeEntryEntity>()
        val videoEntities = mutableListOf<EdgeThemeVideoEntity>()

        model.entries.forEachIndexed { entryIndex, entry ->
            val entryId = stableEntryId(themeId = themeId, entry = entry, index = entryIndex)
            entryEntities +=
                EdgeThemeEntryEntity(
                    themeId = themeId,
                    entryId = entryId,
                    episodes = entry.episodes,
                    notes = entry.notes,
                    nsfw = entry.nsfw,
                    spoiler = entry.spoiler,
                    version = entry.version ?: 0,
                    id = entryId,
                )

            entry.videos.forEachIndexed { videoIndex, video ->
                val link = video.link
                if (link.isNullOrBlank()) {
                    return@forEachIndexed
                }

                val videoId = stableVideoId(entryId = entryId, video = video, index = videoIndex)
                videoEntities +=
                    EdgeThemeVideoEntity(
                        entryId = entryId,
                        videoId = videoId,
                        link = link,
                        resolution = video.resolution,
                        source = video.source,
                        subbed = video.subbed,
                        lyrics = video.lyrics,
                        nc = video.nc,
                        uncen = video.uncen,
                        tags = video.tags,
                        overlap = video.overlap,
                        audioId = video.audio?.id,
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
            type = pair.second.type.orEmpty(),
            sequence = pair.second.sequence ?: 0,
            songId = pair.second.song?.id,
            songTitle = pair.second.name.orEmpty(),
            id = stableThemeId(pair.second),
        )
    }
    override val toType: (EdgeThemeEntity) -> EdgeThemeEmbedded = { throw NotImplementedError() }

    private companion object {
        fun stableThemeId(model: EdgeThemeModel): String {
            model.id?.let {
                return it.toString()
            }

            val normalizedName =
                buildString {
                    model.name.orEmpty().forEach { character ->
                        if (character.isLetterOrDigit()) {
                            append(character.lowercaseChar())
                        }
                    }
                }
            val themeMeta = model.meta
            val type =
                themeMeta
                    ?.type
                    .orEmpty()
                    .trim()
                    .lowercase()
            val number = themeMeta?.number ?: 0
            val version = themeMeta?.version ?: 0

            return listOf(normalizedName, type, number.toString(), version.toString()).joinToString(":")
        }

        fun stableEntryId(
            themeId: String,
            entry: EdgeThemeModel.EntryModel,
            index: Int,
        ): String = entry.id?.toString() ?: "$themeId:entry:$index"

        fun stableVideoId(
            entryId: String,
            video: EdgeThemeModel.VideoModel,
            index: Int,
        ): String = video.id?.toString() ?: "$entryId:video:$index"
    }
}

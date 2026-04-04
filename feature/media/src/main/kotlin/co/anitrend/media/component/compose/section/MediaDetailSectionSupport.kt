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
package co.anitrend.media.component.compose.section

import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.tag.entity.Tag

internal enum class MediaTagSpoilerLevel {
    NONE,
    GENERAL,
    MEDIA,
}

internal data class MediaTagPartition(
    val safeTags: List<Tag>,
    val spoilerTags: List<Tag>,
    val mediaSpoilerCount: Int,
    val generalSpoilerCount: Int,
)

internal fun Tag.spoilerLevel(): MediaTagSpoilerLevel =
    when {
        this is Tag.Extended && isMediaSpoiler -> MediaTagSpoilerLevel.MEDIA
        isGeneralSpoiler -> MediaTagSpoilerLevel.GENERAL
        else -> MediaTagSpoilerLevel.NONE
    }

internal fun partitionMediaTags(tags: List<Tag>): MediaTagPartition {
    val safeTags = mutableListOf<Tag>()
    val spoilerTags = mutableListOf<Tag>()
    var mediaSpoilerCount = 0
    var generalSpoilerCount = 0

    tags.forEach { tag ->
        when (tag.spoilerLevel()) {
            MediaTagSpoilerLevel.NONE -> safeTags += tag
            MediaTagSpoilerLevel.GENERAL -> {
                spoilerTags += tag
                generalSpoilerCount += 1
            }

            MediaTagSpoilerLevel.MEDIA -> {
                spoilerTags += tag
                mediaSpoilerCount += 1
            }
        }
    }

    return MediaTagPartition(
        safeTags = safeTags,
        spoilerTags = spoilerTags,
        mediaSpoilerCount = mediaSpoilerCount,
        generalSpoilerCount = generalSpoilerCount,
    )
}

internal fun selectRankingPreview(
    ranks: List<IMediaRank>,
    maxCount: Int = 3,
): List<IMediaRank> {
    if (ranks.isEmpty() || maxCount <= 0) {
        return emptyList()
    }

    val anchorsByPriority =
        listOf(
            MediaRankType.RATED,
            MediaRankType.POPULAR,
        ).mapNotNull { type ->
            ranks.firstOrNull { it.type == type && it.allTime == true }
                ?: ranks.firstOrNull { it.type == type }
        }.distinctBy(IMediaRank::id)

    val remainingRanked =
        ranks
            .sortedWith(
                compareByDescending<IMediaRank> { it.allTime == true }
                    .thenBy { it.rank }
                    .thenByDescending { it.year ?: 0 },
            ).filterNot { rank -> anchorsByPriority.any { it.id == rank.id } }

    return (anchorsByPriority + remainingRanked).take(maxCount)
}

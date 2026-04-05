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

import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.enums.MediaRelation

internal enum class MediaRelationBucket {
    STORY_CONTINUITY,
    SOURCE_AND_ADAPTATION,
    SIDE_PATHS,
    SHARED_UNIVERSE,
}

internal data class MediaRelationGroup(
    val bucket: MediaRelationBucket,
    val entries: List<MediaRelationEntry>,
)

private val relationOrder =
    listOf(
        MediaRelation.SEQUEL,
        MediaRelation.PREQUEL,
        MediaRelation.PARENT,
        MediaRelation.SOURCE,
        MediaRelation.ADAPTATION,
        MediaRelation.SIDE_STORY,
        MediaRelation.SPIN_OFF,
        MediaRelation.ALTERNATIVE,
        MediaRelation.SUMMARY,
        MediaRelation.COMPILATION,
        MediaRelation.CONTAINS,
        MediaRelation.CHARACTER,
        MediaRelation.OTHER,
        null,
    )

internal fun groupRelationsByBucket(relations: List<MediaRelationEntry>): List<MediaRelationGroup> {
    if (relations.isEmpty()) {
        return emptyList()
    }

    val ordered =
        relationOrder
            .flatMap { relation ->
                relations.filter { it.relation == relation }
            }.distinctBy { it.media.id }

    return MediaRelationBucket.entries.mapNotNull { bucket ->
        val entries = ordered.filter { relation -> relation.bucket() == bucket }
        entries.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let {
            MediaRelationGroup(bucket = bucket, entries = it)
        }
    }
}

private fun MediaRelationEntry.bucket(): MediaRelationBucket =
    when (relation) {
        MediaRelation.SEQUEL,
        MediaRelation.PREQUEL,
        MediaRelation.PARENT,
        -> MediaRelationBucket.STORY_CONTINUITY

        MediaRelation.SOURCE,
        MediaRelation.ADAPTATION,
        -> MediaRelationBucket.SOURCE_AND_ADAPTATION

        MediaRelation.SIDE_STORY,
        MediaRelation.SPIN_OFF,
        MediaRelation.ALTERNATIVE,
        MediaRelation.SUMMARY,
        MediaRelation.COMPILATION,
        MediaRelation.CONTAINS,
        -> MediaRelationBucket.SIDE_PATHS

        MediaRelation.CHARACTER,
        MediaRelation.OTHER,
        null,
        -> MediaRelationBucket.SHARED_UNIVERSE
    }

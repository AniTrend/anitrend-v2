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

import androidx.annotation.StringRes
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.media.R
import java.util.Locale

internal enum class MediaStaffRoleGroup(
    @param:StringRes val titleRes: Int,
) {
    ORIGINAL_CREATOR(R.string.label_media_production_original_creator),
    DIRECTOR(R.string.label_media_production_directors),
    WRITER(R.string.label_media_production_writers),
    PRODUCER(R.string.label_media_production_producers),
    OTHER(R.string.label_media_production_additional_credits),
}

internal data class MediaStaffRoleSection(
    val group: MediaStaffRoleGroup,
    val staff: List<MediaPerson.Staff>,
)

private fun String?.normalizedRole(): String =
    this
        .orEmpty()
        .lowercase(Locale.getDefault())
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()

private fun MediaPerson.Staff.roleGroup(): MediaStaffRoleGroup {
    val normalized = role.normalizedRole()

    return when {
        normalized.contains("original creator") ||
            normalized.contains("original story") ||
            normalized.contains("original work") ||
            normalized.contains("creator") ||
            normalized.contains("manga") ||
            normalized.contains("light novel") ||
            normalized.contains("web novel") ||
            normalized.contains("novel") ||
            normalized.contains("comic") -> {
            MediaStaffRoleGroup.ORIGINAL_CREATOR
        }

        normalized.contains("director") -> MediaStaffRoleGroup.DIRECTOR
        normalized.contains("writer") ||
            normalized.contains("screenplay") ||
            normalized.contains("script") ||
            normalized.contains("composition") ||
            normalized.contains("story") -> {
            MediaStaffRoleGroup.WRITER
        }

        normalized.contains("producer") ||
            normalized.contains("animation production") -> {
            MediaStaffRoleGroup.PRODUCER
        }

        else -> MediaStaffRoleGroup.OTHER
    }
}

internal fun groupStaffByRoleBucket(staff: List<MediaPerson.Staff>): List<MediaStaffRoleSection> {
    if (staff.isEmpty()) {
        return emptyList()
    }

    val grouped = linkedMapOf<MediaStaffRoleGroup, MutableList<MediaPerson.Staff>>()

    staff.forEach { item ->
        grouped.getOrPut(item.roleGroup()) { mutableListOf() }.add(item)
    }

    return buildList {
        listOf(
            MediaStaffRoleGroup.ORIGINAL_CREATOR,
            MediaStaffRoleGroup.DIRECTOR,
            MediaStaffRoleGroup.WRITER,
            MediaStaffRoleGroup.PRODUCER,
            MediaStaffRoleGroup.OTHER,
        ).forEach { group ->
            val items = grouped[group].orEmpty()
            if (items.isNotEmpty()) {
                add(
                    MediaStaffRoleSection(
                        group = group,
                        staff = items,
                    ),
                )
            }
        }
    }
}

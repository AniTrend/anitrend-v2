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
package co.anitrend.media.component.compose.people

import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.media.entity.MediaPerson
import kotlin.math.min

private const val PREVIEW_SCAN_MULTIPLIER = 4

private val STAFF_ROLE_PRIORITY_GROUPS =
    listOf(
        listOf(
            "original creator",
            "original story",
            "creator",
            "manga",
            "light novel",
            "web novel",
            "novel",
            "comic",
        ),
        listOf(
            "chief director",
            "series director",
            "director",
        ),
        listOf(
            "series composition",
            "series compositor",
            "screenplay",
            "script",
        ),
        listOf(
            "character design",
            "character designer",
            "chief animation director",
            "animation director",
        ),
        listOf(
            "writer",
            "music",
            "composer",
            "sound director",
            "art director",
            "producer",
            "animation production",
            "studio",
        ),
    )

internal fun List<MediaPerson.Character>.curatedCharacterPreview(maxCount: Int): List<MediaPerson.Character> =
    selectCharacterPreview(previewCandidates(maxCount), maxCount)

internal fun List<MediaPerson.Staff>.curatedStaffPreview(maxCount: Int): List<MediaPerson.Staff> =
    selectStaffPreview(previewCandidates(maxCount), maxCount)

internal fun selectCharacterPreview(
    characters: List<MediaPerson.Character>,
    maxCount: Int,
): List<MediaPerson.Character> {
    if (characters.isEmpty() || maxCount <= 0) {
        return emptyList()
    }

    return characters
        .withIndex()
        .sortedWith(
            compareBy<IndexedValue<MediaPerson.Character>>(
                { it.value.characterRolePriority() },
                { it.index },
            ),
        ).map(IndexedValue<MediaPerson.Character>::value)
        .take(maxCount)
}

internal fun selectStaffPreview(
    staff: List<MediaPerson.Staff>,
    maxCount: Int,
): List<MediaPerson.Staff> {
    if (staff.isEmpty() || maxCount <= 0) {
        return emptyList()
    }

    return staff
        .withIndex()
        .sortedWith(
            compareBy<IndexedValue<MediaPerson.Staff>>(
                { it.value.staffRolePriority() },
                { it.index },
            ),
        ).map(IndexedValue<MediaPerson.Staff>::value)
        .take(maxCount)
}

internal fun <T : Any> List<T>.previewCandidates(
    maxCount: Int,
    scanMultiplier: Int = PREVIEW_SCAN_MULTIPLIER,
): List<T> {
    if (maxCount <= 0 || isEmpty()) {
        return emptyList()
    }

    val scanLimit = min(size, maxCount * scanMultiplier)
    return take(scanLimit)
}

private fun MediaPerson.Character.characterRolePriority(): Int =
    when (role) {
        CharacterRole.MAIN -> 0
        CharacterRole.SUPPORTING -> 1
        CharacterRole.BACKGROUND,
        null,
        -> 2
    }

private fun MediaPerson.Staff.staffRolePriority(): Int {
    val normalizedRole = role.normalizeRoleText()
    if (normalizedRole.isBlank()) {
        return STAFF_ROLE_PRIORITY_GROUPS.size
    }

    val priority =
        STAFF_ROLE_PRIORITY_GROUPS.indexOfFirst { keywords ->
            keywords.any(normalizedRole::contains)
        }

    return if (priority >= 0) priority else STAFF_ROLE_PRIORITY_GROUPS.size
}

private fun String?.normalizeRoleText(): String =
    this
        .orEmpty()
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()

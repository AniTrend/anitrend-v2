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

import androidx.annotation.StringRes
import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.media.R

internal data class MediaCharacterRoleSection(
    @param:StringRes val titleRes: Int,
    val characters: List<MediaPerson.Character>,
)

internal fun List<MediaPerson.Character>.groupedCharacterSections(): List<MediaCharacterRoleSection> {
    if (isEmpty()) {
        return emptyList()
    }

    return buildList {
        listOf(
            CharacterRole.MAIN to R.string.label_media_people_characters_group_main,
            CharacterRole.SUPPORTING to R.string.label_media_people_characters_group_supporting,
            CharacterRole.BACKGROUND to R.string.label_media_people_characters_group_background,
        ).forEach { (role, titleRes) ->
            val items = this@groupedCharacterSections.filter { character -> character.role == role }
            if (items.isNotEmpty()) {
                add(MediaCharacterRoleSection(titleRes = titleRes, characters = items))
            }
        }

        val unclassified = this@groupedCharacterSections.filter { character -> character.role == null }
        if (unclassified.isNotEmpty()) {
            add(
                MediaCharacterRoleSection(
                    titleRes = R.string.label_media_people_characters_group_background,
                    characters = unclassified,
                ),
            )
        }
    }
}

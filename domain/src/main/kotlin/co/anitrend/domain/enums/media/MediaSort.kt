/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.enums.media

import co.anitrend.domain.common.enum.IGraphEnum

/**
 * Media sort enums
 */
enum class MediaSort(override val value: String) : IGraphEnum {
    CHAPTERS("CHAPTERS"),
    DURATION("DURATION"),
    END_DATE("END_DATE"),
    EPISODES("EPISODES"),
    FAVOURITES("FAVOURITES"),
    FORMAT("FORMAT"),
    ID("ID"),
    POPULARITY("POPULARITY"),
    SCORE("SCORE"),
    SEARCH_MATCH("SEARCH_MATCH"),
    START_DATE("START_DATE"),
    STATUS("STATUS"),
    TITLE_ENGLISH("TITLE_ENGLISH"),
    TITLE_NATIVE("TITLE_NATIVE"),
    TITLE_ROMAJI("TITLE_ROMAJI"),
    TRENDING("TRENDING"),
    TYPE("TYPE"),
    UPDATED_AT("UPDATED_AT"),
    VOLUMES("VOLUMES")
}
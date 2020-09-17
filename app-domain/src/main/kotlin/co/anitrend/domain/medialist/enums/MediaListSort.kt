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

package co.anitrend.domain.medialist.enums

import co.anitrend.domain.common.enums.contract.IAliasable
import co.anitrend.domain.common.enums.contract.ISortable

/**
 * Media list sort enums
 */
enum class MediaListSort(override val alias: CharSequence) : IAliasable, ISortable {
    ADDED_TIME("Added time"),
    FINISHED_ON("Finished on"),
    MEDIA_ID("Id"),
    MEDIA_POPULARITY("Popularity"),
    MEDIA_TITLE_ENGLISH("Title english"),
    MEDIA_TITLE_NATIVE("Title native"),
    MEDIA_TITLE_ROMAJI("Title Romaji"),
    PRIORITY("Priority"),
    PROGRESS("Progress"),
    PROGRESS_VOLUMES("Progress volumes"),
    REPEAT("Repeat"),
    SCORE("Score"),
    STARTED_ON("Started on"),
    STATUS("Status"),
    UPDATED_TIME("Updated time")
}
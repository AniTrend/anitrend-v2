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

import co.anitrend.domain.common.enums.contract.ISortable

/**
 * Media list sort enums
 */
enum class MediaListSort : ISortable {
    ADDED_TIME,
    FINISHED_ON,
    MEDIA_ID,
    MEDIA_POPULARITY,
    MEDIA_TITLE_ENGLISH,
    MEDIA_TITLE_NATIVE,
    MEDIA_TITLE_ROMAJI,
    PRIORITY,
    PROGRESS,
    PROGRESS_VOLUMES,
    REPEAT,
    SCORE,
    STARTED_ON,
    STATUS,
    UPDATED_TIME
}
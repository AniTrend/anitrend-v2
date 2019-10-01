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

/**
 * Media list sort enums
 */
enum class MediaListSort(override val value: String) : IGraphEnum {
    ADDED_TIME("ADDED_TIME"),
    FINISHED_ON("FINISHED_ON"),
    MEDIA_ID("MEDIA_ID"),
    MEDIA_POPULARITY("MEDIA_POPULARITY"),
    MEDIA_TITLE_ENGLISH("MEDIA_TITLE_ENGLISH"),
    MEDIA_TITLE_NATIVE("MEDIA_TITLE_NATIVE"),
    MEDIA_TITLE_ROMAJI("MEDIA_TITLE_ROMAJI"),
    PRIORITY("PRIORITY"),
    PROGRESS("PROGRESS"),
    PROGRESS_VOLUMES("PROGRESS_VOLUMES"),
    REPEAT("REPEAT"),
    SCORE("SCORE"),
    STARTED_ON("STARTED_ON"),
    STATUS("STATUS"),
    UPDATED_TIME("UPDATED_TIME")
}
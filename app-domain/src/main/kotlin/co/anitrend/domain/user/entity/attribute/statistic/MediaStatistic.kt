/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.user.entity.attribute.statistic

import co.anitrend.domain.common.CountryCode
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.staff.entity.Staff as StaffEntity
import co.anitrend.domain.studio.entity.Studio as StudioEntity
import co.anitrend.domain.tag.entity.Tag as TagEntity

/**
 * Contract for user statistics, applied on various items such as countries, formats, genres, years, staff e.t.c
 *
 * @property count Count for the statistic
 * @property meanScore Mean score for the the statistic
 * @property mediaIds List of media ids for the statistic
 */
sealed class MediaStatistic {
    abstract val count: Int
    abstract val meanScore: Float
    abstract val mediaIds: List<Long>

    /**
     * @property minutesWatched Minutes for watched for teh statistic
     */
    sealed class Anime : MediaStatistic() {
        abstract val minutesWatched: Int

        data class Country(
            val country: CountryCode,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Format(
            val format: MediaFormat,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Genre(
            val genre: String,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Length(
            val length: String,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class ReleaseYear(
            val releaseYear: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Score(
            val score: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Staff(
            val staff: StaffEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class StartYear(
            val startYear: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Status(
            val status: MediaListStatus,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Studio(
            val studio: StudioEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class Tag(
            val tag: TagEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()

        data class VoiceActor(
            val voiceActor: StaffEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val minutesWatched: Int
        ) : Anime()
    }

    /**
     * @property chaptersRead Chapters read for the statistic
     */
    sealed class Manga : MediaStatistic() {
        abstract val chaptersRead: Int

        data class Country(
            val country: CountryCode,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Format(
            val format: MediaFormat,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Genre(
            val genre: String,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Length(
            val length: String,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class ReleaseYear(
            val releaseYear: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Score(
            val score: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Staff(
            val staff: StaffEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class StartYear(
            val startYear: Int,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Status(
            val status: MediaListStatus,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Studio(
            val studio: StudioEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class Tag(
            val tag: TagEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()

        data class VoiceActor(
            val voiceActor: StaffEntity?,
            override val count: Int,
            override val meanScore: Float,
            override val mediaIds: List<Long>,
            override val chaptersRead: Int
        ) : Manga()
    }
}
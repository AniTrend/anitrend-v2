/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.user.model.remote.statistics.media

import co.anitrend.data.arch.CountryCode
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.staff.model.remote.StaffModel
import co.anitrend.data.studio.model.remote.StudioModel
import co.anitrend.data.tag.model.remote.TagModel
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * @property chaptersRead Chapters read for the statistic
 */
@Serializable
internal sealed class UserStatisticMangaModel : IStatisticModel {
    abstract val chaptersRead: Int

    @Serializable
    data class Country(
        @SerialName("country") val country: CountryCode?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Format(
        @SerialName("format") val format: MediaFormat?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Genre(
        @SerialName("genre") val genre: String?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Length(
        @SerialName("length") val length: String?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class ReleaseYear(
        @SerialName("releaseYear") val releaseYear: Int?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Score(
        @SerialName("score") val score: Int?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Staff(
        @SerialName("staff") val staff: StaffModel.Core?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class StartYear(
        @SerialName("startYear") val startYear: Int?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Status(
        @SerialName("status") val status: MediaListStatus,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Studio(
        @SerialName("studio") val studio: StudioModel.Core?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class Tag(
        @SerialName("tag") val tag: TagModel?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()

    @Serializable
    data class VoiceActor(
        @SerialName("voiceActor") val voiceActor: StaffModel.Core?,
        @SerialName("count") override val count: Int,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("mediaIds") override val mediaIds: List<Long>,
        @SerialName("chaptersRead") override val chaptersRead: Int
    ) : UserStatisticMangaModel()
}
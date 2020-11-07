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

package co.anitrend.data.user.model.remote.statistics

import co.anitrend.data.user.model.remote.statistics.contract.IUserStatisticModel
import co.anitrend.data.user.model.remote.statistics.media.UserStatisticAnimeModel
import co.anitrend.data.user.model.remote.statistics.media.UserStatisticMangaModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @property count TBA
 * @property meanScore TBA
 * @property standardDeviation TBA
 */
@Serializable
internal sealed class UserStatisticModel : IUserStatisticModel {

    abstract val count: Int
    abstract val meanScore: Float
    abstract val standardDeviation: Float

    @Serializable
    data class Anime(
        @SerialName("minutesWatched") val minutesWatched: Int,
        @SerialName("episodesWatched") val episodesWatched: Int,
        @SerialName("count") override val count: Int,
        @SerialName("countries") override val countries: List<UserStatisticAnimeModel>?,
        @SerialName("formats") override val formats: List<UserStatisticAnimeModel>?,
        @SerialName("genres") override val genres: List<UserStatisticAnimeModel>?,
        @SerialName("lengths") override val lengths: List<UserStatisticAnimeModel>?,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("releaseYears") override val releaseYears: List<UserStatisticAnimeModel>?,
        @SerialName("scores") override val scores: List<UserStatisticAnimeModel>?,
        @SerialName("staff") override val staff: List<UserStatisticAnimeModel>?,
        @SerialName("standardDeviation") override val standardDeviation: Float,
        @SerialName("startYears") override val startYears: List<UserStatisticAnimeModel>?,
        @SerialName("statuses") override val statuses: List<UserStatisticAnimeModel>?,
        @SerialName("studios") override val studios: List<UserStatisticAnimeModel>?,
        @SerialName("tags") override val tags: List<UserStatisticAnimeModel>?,
        @SerialName("voiceActors") override val voiceActors: List<UserStatisticAnimeModel>?
    ) : UserStatisticModel()

    @Serializable
    data class Manga(
        @SerialName("chaptersRead") val chaptersRead: Int,
        @SerialName("volumesRead") val volumesRead: Int,
        @SerialName("count") override val count: Int,
        @SerialName("countries") override val countries: List<UserStatisticMangaModel>?,
        @SerialName("formats") override val formats: List<UserStatisticMangaModel>?,
        @SerialName("genres") override val genres: List<UserStatisticMangaModel>?,
        @SerialName("lengths") override val lengths: List<UserStatisticMangaModel>?,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("releaseYears") override val releaseYears: List<UserStatisticMangaModel>?,
        @SerialName("scores") override val scores: List<UserStatisticMangaModel>?,
        @SerialName("staff") override val staff: List<UserStatisticMangaModel>?,
        @SerialName("standardDeviation") override val standardDeviation: Float,
        @SerialName("startYears") override val startYears: List<UserStatisticMangaModel>?,
        @SerialName("statuses") override val statuses: List<UserStatisticMangaModel>?,
        @SerialName("studios") override val studios: List<UserStatisticMangaModel>?,
        @SerialName("tags") override val tags: List<UserStatisticMangaModel>?,
        @SerialName("voiceActors") override val voiceActors: List<UserStatisticMangaModel>?
    ) : UserStatisticModel()
}
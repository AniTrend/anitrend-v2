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

package co.anitrend.data.user.model.statistics

import co.anitrend.data.user.model.statistics.contract.IUserStatisticModel
import co.anitrend.data.user.model.statistics.media.UserStatisticAnimeModel
import co.anitrend.data.user.model.statistics.media.UserStatisticMangaModel
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
        @SerialName("countries") override val countries: List<UserStatisticAnimeModel.Country>?,
        @SerialName("formats") override val formats: List<UserStatisticAnimeModel.Format>?,
        @SerialName("genres") override val genres: List<UserStatisticAnimeModel.Genre>?,
        @SerialName("lengths") override val lengths: List<UserStatisticAnimeModel.Length>?,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("releaseYears") override val releaseYears: List<UserStatisticAnimeModel.ReleaseYear>?,
        @SerialName("scores") override val scores: List<UserStatisticAnimeModel.Score>?,
        @SerialName("staff") override val staff: List<UserStatisticAnimeModel.Staff>?,
        @SerialName("standardDeviation") override val standardDeviation: Float,
        @SerialName("startYears") override val startYears: List<UserStatisticAnimeModel.StartYear>?,
        @SerialName("statuses") override val statuses: List<UserStatisticAnimeModel.Status>?,
        @SerialName("studios") override val studios: List<UserStatisticAnimeModel.Studio>?,
        @SerialName("tags") override val tags: List<UserStatisticAnimeModel.Tag>?,
        @SerialName("voiceActors") override val voiceActors: List<UserStatisticAnimeModel.VoiceActor>?
    ) : UserStatisticModel()

    @Serializable
    data class Manga(
        @SerialName("chaptersRead") val chaptersRead: Int,
        @SerialName("volumesRead") val volumesRead: Int,
        @SerialName("count") override val count: Int,
        @SerialName("countries") override val countries: List<UserStatisticMangaModel.Country>?,
        @SerialName("formats") override val formats: List<UserStatisticMangaModel.Format>?,
        @SerialName("genres") override val genres: List<UserStatisticMangaModel.Genre>?,
        @SerialName("lengths") override val lengths: List<UserStatisticMangaModel.Length>?,
        @SerialName("meanScore") override val meanScore: Float,
        @SerialName("releaseYears") override val releaseYears: List<UserStatisticMangaModel.ReleaseYear>?,
        @SerialName("scores") override val scores: List<UserStatisticMangaModel.Score>?,
        @SerialName("staff") override val staff: List<UserStatisticMangaModel.Staff>?,
        @SerialName("standardDeviation") override val standardDeviation: Float,
        @SerialName("startYears") override val startYears: List<UserStatisticMangaModel.StartYear>?,
        @SerialName("statuses") override val statuses: List<UserStatisticMangaModel.Status>?,
        @SerialName("studios") override val studios: List<UserStatisticMangaModel.Studio>?,
        @SerialName("tags") override val tags: List<UserStatisticMangaModel.Tag>?,
        @SerialName("voiceActors") override val voiceActors: List<UserStatisticMangaModel.VoiceActor>?
    ) : UserStatisticModel()
}
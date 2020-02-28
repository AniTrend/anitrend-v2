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

package co.anitrend.data.medialist.model.remote

import android.os.Parcelable
import co.anitrend.domain.medialist.enums.ScoreFormat
import kotlinx.android.parcel.Parcelize

/** [MediaListOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistoptions.doc.html)
 * A user's list options
 *
 * @param scoreFormat The score format the user is using for media lists
 * @param rowOrder The default order list rows should be displayed in
 * @param animeList The user's anime list options
 * @param mangaList The user's manga list options
 */
@Parcelize
data class MediaListOptions(
    val scoreFormat: ScoreFormat,
    val rowOrder: String,
    val animeList: MediaListTypeOptions?,
    val mangaList: MediaListTypeOptions?
): Parcelable
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

package co.anitrend.data.studio.model.remote

import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.studio.model.contract.IStudioModel
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Studio](https://anilist.github.io/ApiV2-GraphQL-Docs/studio.doc.html)
 * Animation or production company
 *
 * @param favourites The amount of user's who have favourite the studio
 */
@Serializable
internal data class StudioModel(
    val media: MediaConnection?,
    @SerialName("favourites") override val favourites: Int?,
    @SerialName("isAnimationStudio") override val isAnimationStudio: Boolean,
    @SerialName("isFavourite") override val isFavourite: Boolean,
    @SerialName("name") override val name: String,
    @SerialName("siteUrl") override val siteUrl: String?,
    @SerialName("id") override val id: Long
) : IStudioModel
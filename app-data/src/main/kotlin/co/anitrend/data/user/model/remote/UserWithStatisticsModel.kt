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

package co.anitrend.data.user.model.remote

import co.anitrend.data.medialist.model.remote.option.MediaListOptions
import co.anitrend.data.shared.model.SharedImage
import co.anitrend.data.user.model.contract.IUserModelExtended
import co.anitrend.data.user.model.remote.option.UserOptionsModel
import com.google.gson.annotations.SerializedName

internal data class UserWithStatisticsModel(
    @SerializedName("statistics") val statistics: UserStatisticTypes?,
    @SerializedName("about") override val about: String?,
    @SerializedName("options") override val options: UserOptionsModel?,
    @SerializedName("unreadNotificationCount") override val unreadNotificationCount: Int?,
    @SerializedName("donatorTier") override val donatorTier: Int?,
    @SerializedName("donatorBadge") override val donatorBadge: String?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("updatedAt") override val updatedAt: Long?,
    @SerializedName("mediaListOptions") override val mediaListOptions: MediaListOptions?,
    @SerializedName("name") override val name: String,
    @SerializedName("avatar") override val avatar: SharedImage?,
    @SerializedName("bannerImage") override val bannerImage: String?,
    @SerializedName("isFollowing") override val isFollowing: Boolean?,
    @SerializedName("isFollower") override val isFollower: Boolean?,
    @SerializedName("id") override val id: Long
) : IUserModelExtended
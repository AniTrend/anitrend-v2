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

package co.anitrend.data.auth.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Json Web Token from Implicit Grant see implicit grant manual docs at:
 * https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth/implicit-grant
 */
@Entity
@Parcelize
data class JsonWebToken(
    @PrimaryKey
    val id: Long,
    val accessToken: String
): Parcelable {
    fun getTokenKey() = "Bearer $accessToken"
}
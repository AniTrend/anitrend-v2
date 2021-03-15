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

package co.anitrend.media.discover.component.sheet.action

import co.anitrend.navigation.model.common.IParam
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

internal sealed class MediaFilterAction : IParam {

    @Parcelize
    data class Tag(
        var tagCategory_in: List<String>? = null,
        var tagCategory_not_in: List<String>? = null,
        var tag_in: List<String>? = null,
        var tag_not_in: List<String>? = null
    ) : MediaFilterAction() {

        @IgnoredOnParcel
        override val idKey = KEY

        companion object : IParam.IKey {
            override val KEY = "Tag"
        }
    }

    @Parcelize
    data class Genre(
        var genre_in: List<String>? = null,
        var genre_not_in: List<String>? = null,
    ) : MediaFilterAction() {

        @IgnoredOnParcel
        override val idKey = KEY

        companion object : IParam.IKey {
            override val KEY = "Genre"
        }
    }
}
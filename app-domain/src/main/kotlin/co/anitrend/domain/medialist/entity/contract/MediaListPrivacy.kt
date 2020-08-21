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

package co.anitrend.domain.medialist.entity.contract

/**
 * Privacy of a list information
 *
 * @param isPrivate If the entry should only be visible to the current authenticated user
 * @param isHidden If the entry shown be hidden from non-custom lists
 * @param notes Users notes
 */
data class MediaListPrivacy(
    val isPrivate: Boolean = false,
    val isHidden: Boolean = false,
    val notes: CharSequence?
)
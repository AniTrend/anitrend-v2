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

package co.anitrend.domain.news.entity

import co.anitrend.domain.common.entity.contract.IEntity

data class News(
    override val id: Long,
    val guid: String,
    val link: String,
    val title: String,
    val image: String?,
    val author: String,
    val subTitle: String,
    val description: String?,
    val content: String,
    val publishedOn: Long?
) : IEntity
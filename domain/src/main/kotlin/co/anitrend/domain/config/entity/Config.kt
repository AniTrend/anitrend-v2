/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.domain.config.entity

data class Config(
    val settings: Settings,
    val image: DefaultImage,
    val navigation: List<Navigation>,
    val genres: List<Genre>,
) {
    data class Settings(
        val analyticsEnabled: Boolean,
        val platformSource: String?,
    )

    data class DefaultImage(
        val banner: String,
        val poster: String,
        val loading: String,
        val error: String,
        val info: String,
        val default: String,
    )

    data class Genre(
        val mediaId: Long,
        val name: String,
    )

    data class Navigation(
        val criteria: String,
        val destination: String,
        val group: Group,
        val i18n: String,
        val icon: String,
        val id: Long,
    ) {
        data class Group(
            val authenticated: Boolean,
            val i18n: String,
        )
    }
}

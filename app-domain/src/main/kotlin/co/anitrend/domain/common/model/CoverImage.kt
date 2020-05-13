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

package co.anitrend.domain.common.model

import co.anitrend.domain.common.entity.IEntityImage

/**
 * Shared model between [co.anitrend.domain.character.entities.Character],
 * [co.anitrend.domain.staff.entities.Staff] &
 * [co.anitrend.domain.user.entities.User]
 *
 * @param large The cover image at its largest size
 * @param medium The cover image at medium size
 */
data class CoverImage(
    override val large: String?,
    override val medium: String?
) : IEntityImage
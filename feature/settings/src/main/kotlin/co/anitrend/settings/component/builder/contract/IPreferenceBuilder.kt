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
package co.anitrend.settings.component.builder.contract

import co.anitrend.settings.model.SettingItem

interface IPreferenceBuilder {
    fun add(
        category: SettingItem.CategoryHeader? = null,
        entries: List<SettingItem> = emptyList(),
        isVisible: Boolean = true,
    ): IPreferenceBuilder

    fun build(): List<SettingItem>

    fun size(): Int

    fun clear()
}

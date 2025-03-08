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
package co.anitrend.settings.component.builder

import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

internal class PreferenceBuilder : IPreferenceBuilder {
    private val collection = mutableListOf<SettingCollectionItem>()

    override fun add(
        category: SettingItem.CategoryHeader?,
        entries: List<SettingItem>,
        isVisible: Boolean,
    ): IPreferenceBuilder {
        if (category != null) {
            collection.add(SettingCollectionItem(category, isVisible))
        }
        entries.forEach {
            collection.add(SettingCollectionItem(it, isVisible))
        }
        return this
    }

    override fun build(): List<SettingItem> =
        collection
            .filter(SettingCollectionItem::isVisible)
            .map(SettingCollectionItem::item)
            .toList()

    override fun size(): Int = collection.size

    override fun clear() = collection.clear()

    data class SettingCollectionItem(
        val item: SettingItem,
        val isVisible: Boolean,
    )
}

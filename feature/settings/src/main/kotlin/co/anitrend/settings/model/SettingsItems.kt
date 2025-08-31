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
package co.anitrend.settings.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed class SettingItem(
    open val id: String,
) {
    data class CategoryHeader(
        override val id: String,
        val title: String,
    ) : SettingItem(id)

    data class HintCard(
        override val id: String,
        val title: String,
        val description: String?,
        val icon: ImageVector,
        val onClick: () -> Unit,
    ) : SettingItem(id)

    data class SwitchSetting(
        override val id: String,
        val title: String,
        val summary: String,
        val icon: ImageVector,
        val onValueChange: (Boolean) -> Unit,
        val onClick: () -> Boolean,
    ) : SettingItem(id)

    data class ClickableSetting(
        override val id: String,
        val title: String,
        val summary: String,
        val icon: ImageVector,
        val onClick: () -> Unit,
    ) : SettingItem(id)

    data class DialogSetting<T>(
        override val id: String,
        val title: String,
        val summary: String,
        val icon: ImageVector,
        val options: List<T>,
        val selectedOption: () -> T,
        val onOptionSelected: (T) -> Unit,
        val displayText: (T) -> String,
        val displayDescription: ((T) -> String)? = null,
    ) : SettingItem(id)

    data class SliderSetting(
        override val id: String,
        val value: () -> Float,
        val onValueChange: (Float) -> Unit,
        val valueRange: ClosedFloatingPointRange<Float>,
        val steps: Int = 0,
        val valueLabel: (Float) -> String = { it.toString() },
        val extraInfo: (() -> String)? = null,
        val progress: (() -> Float)? = null,
    ) : SettingItem(id)
}

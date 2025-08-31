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
package co.anitrend.settings.component.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.category.AniTrendCategoryHeader
import co.anitrend.android.core.compose.design.category.AniTrendCategoryItem
import co.anitrend.android.core.compose.design.slider.AniTrendSliderItem
import co.anitrend.android.core.compose.design.toggle.AniTrendSwitch
import co.anitrend.settings.model.SettingItem

@Composable
fun SettingsItemsList(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = settingsItems.size,
            key = { settingsItems[it].id },
        ) { index ->
            RenderSettingItem(item = settingsItems[index])
        }
    }
}

@Composable
private fun RenderSettingItem(item: SettingItem) {
    when (item) {
        is SettingItem.CategoryHeader ->
            AniTrendCategoryHeader(text = item.title)

        is SettingItem.HintCard ->
            AniTrendHintCard(
                title = item.title,
                description = item.description,
                icon = item.icon,
                onClick = item.onClick,
            )

        is SettingItem.SwitchSetting ->
            AniTrendSwitch(
                title = item.title,
                description = item.summary,
                icon = item.icon,
                enabled = true,
                isChecked = item.onClick(),
                onClick = { item.onValueChange(!item.onClick()) },
            )

        is SettingItem.ClickableSetting ->
            AniTrendCategoryItem(
                title = item.title,
                description = item.summary,
                icon = item.icon,
                onClick = item.onClick,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                    )
                },
            )

        is SettingItem.DialogSetting<*> ->
            PreferenceDialog(item = item)

        is SettingItem.SliderSetting ->
            AniTrendSliderItem(
                value = item.value,
                onValueChange = item.onValueChange,
                valueRange = item.valueRange,
                steps = item.steps,
                valueLabel = item.valueLabel,
                extraInfo = item.extraInfo,
                progress = item.progress,
            )
    }
}

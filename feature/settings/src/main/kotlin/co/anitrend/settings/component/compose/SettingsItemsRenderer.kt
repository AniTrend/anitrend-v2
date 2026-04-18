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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.slider.AniTrendSliderItem
import co.anitrend.settings.model.SettingItem

@Composable
fun SettingsItemsList(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    val blocks = rememberSettingsBlocks(settingsItems)
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = blocks.size,
            key = { blocks[it].id },
        ) { index ->
            when (val block = blocks[index]) {
                is SettingsBlock.SingleItem -> RenderStandaloneItem(item = block.item)
                is SettingsBlock.Section -> {
                    SettingsSectionCard(title = block.header.title) {
                        block.items.forEachIndexed { itemIndex, item ->
                            RenderSectionItem(item = item)
                            if (itemIndex != block.items.lastIndex) {
                                androidx.compose.material3.HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    color =
                                        androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant
                                            .copy(alpha = 0.35f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderStandaloneItem(item: SettingItem) {
    when (item) {
        is SettingItem.HintCard ->
            AniTrendHintCard(
                title = item.title,
                description = item.description,
                icon = item.icon,
                currentValue = item.currentValue,
                actionLabel = item.actionLabel,
                onClick = item.onClick,
            )

        is SettingItem.CategoryHeader -> Unit
        else ->
            SettingsSectionCard {
                RenderSectionItem(item = item)
            }
    }
}

@Composable
private fun RenderSectionItem(item: SettingItem) {
    when (item) {
        is SettingItem.SwitchSetting ->
            SettingsToggleRow(
                title = item.title,
                summary = item.summary,
                icon = item.icon,
                enabled = item.enabled(),
                checked = item.onClick(),
                onCheckedChange = item.onValueChange,
            )

        is SettingItem.ClickableSetting ->
            SettingsValueRow(
                title = item.title,
                summary = item.summary,
                icon = item.icon,
                currentValue = item.currentValue?.invoke(),
                enabled = item.enabled,
                onClick = item.onClick,
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

        is SettingItem.CategoryHeader,
        is SettingItem.HintCard,
        -> Unit
    }
}

private sealed class SettingsBlock(
    open val id: String,
) {
    data class SingleItem(
        val item: SettingItem,
    ) : SettingsBlock(item.id)

    data class Section(
        val header: SettingItem.CategoryHeader,
        val items: List<SettingItem>,
    ) : SettingsBlock(header.id)
}

@Composable
private fun rememberSettingsBlocks(settingsItems: List<SettingItem>): List<SettingsBlock> =
    androidx.compose.runtime.remember(settingsItems) {
        buildList {
            var currentHeader: SettingItem.CategoryHeader? = null
            val currentItems = mutableListOf<SettingItem>()

            fun flushSection() {
                val header = currentHeader
                if (header != null && currentItems.isNotEmpty()) {
                    add(SettingsBlock.Section(header = header, items = currentItems.toList()))
                }
                currentHeader = null
                currentItems.clear()
            }

            settingsItems.forEach { item ->
                when (item) {
                    is SettingItem.CategoryHeader -> {
                        flushSection()
                        currentHeader = item
                    }

                    is SettingItem.HintCard -> {
                        flushSection()
                        add(SettingsBlock.SingleItem(item))
                    }

                    else -> {
                        if (currentHeader == null) {
                            add(SettingsBlock.SingleItem(item))
                        } else {
                            currentItems += item
                        }
                    }
                }
            }

            flushSection()
        }
    }

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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.anitrend.android.core.compose.design.category.AniTrendCategoryItem
import co.anitrend.android.core.compose.design.choice.AniTrendSingleChoiceItem
import co.anitrend.settings.model.SettingItem
import co.anitrend.settings.R

@Composable
fun <T> PreferenceDialog(item: SettingItem.DialogSetting<T>) {
    var showDialog by remember { mutableStateOf(false) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(item.selectedOption()) }

    AniTrendCategoryItem(
        title = item.title,
        description = item.summary,
        icon = item.icon,
        enabled = !showDialog,
        onClick = { showDialog = true },
        trailingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
            )
        },
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = item.title)
            },
            icon = {
                Icon(imageVector = item.icon, contentDescription = null)
            },
            text = {
                Column(modifier = Modifier.selectableGroup()) {
                    item.options.forEach { option ->
                        AniTrendSingleChoiceItem(
                            text = item.displayText(option),
                            selected = selectedOption == option,
                            onOptionSelected = {
                                onOptionSelected(option)
                                item.onOptionSelected(option)
                            },
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.action_settings_close))
                }
            },
        )
    }
}

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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.settings.model.SettingItem

@Composable
fun PreferenceCategory(
    item: SettingItem.CategoryHeader,
    modifier: Modifier = Modifier,
) {
    Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun PreferenceSwitch(
    item: SettingItem.SwitchSetting,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable { item.onValueChange(!item.checked()) },
        headlineContent = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            if (item.summary.isNotEmpty()) {
                Text(
                    text = item.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
            )
        },
        trailingContent = {
            Switch(
                checked = item.checked(),
                onCheckedChange = item.onValueChange,
            )
        },
    )
}

@Composable
fun PreferenceItem(
    item: SettingItem.ClickableSetting,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable { item.onClick() },
        headlineContent = {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            if (item.summary.isNotEmpty()) {
                Text(
                    text = item.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
            )
        },
    )
}

@Composable
fun <T> PreferenceDialog(
    item: SettingItem.DialogSetting<T>,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }

    // The preference row. When tapped, the dialog is triggered.
    ListItem(
        modifier = modifier.clickable { showDialog = true },
        headlineContent = {
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
        },
        supportingContent = {
            Column {
                if (item.summary.isNotEmpty()) {
                    Text(
                        text = item.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = item.displayText(item.selectedOption()),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
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
            text = {
                // A column that lists all the options with radio buttons.
                Column {
                    item.options.forEach { option ->
                        val selected = item.selectedOption() == option
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        item.onOptionSelected(option)
                                        showDialog = false
                                    }.padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = selected,
                                onClick = {
                                    item.onOptionSelected(option)
                                    showDialog = false
                                },
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item.displayText(option), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "OK")
                }
            },
        )
    }
}

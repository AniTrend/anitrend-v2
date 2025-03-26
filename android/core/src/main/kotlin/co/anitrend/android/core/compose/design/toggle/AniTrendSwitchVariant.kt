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
package co.anitrend.android.core.compose.design.toggle

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.applyOpacity
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@Composable
fun AniTrendSwitchVariant(
    title: String,
    description: String? = null,
    icon: ImageVector? = Icons.Outlined.ToggleOn,
    enabled: Boolean = true,
    isChecked: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier =
            Modifier
                .toggleable(
                    value = isChecked,
                    enabled = enabled,
                    onValueChange = { onClick() },
                    indication = LocalIndication.current,
                    interactionSource = interactionSource,
                ).fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground.applyOpacity(enabled),
                    style = MaterialTheme.typography.titleLarge,
                )
                if (!description.isNullOrEmpty()) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            Switch(
                modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterVertically),
                checked = isChecked,
                onCheckedChange = null,
                interactionSource = interactionSource,
                enabled = enabled,
                thumbContent = thumbContent,
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun AniTrendSwitchVariantPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    var toggle by remember { mutableStateOf(true) }
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSwitchVariant(
            title = "AniTrendSwitchVariant",
            description = "Variant description",
            isChecked = toggle,
            onClick = { toggle = !toggle },
        )
    }
}

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
package co.anitrend.core.android.compose.design.toggle

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

/**
 * Renamed from PreferenceSwitchWithContainer -> AniTrendSwitchWithContainer
 */
@Composable
fun AniTrendSwitchWithContainer(
    title: String,
    icon: ImageVector? = null,
    isChecked: Boolean,
    thumbContent: @Composable (() -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .toggleable(
                    value = isChecked,
                    onValueChange = { onClick() },
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ).padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 16.dp).size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = if (icon == null) 12.dp else 0.dp, end = 12.dp),
        ) {
            Text(
                text = title,
                maxLines = 2,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.weight(.1f))
        Switch(
            modifier = Modifier.padding(end = 8.dp),
            checked = isChecked,
            onCheckedChange = null,
            interactionSource = interactionSource,
            thumbContent = thumbContent,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun AniTrendSwitchWithContainerPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    var isChecked by remember { mutableStateOf(false) }
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSwitchWithContainer(
            title = "Title in Container",
            isChecked = isChecked,
            onClick = { isChecked = !isChecked },
        )
    }
}

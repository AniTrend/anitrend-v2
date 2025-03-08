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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.design.category.AniTrendCategoryItemDescription
import co.anitrend.core.android.compose.design.category.AniTrendCategoryItemTitle
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.applyOpacity
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@Composable
fun AniTrendSwitchWithDivider(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isSwitchEnabled: Boolean = enabled,
    isChecked: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = rememberThumbContent(isChecked = isChecked),
    onClick: () -> Unit = {},
    onChecked: () -> Unit = {},
) {
    Surface(
        modifier =
            Modifier.clickable(
                enabled = enabled,
                onClick = onClick,
                onClickLabel = "Open Preferences",
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 12.dp)
                    .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp).size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                AniTrendCategoryItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty()) {
                    AniTrendCategoryItemDescription(text = description, enabled = enabled)
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            VerticalDivider(
                modifier =
                    Modifier
                        .height(32.dp)
                        .padding(horizontal = 8.dp)
                        .width(1f.dp)
                        .align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
            Switch(
                checked = isChecked,
                onCheckedChange = { onChecked() },
                modifier =
                    Modifier.padding(start = 6.dp, end = 8.dp).semantics { contentDescription = title },
                enabled = isSwitchEnabled,
                thumbContent = thumbContent,
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
fun AniTrendSwitchWithDividerPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    var isChecked by remember { mutableStateOf(false) }
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSwitchWithDivider(
            title = "AniTrendSwitchWithDivider",
            description = "Divider-based switch layout",
            isChecked = isChecked,
            onChecked = { isChecked = !isChecked },
        )
    }
}

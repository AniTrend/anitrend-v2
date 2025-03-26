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
package co.anitrend.android.core.compose.design.choice

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.applyOpacity
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@Composable
fun AniTrendSingleChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onOptionSelected: () -> Unit,
) {
    Row(
        modifier =
            modifier.then(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected,
                        onClick = { onOptionSelected() },
                        role = Role.RadioButton,
                    ).padding(16.dp),
            ),
    ) {
        Text(
            text = text,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface.applyOpacity(true),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.weight(.1f))
        RadioButton(
            selected = selected,
            onClick = null,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun AniTrendSingleChoiceItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSingleChoiceItem(
            text = "Single Choice Item",
            selected = true,
            onOptionSelected = {},
        )
    }
}

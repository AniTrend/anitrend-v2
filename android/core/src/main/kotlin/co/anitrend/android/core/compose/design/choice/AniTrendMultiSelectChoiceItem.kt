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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@Composable
fun AniTrendMultiSelectChoiceItem(
    label: String,
    summary: String? = null,
    selected: Boolean = false,
    isMultiSelectEnabled: Boolean = false,
    checked: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Surface(
        modifier =
            Modifier.run {
                if (!isMultiSelectEnabled) {
                    then(
                        this.combinedClickable(
                            onClick = onClick,
                            onClickLabel = "Edit",
                            onLongClick = onLongClick,
                            onLongClickLabel = "Multiselect mode",
                        ),
                    )
                } else {
                    then(this.toggleable(value = checked, onValueChange = onCheckedChange))
                }
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isMultiSelectEnabled) {
                Checkbox(
                    modifier = Modifier.clearAndSetSemantics {},
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                )
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                Text(
                    text = label,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                summary?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            AnimatedVisibility(!isMultiSelectEnabled) {
                Row {
                    VerticalDivider(
                        modifier =
                            Modifier
                                .height(32.dp)
                                .padding(horizontal = 12.dp)
                                .align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp,
                    )
                    RadioButton(
                        modifier = Modifier.semantics { contentDescription = label },
                        selected = selected,
                        onClick = onSelect,
                    )
                }
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun TemplateItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme) {
        AniTrendMultiSelectChoiceItem(
            label = "Label",
            summary = "summary about item",
        )
    }
}

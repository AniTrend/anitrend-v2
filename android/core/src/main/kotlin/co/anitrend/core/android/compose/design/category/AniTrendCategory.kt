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
package co.anitrend.core.android.compose.design.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.applyOpacity
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AniTrendCategoryItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier =
            Modifier.combinedClickable(
                onClick = onClick,
                onClickLabel = onClickLabel,
                enabled = enabled,
                onLongClickLabel = onLongClickLabel,
                onLongClick = onLongClick,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()

            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
                    )
                }

                is Painter -> {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            horizontal = if (icon == null && leadingIcon == null) 8.dp else 0.dp,
                        ).padding(end = 8.dp),
            ) {
                AniTrendCategoryItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty()) {
                    AniTrendCategoryItemDescription(text = description, enabled = enabled)
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            if (trailingIcon != null) {
                VerticalDivider(
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    thickness = 1.dp,
                )
                trailingIcon.invoke()
            }
        }
    }
}

@Composable
fun AniTrendCategoryItemTitle(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 2,
    enabled: Boolean,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground.applyOpacity(enabled),
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun AniTrendCategoryHeader(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, top = 20.dp, bottom = 8.dp),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = text,
        modifier = modifier.padding(contentPadding),
        color = color,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
fun AniTrendCategoryItemDescription(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled),
        overflow = TextOverflow.Ellipsis,
    )
}

@AniTrendPreview.Default
@Composable
private fun AniTrendCategoryPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        Column {
            AniTrendCategoryHeader(text = "Preview")
            AniTrendCategoryItem(
                title = "Title",
                description = "Description",
            )
            AniTrendCategoryItem(
                title = "Title",
                description = "Description",
                icon = Icons.Outlined.Update,
            )
        }
    }
}

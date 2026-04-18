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
package co.anitrend.android.core.compose.design.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes

@Composable
fun AniTrendHintCard(
    title: String,
    description: String?,
    icon: ImageVector? = Icons.Outlined.Translate,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    currentValue: String? = null,
    actionLabel: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val cardModifier =
        if (onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        }

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .then(cardModifier),
        shape = MaterialTheme.shapes.extraLarge,
        color = containerColor,
        contentColor = contentColor,
        border =
            CardDefaults.outlinedCardBorder().copy(
                brush =
                    androidx.compose.ui.graphics
                        .SolidColor(contentColor.copy(alpha = 0.08f)),
            ),
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.Top,
        ) {
            icon?.let {
                Surface(
                    modifier = Modifier.padding(top = 2.dp, end = 16.dp),
                    color = contentColor.copy(alpha = 0.12f),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp).size(22.dp),
                        tint = contentColor,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColor,
                    )
                    if (!currentValue.isNullOrBlank()) {
                        Surface(
                            color = contentColor.copy(alpha = 0.14f),
                            contentColor = contentColor,
                            shape = MaterialTheme.shapes.large,
                        ) {
                            Text(
                                text = currentValue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = contentColor,
                            )
                        }
                    }
                }
                if (description != null) {
                    Text(
                        text = description,
                        modifier = Modifier.padding(top = 6.dp),
                        color = contentColor.copy(alpha = 0.92f),
                        maxLines = if (actionLabel.isNullOrBlank()) 3 else 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (!actionLabel.isNullOrBlank() && onClick != null) {
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = actionLabel,
                            color = contentColor,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp).size(16.dp),
                            tint = contentColor,
                        )
                    }
                }
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
fun AniTrendHintCardPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    CompositionLocalProvider(LocalTonalPalettes provides Color.Green.toTonalPalettes()) {
        PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
            Column {
                AniTrendHintCard(
                    title = "Explore new features",
                    icon = Icons.Outlined.TipsAndUpdates,
                    description = "Find out what's new in this version",
                    currentValue = "Latest",
                    actionLabel = "Open release notes",
                    onClick = {},
                )
                Spacer(modifier = Modifier.size(4.dp))
                AniTrendHintCard(
                    title = "Language and region",
                    icon = Icons.Outlined.Translate,
                    description = "Compare your current app locale against your device defaults.",
                    currentValue = "English",
                )
            }
        }
    }
}

/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.updater.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.updater.R
import co.anitrend.updater.component.viewmodel.UpdateChannel
import co.anitrend.updater.component.viewmodel.UpdateCheckState
import co.anitrend.updater.component.viewmodel.UpdateUiState
import co.anitrend.updater.component.viewmodel.UpdateViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
private fun UpdateContent(
    modifier: Modifier = Modifier,
    uiState: UpdateUiState,
    onChannelSelected: (UpdateChannel) -> Unit,
    onAutoCheckChange: (Boolean) -> Unit,
    onIncludePreviewBuildsChange: (Boolean) -> Unit,
    onCheckForUpdates: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        item {
            AniTrendHintCard(
                title = stringResource(R.string.title_updater_hub),
                description = heroDescription(uiState),
                icon = Icons.Outlined.SystemUpdate,
                currentValue = uiState.version,
                actionLabel = stringResource(R.string.action_updater_check_now),
                onClick = onCheckForUpdates,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
        item {
            UpdateSectionCard(
                title = stringResource(R.string.title_updater_installed_build),
                description = stringResource(R.string.summary_updater_installed_build),
            ) {
                UpdateInfoRow(label = stringResource(R.string.label_updater_version), value = uiState.version)
                UpdateInfoRow(label = stringResource(R.string.label_updater_build), value = uiState.build)
                UpdateInfoRow(label = stringResource(R.string.label_updater_version_code), value = uiState.code)
                UpdateInfoRow(label = stringResource(R.string.label_updater_distribution), value = uiState.source)
                UpdateInfoRow(label = stringResource(R.string.label_updater_locale), value = uiState.locale)
                UpdateInfoRow(label = stringResource(R.string.label_updater_build_type), value = uiState.buildType)
            }
        }
        item {
            UpdateSectionCard(
                title = stringResource(R.string.title_updater_release_channel),
                description = stringResource(R.string.summary_updater_release_channel),
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    UpdateChannel.entries.forEach { channel ->
                        FilterChip(
                            selected = uiState.selectedChannel == channel,
                            onClick = { onChannelSelected(channel) },
                            label = { Text(text = channelLabel(channel)) },
                        )
                    }
                }
                UpdateToggleRow(
                    title = stringResource(R.string.label_updater_auto_check),
                    summary = stringResource(R.string.summary_updater_auto_check),
                    checked = uiState.autoCheckEnabled,
                    onCheckedChange = onAutoCheckChange,
                )
                UpdateToggleRow(
                    title = stringResource(R.string.label_updater_include_preview),
                    summary = stringResource(R.string.summary_updater_include_preview),
                    checked = uiState.includePreviewBuilds,
                    onCheckedChange = onIncludePreviewBuildsChange,
                )
            }
        }
        item {
            OutlinedCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text(
                    text = stringResource(R.string.summary_updater_local_only),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun UpdateSectionCard(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            content()
        }
    }
}

@Composable
private fun UpdateInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun UpdateToggleRow(
    title: String,
    summary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun channelLabel(channel: UpdateChannel): String =
    when (channel) {
        UpdateChannel.STABLE -> stringResource(R.string.label_updater_channel_stable)
        UpdateChannel.BETA -> stringResource(R.string.label_updater_channel_beta)
        UpdateChannel.EXPERIMENTAL -> stringResource(R.string.label_updater_channel_experimental)
    }

@Composable
private fun heroDescription(uiState: UpdateUiState): String =
    when (uiState.checkState) {
        UpdateCheckState.IDLE ->
            stringResource(
                R.string.summary_updater_overview,
                uiState.version,
                uiState.buildType,
            )

        UpdateCheckState.MANUAL_ONLY ->
            stringResource(
                R.string.summary_updater_manual_check_result,
                uiState.version,
            )
    }

@Composable
fun UpdateScreenContent(
    onBackPress: () -> Unit,
    viewModel: UpdateViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState
    DefaultScaffold(
        onBackPress = onBackPress,
    ) { innerPadding ->
        UpdateContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onChannelSelected = viewModel::selectChannel,
            onAutoCheckChange = viewModel::setAutoCheckEnabled,
            onIncludePreviewBuildsChange = viewModel::setIncludePreviewBuilds,
            onCheckForUpdates = viewModel::checkForUpdates,
        )
    }
}

@AniTrendPreview.Default
@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun UpdateComposablePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        UpdateContent(
            uiState =
                UpdateUiState(
                    appLabel = "AniTrend",
                    version = "11.4.0",
                    build = "2026.04.18",
                    source = "com.android.vending",
                    code = "110400",
                    locale = "en-ZA",
                    buildType = "release",
                    selectedChannel = UpdateChannel.BETA,
                    includePreviewBuilds = true,
                    checkState = UpdateCheckState.MANUAL_ONLY,
                ),
            onChannelSelected = {},
            onAutoCheckChange = {},
            onIncludePreviewBuildsChange = {},
            onCheckForUpdates = {},
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun UpdateIdlePreview() {
    PreviewTheme(darkTheme = true, wrapInSurface = true) {
        UpdateContent(
            uiState =
                UpdateUiState(
                    appLabel = "AniTrend",
                    version = "11.4.0",
                    build = "2026.04.18",
                    source = "com.android.vending",
                    code = "110400",
                    locale = "en-ZA",
                    buildType = "release",
                    selectedChannel = UpdateChannel.STABLE,
                    includePreviewBuilds = false,
                    checkState = UpdateCheckState.IDLE,
                ),
            onChannelSelected = {},
            onAutoCheckChange = {},
            onIncludePreviewBuildsChange = {},
            onCheckForUpdates = {},
        )
    }
}

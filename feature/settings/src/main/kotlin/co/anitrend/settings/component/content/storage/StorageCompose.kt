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
package co.anitrend.settings.component.content.storage

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.slider.AniTrendSliderItem
import co.anitrend.android.core.storage.contract.IStorageController
import co.anitrend.android.core.storage.enums.StorageType
import co.anitrend.android.core.storage.extensions.toHumanReadableByteValue
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionSegment
import co.anitrend.data.settings.cache.ICacheSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsHeroCard
import co.anitrend.settings.component.compose.SettingsSectionCard

private data class StorageSummary(
    val allowanceBytes: Long,
    val freeBytes: Long,
    val logsBytes: Long,
    val imageBytes: Long,
    val videoBytes: Long,
    val offlineBytes: Long,
) {
    val usedBytes: Long
        get() = logsBytes + imageBytes + videoBytes + offlineBytes

    val remainingAllowanceBytes: Long
        get() = (allowanceBytes - usedBytes).coerceAtLeast(0L)
}

@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    settings: ICacheSettings = koinInject(),
    storageController: IStorageController = koinInject(),
) {
    val options = listOf(0.10f, 0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.50f)
    var selectedRatio by remember { mutableFloatStateOf(options.minByOrNull { abs(it - settings.cacheUsageRatio.value) } ?: options.first()) }
    val context = LocalContext.current
    val storageSummary by produceState<StorageSummary?>(initialValue = null, selectedRatio, context, storageController) {
        value =
            withContext(Dispatchers.IO) {
                StorageSummary(
                    allowanceBytes = storageController.getStorageUsageLimit(context, StorageType.CACHE, settings),
                    freeBytes = storageController.getFreeSpace(context, StorageType.CACHE),
                    logsBytes = storageController.getLogsCache(context).directorySize(),
                    imageBytes = storageController.getImageCache(context).directorySize(),
                    videoBytes = storageController.getVideoCache(context).directorySize(),
                    offlineBytes = storageController.getVideoOfflineCache(context).directorySize(),
                )
            }
    }

    StorageContent(
        modifier = modifier,
        selectedRatio = selectedRatio,
        storageSummary = storageSummary,
        onRatioChanged = { value ->
            val snapped = options.minByOrNull { abs(it - value) } ?: value
            settings.cacheUsageRatio.value = snapped
            selectedRatio = snapped
        },
    )
}

@Composable
private fun StorageContent(
    modifier: Modifier = Modifier,
    selectedRatio: Float,
    storageSummary: StorageSummary?,
    onRatioChanged: (Float) -> Unit,
) {
    val resources = LocalContext.current.resources
    val currentLimit = stringResource(R.string.summary_settings_storage_current_limit, percentLabel(resources, selectedRatio))
    val extraInfo = stringResource(R.string.summary_settings_storage_cache_allowance_info)
    val deviceSegments = rememberDeviceSegments(storageSummary)
    val usageSegments = rememberUsageSegments(storageSummary)
    LazyColumn(modifier = modifier) {
        item {
            SettingsHeroCard(
                title = stringResource(R.string.title_settings_storage_budget),
                description = stringResource(R.string.summary_settings_storage_budget),
                icon = Icons.Outlined.Storage,
                currentValue = percentLabel(resources, selectedRatio),
            )
        }
        item {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_storage_cache_allowance),
                description = currentLimit,
            ) {
                AniTrendSliderItem(
                    value = { selectedRatio },
                    onValueChange = onRatioChanged,
                    valueRange = 0.10f..0.50f,
                    steps = 6,
                    valueLabel = { percentLabel(resources, it) },
                    extraInfo = { extraInfo },
                    progress = { selectedRatio / 0.50f },
                )
            }
        }
        storageSummary?.let { summary ->
            item {
                SettingsSectionCard(
                    title = stringResource(R.string.title_settings_storage_device_overview),
                    description = stringResource(R.string.summary_settings_storage_device_overview),
                ) {
                    StatusDistributionBar(segments = deviceSegments)
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_allowance),
                            value = summary.allowanceBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_free_space),
                            value = summary.freeBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
            item {
                SettingsSectionCard(
                    title = stringResource(R.string.title_settings_storage_cache_breakdown),
                    description = stringResource(R.string.summary_settings_storage_cache_breakdown),
                ) {
                    StatusDistributionBar(segments = usageSegments)
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_used_cache),
                            value = summary.usedBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_remaining_allowance),
                            value = summary.remainingAllowanceBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_logs),
                            value = summary.logsBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.error,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_images),
                            value = summary.imageBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_video),
                            value = summary.videoBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        ChartLegendRow(
                            label = stringResource(R.string.label_settings_storage_offline),
                            value = summary.offlineBytes.toHumanReadableByteValue(),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        } ?: item {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_storage_device_overview),
                description = stringResource(R.string.summary_settings_storage_unavailable),
            ) {
            }
        }
    }
}

@Composable
private fun rememberDeviceSegments(storageSummary: StorageSummary?): List<StatusDistributionSegment> {
    if (storageSummary == null || storageSummary.freeBytes <= 0L) {
        return emptyList()
    }

    val allowanceFraction = (storageSummary.allowanceBytes.toFloat() / storageSummary.freeBytes.toFloat()).coerceIn(0f, 1f)
    val remainingFraction = (1f - allowanceFraction).coerceAtLeast(0f)
    return listOf(
        StatusDistributionSegment(
            label = "Allowance",
            value = storageSummary.allowanceBytes.toHumanReadableByteValue(),
            fraction = allowanceFraction,
            color = MaterialTheme.colorScheme.primary,
        ),
        StatusDistributionSegment(
            label = "Free",
            value = (storageSummary.freeBytes - storageSummary.allowanceBytes).coerceAtLeast(0L).toHumanReadableByteValue(),
            fraction = remainingFraction,
            color = MaterialTheme.colorScheme.tertiary,
        ),
    )
}

@Composable
private fun rememberUsageSegments(storageSummary: StorageSummary?): List<StatusDistributionSegment> {
    if (storageSummary == null || storageSummary.allowanceBytes <= 0L) {
        return emptyList()
    }

    val usedFraction = (storageSummary.usedBytes.toFloat() / storageSummary.allowanceBytes.toFloat()).coerceIn(0f, 1f)
    val remainingFraction = (1f - usedFraction).coerceAtLeast(0f)
    return listOf(
        StatusDistributionSegment(
            label = "Used",
            value = storageSummary.usedBytes.toHumanReadableByteValue(),
            fraction = usedFraction,
            color = MaterialTheme.colorScheme.primary,
        ),
        StatusDistributionSegment(
            label = "Remaining",
            value = storageSummary.remainingAllowanceBytes.toHumanReadableByteValue(),
            fraction = remainingFraction,
            color = MaterialTheme.colorScheme.secondary,
        ),
    )
}

private fun File.directorySize(): Long {
    if (!exists()) {
        return 0L
    }

    return walkTopDown()
        .filter { it.isFile }
        .sumOf { it.length() }
}

private fun percentLabel(
    resources: Resources,
    value: Float,
): String = resources.getString(R.string.label_settings_storage_percent, (value * 100).roundToInt())

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun StorageScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        StorageContent(
            selectedRatio = 0.25f,
            storageSummary =
                StorageSummary(
                    allowanceBytes = 1_073_741_824L,
                    freeBytes = 4_294_967_296L,
                    logsBytes = 41_943_040L,
                    imageBytes = 314_572_800L,
                    videoBytes = 167_772_160L,
                    offlineBytes = 125_829_120L,
                ),
            onRatioChanged = {},
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun StorageScreenUnavailablePreview() {
    PreviewTheme(wrapInSurface = true, darkTheme = true) {
        StorageContent(
            selectedRatio = 0.20f,
            storageSummary = null,
            onRatioChanged = {},
        )
    }
}

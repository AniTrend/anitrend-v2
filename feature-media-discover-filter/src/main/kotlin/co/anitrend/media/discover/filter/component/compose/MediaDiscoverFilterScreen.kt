package co.anitrend.media.discover.filter.component.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.enums.contract.IAliasable
import co.anitrend.domain.media.enums.MediaCountry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.MediaDiscoverRouter

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> FilterChipGroup(
    entries: List<T>,
    @StringRes title: Int,
    @StringRes subTitle: Int,
    labelFormatter: (T) -> String,
    isSelected: ((T) -> Boolean?) = { null },
    selectedItemIcon: ImageVector = Icons.Filled.Done,
    onSelectionChange : (T, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(subTitle),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    FlowRow {
        entries.forEach { entry ->
            val labelText = labelFormatter(entry)
            val selected = isSelected(entry) ?: false

            FilterChip(
                modifier = Modifier.padding(end = 8.dp),
                selected = selected,
                onClick = {
                    onSelectionChange(entry, !selected)
                },
                label = { Text(text = labelText) },
                leadingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = selectedItemIcon,
                            contentDescription = labelText,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterYearSection(
    dateHelper: AbstractSupportDateHelper,
    onSelectionChange: () -> Unit,
) {
    val upperYearBound = dateHelper.getCurrentYear(2).toFloat()
    var isYearRangeSelected by remember { mutableStateOf(false) }
    Text(
        text = stringResource(co.anitrend.media.discover.filter.R.string.label_discover_filter_year_title),
        style = MaterialTheme.typography.bodyMedium
    )

    FilterChip(
        selected = isYearRangeSelected,
        onClick = {
            isYearRangeSelected = !isYearRangeSelected
        },
        label = {
            Text(text = stringResource(co.anitrend.media.discover.filter.R.string.label_discover_filter_year_title_by_range))
        }
    )

    if (isYearRangeSelected) {
        RangeSlider(
            value = 1970f..upperYearBound,
            onValueChange = {

            }
        )
    } else {
        Slider(
            state = SliderState(
                value = upperYearBound,
                steps = 1,
                valueRange = 1970f..upperYearBound,
            )
        )
    }
}

@Composable
fun MediaDiscoverFilterScreen(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.Param,
    onParamChange: (MediaDiscoverRouter.Param) -> Unit
) {
    val state by remember { mutableStateOf(param) }
    val titleFormatter: (IAliasable) -> String = { it.alias.toString() }
    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterYearSection(
            dateHelper = dateHelper,
            onSelectionChange = {

            }
        )
        FilterChipGroup(
            entries = MediaStatus.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_status_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_status_sub_title,
            labelFormatter = titleFormatter,
            isSelected = {
                param.status_in?.contains(it) == true || param.status == it
            },
            onSelectionChange = { item, _ ->
                state.status = item
                onParamChange(state)
            }
        )
        FilterChipGroup(
            entries = MediaType.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_media_type_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_media_type_sub_title,
            labelFormatter = titleFormatter,
            isSelected = {
                param.type == it
            },
            onSelectionChange = { item, _ ->
                state.type = item
                onParamChange(state)
            }
        )
        FilterChipGroup(
            entries = MediaSeason.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_season_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_season_sub_title,
            labelFormatter = titleFormatter,
            isSelected = {
                param.season == it
            },
            onSelectionChange = { item, _ ->
                state.season = item
                onParamChange(state)
            }
        )
        FilterChipGroup(
            entries = MediaFormat.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_format_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_format_sub_title,
            labelFormatter = titleFormatter,
            isSelected = {
                param.format_in?.contains(it) == true || param.format == it
            },
            onSelectionChange = { item, _ ->
                state.format = item
                onParamChange(state)
            }
        )
        FilterChipGroup(
            entries = MediaSource.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_source_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_source_sub_title,
            labelFormatter = titleFormatter,
            isSelected = {
                param.source_in?.contains(it) == true || param.source == it
            },
            onSelectionChange = { item, _ ->
                state.source = item
                onParamChange(state)
            }
        )
        FilterChipGroup(
            entries = MediaCountry.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_source_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_source_sub_title,
            labelFormatter = { it.name.lowercase().capitalizeWords() },
            isSelected = {
                param.countryOfOrigin == it.alias
            },
            onSelectionChange = { item, _ ->
                state.countryOfOrigin = item.alias
                onParamChange(state)
            },
        )
        FilterChipGroup(
            entries = MediaLicensor.entries,
            title = co.anitrend.media.discover.filter.R.string.label_discover_filter_streaming_title,
            subTitle = co.anitrend.media.discover.filter.R.string.label_discover_filter_streaming_sub_title,
            labelFormatter = { it.title.toString() },
            isSelected = {
                param.licensedBy_in?.contains(it) == true || param.licensedBy == it
            },
            onSelectionChange = { item, _ ->
                state.licensedBy = item
                onParamChange(state)
            },
        )
    }
}

@AniTrendPreview.Mobile
@Composable
fun MediaDiscoverFilterScreenPreview() {
    PreviewTheme {
        MediaDiscoverFilterScreen(
            dateHelper = object : AbstractSupportDateHelper() {
                override val currentSeason: SeasonType = SeasonType.FALL
                override fun getCurrentYear(delta: Int) = 2023 + delta
            },
            param = MediaDiscoverRouter.Param(
                status = MediaStatus.FINISHED,
                type = MediaType.ANIME,
                season = MediaSeason.SPRING,
            ),
            onParamChange = {},
        )
    }
}

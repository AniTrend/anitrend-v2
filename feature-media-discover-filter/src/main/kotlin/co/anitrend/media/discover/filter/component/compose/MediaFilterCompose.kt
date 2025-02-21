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
package co.anitrend.media.discover.filter.component.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.domain.common.enums.contract.IAliasable
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaCountry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.media.discover.filter.R
import co.anitrend.media.discover.filter.component.spec.FilterDefinition.Companion.buildSummaryItems
import co.anitrend.media.discover.filter.component.spec.data.filterDefinitions
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.sorting.Sorting

/**
 * Simple data class to track your expanded/collapsed filter sections.
 */
internal data class FilterUiState(
    val isBasicExpanded: Boolean = true,
    val isAdvancedExpanded: Boolean = false,
)

/**
 * Reusable component for displaying a section header (title and subtitle).
 */
@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

/**
 * Reusable "Clear All" action button.
 */
@Composable
fun ClearAllAction(
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(onClick = onClearAll, modifier = modifier) {
        Text(text = stringResource(id = R.string.text_clear_all))
    }
}

/**
 * An expandable/collapsible section with a title.
 */
@Composable
private fun FilterSection(
    title: String,
    expanded: Boolean,
    onExpandToggled: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onExpandToggled) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                )
            }
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column { content() }
        }
    }
}

/**
 * Shows a row of chips summarizing the user's current selections.
 */
@Composable
private fun SelectedFiltersSummary(
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    val selectedItems =
        remember(param) {
            filterDefinitions.buildSummaryItems(
                param = param,
                onParamChange = onParamChange,
            )
        }

    if (selectedItems.isEmpty()) return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.title_selected_filters),
            style = MaterialTheme.typography.headlineSmall,
        )
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .clipToBounds(),
        ) {
            selectedItems.forEach { (label, removeAction) ->
                FilterChip(
                    selected = true,
                    onClick = removeAction,
                    label = {
                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.content_description_remove_filter),
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}

/**
 * The "Basic Filters" section content.
 */
@Composable
private fun BasicFilters(
    modifier: Modifier = Modifier,
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SortingFilterChipGroup(
            currentSort = param.sort,
            onSortChange = { newSort ->
                onParamChange(param.copy(sort = newSort))
            },
        )
        YearRangeFilter(
            dateHelper = dateHelper,
            param = param,
            onParamChange = onParamChange,
        )
        MultiSelectFilterChipGroup(
            title = stringResource(id = R.string.title_status),
            subTitle = stringResource(id = R.string.subtitle_status),
            entries = MediaStatus.entries,
            isSelected = { status ->
                param.status_in?.contains(status) == true || param.status == status
            },
            onSelectionChange = { item, selected ->
                val currentList = param.status_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    onParamChange(
                        param.copy(
                            status = null,
                            status_in = currentList.apply { if (!contains(item)) add(item) },
                        ),
                    )
                } else {
                    currentList.remove(item)
                    onParamChange(param.copy(status_in = currentList.ifEmpty { null }))
                }
            },
            onDeselectAll = {
                onParamChange(param.copy(status = null, status_in = null))
            },
        )
        SingleSelectFilterChipGroup(
            title = stringResource(id = R.string.title_media_type),
            subTitle = stringResource(id = R.string.subtitle_media_type),
            entries = MediaType.entries,
            isSelected = { type -> param.type == type },
            onSelectionChange = { selectedType ->
                onParamChange(param.copy(type = selectedType))
            },
        )
        SingleSelectFilterChipGroup(
            title = stringResource(id = R.string.title_season),
            subTitle = stringResource(id = R.string.subtitle_season),
            entries = MediaSeason.entries,
            isSelected = { season -> param.season == season },
            onSelectionChange = { selectedSeason ->
                onParamChange(param.copy(season = selectedSeason))
            },
        )
    }
}

/**
 * The "Advanced Filters" section content.
 */
@Composable
private fun AdvancedFilters(
    modifier: Modifier = Modifier,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MultiSelectFilterChipGroup(
            title = stringResource(id = R.string.label_discover_filter_format_title),
            subTitle = stringResource(id = R.string.label_discover_filter_format_sub_title),
            entries = MediaFormat.entries,
            isSelected = { format ->
                param.format_in?.contains(format) == true || param.format == format
            },
            onSelectionChange = { item, selected ->
                val currentList = param.format_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    onParamChange(
                        param.copy(
                            format = null,
                            format_in = currentList.apply { if (!contains(item)) add(item) },
                        ),
                    )
                } else {
                    currentList.remove(item)
                    onParamChange(param.copy(format_in = currentList.ifEmpty { null }))
                }
            },
            onDeselectAll = {
                onParamChange(param.copy(format = null, format_in = null))
            },
        )
        MultiSelectFilterChipGroup(
            title = stringResource(id = R.string.label_discover_filter_source_title),
            subTitle = stringResource(id = R.string.label_discover_filter_source_sub_title),
            entries = MediaSource.entries,
            isSelected = { source ->
                param.source_in?.contains(source) == true || param.source == source
            },
            onSelectionChange = { item, selected ->
                val currentList = param.source_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    onParamChange(
                        param.copy(
                            source = null,
                            source_in = currentList.apply { if (!contains(item)) add(item) },
                        ),
                    )
                } else {
                    currentList.remove(item)
                    onParamChange(param.copy(source_in = currentList.ifEmpty { null }))
                }
            },
            onDeselectAll = {
                onParamChange(param.copy(source = null, source_in = null))
            },
        )
        SingleSelectFilterChipGroup(
            title = stringResource(id = R.string.label_discover_filter_country_title),
            subTitle = stringResource(id = R.string.label_discover_filter_country_sub_title),
            entries = MediaCountry.entries,
            isSelected = { country ->
                param.countryOfOrigin == country.alias
            },
            onSelectionChange = { selectedCountry ->
                onParamChange(param.copy(countryOfOrigin = selectedCountry.alias))
            },
        )
        MultiSelectFilterChipGroup(
            title = stringResource(id = R.string.label_discover_filter_format_title), // Adjust title as needed
            subTitle = stringResource(id = R.string.label_discover_filter_format_sub_title),
            entries = MediaLicensor.entries,
            isSelected = { licensor ->
                param.licensedBy_in?.contains(licensor) == true || param.licensedBy == licensor
            },
            onSelectionChange = { item, selected ->
                val currentList = param.licensedBy_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    onParamChange(
                        param.copy(
                            licensedBy = null,
                            licensedBy_in = currentList.apply { if (!contains(item)) add(item) },
                        ),
                    )
                } else {
                    currentList.remove(item)
                    onParamChange(param.copy(licensedBy_in = currentList.ifEmpty { null }))
                }
            },
            onDeselectAll = {
                onParamChange(param.copy(licensedBy = null, licensedBy_in = null))
            },
        )
    }
}

/**
 * A single range slider for year selection.
 */
@Composable
private fun YearRangeFilter(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    val minYear = param.seasonYear?.toFloat() ?: 1970f
    val maxYear = dateHelper.getCurrentYear(2).toFloat()
    var yearRange by remember { mutableStateOf(minYear.toInt() to maxYear.toInt()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(
            title = stringResource(id = R.string.title_year_range),
            subtitle = stringResource(id = R.string.description_year_range),
        )
        RangeSlider(
            value = yearRange.first.toFloat()..yearRange.second.toFloat(),
            onValueChange = { newRange ->
                val start = newRange.start.toInt()
                val end = newRange.endInclusive.toInt()
                yearRange = start to end
            },
            onValueChangeFinished = {
                if (yearRange.first == yearRange.second) {
                    onParamChange(param.copy(seasonYear = yearRange.first))
                }
                onParamChange(
                    param.copy(
                        startDate_lesser = "${yearRange.first}0000",
                        startDate_greater = "${yearRange.second}0000",
                    ),
                )
            },
            valueRange = minYear..maxYear,
            steps = (maxYear - minYear).toInt() - 1,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("${yearRange.first}")
            Text("${yearRange.second}")
        }
    }
}

/**
 * A composable that displays a row of sort filter chips.
 */
@Composable
fun SortingFilterChipGroup(
    currentSort: List<Sorting<MediaSort>>?,
    onSortChange: (List<Sorting<MediaSort>>?) -> Unit,
    availableSort: List<MediaSort> = MediaSort.entries.toList(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SectionHeader(
            title = stringResource(id = R.string.title_sort_by),
            subtitle = stringResource(id = R.string.description_sort_by),
        )
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .clipToBounds(),
        ) {
            availableSort.forEach { sortOption ->
                val existingSort = currentSort?.find { it.sortable == sortOption }
                val isSelected = existingSort != null
                val sortOrder = existingSort?.order ?: SortOrder.ASC

                val onChipClick = {
                    val newList = currentSort?.toMutableList() ?: mutableListOf()
                    val index = newList.indexOfFirst { it.sortable == sortOption }
                    if (index == -1) {
                        newList.add(Sorting(sortOption, SortOrder.DESC))
                    } else {
                        if (newList[index].order == SortOrder.DESC) {
                            newList[index] = newList[index].copy(order = SortOrder.ASC)
                        } else {
                            newList.removeAt(index)
                        }
                    }
                    onSortChange(newList.ifEmpty { null })
                }
                FilterChip(
                    selected = isSelected,
                    onClick = onChipClick,
                    label = {
                        Text(
                            text = sortOption.alias.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    leadingIcon = {
                        if (isSelected) {
                            val arrowIcon: ImageVector =
                                if (sortOrder == SortOrder.ASC) {
                                    Icons.Filled.ArrowCircleUp
                                } else {
                                    Icons.Filled.ArrowCircleDown
                                }
                            Icon(
                                imageVector = arrowIcon,
                                contentDescription =
                                    stringResource(
                                        id =
                                            if (sortOrder == SortOrder.DESC) {
                                                R.string.content_description_descending
                                            } else {
                                                R.string.content_description_ascending
                                            },
                                    ),
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}

/**
 * A reusable multi-select filter chip group with a "Deselect All" option.
 */
@Composable
private fun <T> MultiSelectFilterChipGroup(
    title: String,
    subTitle: String,
    entries: List<T>,
    isSelected: (T) -> Boolean,
    onSelectionChange: (item: T, selected: Boolean) -> Unit,
    onDeselectAll: () -> Unit,
    labelFormatter: (T) -> String = { if (it is IAliasable) it.alias.toString() else it.toString() },
    selectedItemIcon: ImageVector = Icons.Filled.Done,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(title = title, subtitle = subTitle)
            ClearAllAction(onClearAll = onDeselectAll)
        }
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .clipToBounds(),
        ) {
            entries.forEach { entry ->
                val labelText = labelFormatter(entry)
                val selected = isSelected(entry)
                FilterChip(
                    modifier = Modifier.padding(end = 8.dp),
                    selected = selected,
                    onClick = { onSelectionChange(entry, !selected) },
                    label = { Text(text = labelText) },
                    leadingIcon = {
                        if (selected) {
                            Icon(
                                imageVector = selectedItemIcon,
                                contentDescription = labelText,
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    },
                )
            }
        }
    }
}

/**
 * A reusable single-select filter chip group.
 */
@Composable
private fun <T> SingleSelectFilterChipGroup(
    title: String,
    subTitle: String,
    entries: List<T>,
    isSelected: (T) -> Boolean,
    onSelectionChange: (T) -> Unit,
    labelFormatter: (T) -> String = { if (it is IAliasable) it.alias.toString() else it.toString() },
    selectedItemIcon: ImageVector = Icons.Filled.Done,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SectionHeader(title = title, subtitle = subTitle)
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .clipToBounds(),
        ) {
            entries.forEach { entry ->
                val labelText = labelFormatter(entry)
                val selected = isSelected(entry)
                FilterChip(
                    modifier = Modifier.padding(end = 8.dp),
                    selected = selected,
                    onClick = { onSelectionChange(entry) },
                    label = { Text(text = labelText) },
                    leadingIcon = {
                        if (selected) {
                            Icon(
                                imageVector = selectedItemIcon,
                                contentDescription = labelText,
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    },
                )
            }
        }
    }
}

/**
 * A single row at the bottom for "Apply" and "Clear All" actions.
 */
@Composable
private fun ActionsRow(
    modifier: Modifier = Modifier,
    onApply: () -> Unit,
    onClearAll: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        ClearAllAction(onClearAll = onClearAll)
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onApply) {
            Text(stringResource(id = R.string.text_apply))
        }
    }
}

/**
 * Top-level composable that displays the media filter screen.
 */
@Composable
fun MediaFilterScreen(
    modifier: Modifier = Modifier,
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    var filterUiState by remember { mutableStateOf(FilterUiState()) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SelectedFiltersSummary(
            param = param,
            onParamChange = onParamChange,
        )
        FilterSection(
            title = stringResource(id = R.string.title_basic_filters),
            expanded = filterUiState.isBasicExpanded,
            onExpandToggled = {
                filterUiState = filterUiState.copy(isBasicExpanded = !filterUiState.isBasicExpanded)
            },
        ) {
            BasicFilters(
                dateHelper = dateHelper,
                param = param,
                onParamChange = onParamChange,
            )
        }
        FilterSection(
            title = stringResource(id = R.string.title_advanced_filters),
            expanded = filterUiState.isAdvancedExpanded,
            onExpandToggled = {
                filterUiState = filterUiState.copy(isAdvancedExpanded = !filterUiState.isAdvancedExpanded)
            },
        ) {
            AdvancedFilters(
                param = param,
                onParamChange = onParamChange,
            )
        }
        ActionsRow(
            modifier = Modifier.padding(16.dp),
            onApply = onDismiss,
            onClearAll = {},
        )
    }
}

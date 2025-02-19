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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.domain.common.enums.contract.IAliasable
import co.anitrend.domain.common.sort.SortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaCountry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
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
            Column {
                content()
            }
        }
    }
}

/**
 * Shows a row of chips summarizing the user's current selections.
 * Each chip can be removed (clearing that specific filter).
 */
@Composable
private fun SelectedFiltersSummary(
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    // Gather selected filters into a list of label + removeAction
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
            text = "Selected Filters",
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
                            contentDescription = "Remove filter",
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
 * The "Basic Filters" section content:
 * - Single Range Slider for year selection (discrete steps)
 * - A multi-select example (Status) and single-select example (Type, Season)
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
        // Sorting section
        SortingFilterChipGroup(
            currentSort = param.sort,
            onSortChange = { newSort ->
                onParamChange(param.copy(sort = newSort))
            },
        )

        // Year Range
        YearRangeFilter(
            dateHelper = dateHelper,
            param = param,
            onParamChange = onParamChange,
        )

        // Status (multi-select example)
        MultiSelectFilterChipGroup(
            title = "Status",
            subTitle = "Select one or more statuses",
            entries = MediaStatus.entries,
            isSelected = { status ->
                // True if the item is in status_in or equals status
                param.status_in?.contains(status) == true || param.status == status
            },
            onSelectionChange = { item, selected ->
                val currentList = param.status_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    // If user picks a second status, remove the single param.status
                    // to avoid confusion between single vs. multi
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

        // Type (single-select example)
        SingleSelectFilterChipGroup(
            title = "Media Type",
            subTitle = "Select exactly one type",
            entries = MediaType.entries,
            isSelected = { type -> param.type == type },
            onSelectionChange = { selectedType ->
                onParamChange(param.copy(type = selectedType))
            },
        )

        // Season (single-select example)
        SingleSelectFilterChipGroup(
            title = "Season",
            subTitle = "Select exactly one season",
            entries = MediaSeason.entries,
            isSelected = { season -> param.season == season },
            onSelectionChange = { selectedSeason ->
                onParamChange(param.copy(season = selectedSeason))
            },
        )
    }
}

/**
 * The "Advanced Filters" section content:
 * - Format (multi-select example)
 * - Source (multi-select example)
 * - Licensor (multi-select example)
 * - CountryOfOrigin (single-select example, or adapt as needed)
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
        // Format (multi-select)
        MultiSelectFilterChipGroup(
            title = "Format",
            subTitle = "Select one or more formats",
            entries = MediaFormat.entries,
            isSelected = { format ->
                param.format_in?.contains(format) == true || param.format == format
            },
            onSelectionChange = { item, selected ->
                val currentList = param.format_in?.toMutableList() ?: mutableListOf()
                if (selected) {
                    // Clear single 'format' if we want multi
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

        // Source (multi-select)
        MultiSelectFilterChipGroup(
            title = "Source",
            subTitle = "Select one or more sources",
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

        // Country of Origin (single-select example)
        SingleSelectFilterChipGroup(
            title = "Country of Origin",
            subTitle = "Pick a single country",
            entries = MediaCountry.entries,
            isSelected = { country ->
                param.countryOfOrigin == country.alias
            },
            onSelectionChange = { selectedCountry ->
                onParamChange(param.copy(countryOfOrigin = selectedCountry.alias))
            },
        )

        // Licensor (multi-select)
        MultiSelectFilterChipGroup(
            title = "Licensor",
            subTitle = "Select one or more licensors",
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
 * Example of a single range slider for year selection (discrete steps).
 * Users can drag both handles to the same year if they only want a single year.
 */
@Composable
private fun YearRangeFilter(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    val minYear = param.seasonYear?.toFloat() ?: 1970f
    val maxYear = dateHelper.getCurrentYear(2).toFloat()
    // We assume param has yearRange: Pair<Int, Int>? or something similar
    // For demonstration, we just store in local state.
    var yearRange by remember { mutableStateOf(minYear.toInt() to maxYear.toInt()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Year Range",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Select a range of years (drag both ends). If both ends are the same, it's a single year.",
            style = MaterialTheme.typography.labelMedium,
        )
        RangeSlider(
            value = yearRange.first.toFloat()..yearRange.second.toFloat(),
            onValueChange = { newRange ->
                // Snap to discrete years
                val start = newRange.start.toInt()
                val end = newRange.endInclusive.toInt()
                yearRange = start to end
            },
            onValueChangeFinished = {
                // Update your param on finishing the drag
                onParamChange(
                    param.copy(seasonYear = yearRange.first),
                )
            },
            valueRange = minYear..maxYear,
            steps = (maxYear - minYear).toInt() - 1, // discrete steps for each year
        )

        // Display numeric values to user
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("${yearRange.first}")
            Text("${yearRange.second}")
        }
    }
}

/**
 * A composable that displays a row of sort filter chips.
 *
 * @param currentSort The currently selected sort criteria (a list of [SortWithOrder] for [MediaSort]).
 * @param onSortChange Callback to update the sort list (for example, updating the parameter).
 * @param availableSort List of available sort options (defaulting to all [MediaSort] entries).
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
        Text(
            text = "Sort By",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Tap to toggle sort order. Remove by tapping the close icon.",
            style = MaterialTheme.typography.labelLarge,
        )
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .clipToBounds(),
        ) {
            availableSort.forEach { sortOption ->
                // Find the active sort for this option, if any.
                val existingSort = currentSort?.find { it.sortable == sortOption }
                val isSelected = existingSort != null
                val sortOrder = existingSort?.order ?: SortOrder.ASC

                // Tri-state logic:
                // If not selected: add with DESC.
                // If DESC: toggle to ASC.
                // If ASC: remove the sort.
                val onChipClick = {
                    val newList = currentSort?.toMutableList() ?: mutableListOf()
                    val index = newList.indexOfFirst { it.sortable == sortOption }
                    if (index == -1) {
                        // Not selected, add with DESC.
                        newList.add(Sorting(sortOption, SortOrder.DESC))
                    } else {
                        if (newList[index].order == SortOrder.DESC) {
                            // Toggle to ASC.
                            newList[index] = newList[index].copy(order = SortOrder.ASC)
                        } else {
                            // Already ASC, remove sort.
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
                                if (sortOrder == SortOrder.ASC) Icons.Filled.ArrowCircleUp else Icons.Filled.ArrowCircleDown
                            Icon(
                                imageVector = arrowIcon,
                                contentDescription = if (sortOrder == SortOrder.DESC) "Descending" else "Ascending",
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
            Column {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                Text(text = subTitle, style = MaterialTheme.typography.labelLarge)
            }
            TextButton(onClick = onDeselectAll) {
                Text("Deselect All")
            }
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
                    onClick = {
                        onSelectionChange(entry, !selected)
                    },
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
 * A reusable single-select filter chip group (similar to a row of radio buttons).
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
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(text = subTitle, style = MaterialTheme.typography.labelLarge)

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
                    onClick = {
                        // Because it's single-select, clear other selections
                        onSelectionChange(entry)
                    },
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
        TextButton(onClick = onClearAll) {
            Text("Clear All")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onApply) {
            Text("Apply")
        }
    }
}

/**
 * An example of a top-level composable that:
 * - Shows a summary of selected filters
 * - Groups filters into Basic vs. Advanced sections (accordion style)
 * - Includes an Apply and Clear All row at the bottom
 */
@Composable
fun MediaFilterScreen(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    var filterUiState by remember { mutableStateOf(FilterUiState()) }
    val scrollState = rememberScrollState()

    // Container that holds all the filters in a column
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 1) Summary of Selected Filters (chips that can be removed)
        SelectedFiltersSummary(
            param = param,
            onParamChange = onParamChange,
        )

        // 2) Basic Filters
        FilterSection(
            title = "Basic Filters",
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

        // 3) Advanced Filters
        FilterSection(
            title = "Advanced Filters",
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

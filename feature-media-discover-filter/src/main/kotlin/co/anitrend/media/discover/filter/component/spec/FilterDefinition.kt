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
package co.anitrend.media.discover.filter.component.spec

import co.anitrend.navigation.MediaDiscoverRouter.MediaDiscoverParam

/**
 * A generic definition for a filter that supports both a single selection and multiple selections.
 *
 * @param T The type of the filter (for example, MediaStatus, MediaType, etc.)
 * @property getSingleValue Extracts the single selected value from the current parameter.
 * @property setSingleValue Updates the parameter with a new single value.
 * @property getMultiValue Extracts a list of selected values (if applicable).
 * @property setMultiValue Updates the parameter with a new list of selections.
 * @property label A function to produce a display label from a value.
 */
data class FilterDefinition<T>(
    val getSingleValue: (MediaDiscoverParam) -> T?,
    val setSingleValue: (MediaDiscoverParam, T?) -> MediaDiscoverParam,
    val getMultiValue: (MediaDiscoverParam) -> List<T>?,
    val setMultiValue: (MediaDiscoverParam, List<T>?) -> MediaDiscoverParam,
    val label: (T) -> String
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun FilterDefinition<*>.getLabelFor(value: Any): String {
            return (this as FilterDefinition<Any>).label(value)
        }

        @Suppress("UNCHECKED_CAST")
        fun List<FilterDefinition<out Any>>.buildSummaryItems(
            param: MediaDiscoverParam,
            onParamChange: (MediaDiscoverParam) -> Unit,
        ): List<Pair<String, () -> Unit>> {
            val items = mutableListOf<Pair<String, () -> Unit>>()
            forEach { definition ->
                // Handle single-selection filter
                val singleValue = definition.getSingleValue(param)
                if (singleValue != null) {
                    val label = definition.getLabelFor(singleValue)
                    items += label to {
                        val updated = definition.setSingleValue(param, null)
                        onParamChange(updated)
                    }
                }
                // Handle multi-selection filter
                val multiValues = definition.getMultiValue(param)
                multiValues?.forEach { element ->
                    val labelText = definition.getLabelFor(element)
                    items += labelText to {
                        val newList = multiValues.toMutableList().apply { remove(element) }// Cast definition to FilterDefinition<Any> so that the type matches
                        val updated = (definition as FilterDefinition<Any>).setMultiValue(param, newList.ifEmpty { null })
                        onParamChange(updated)
                    }
                }
            }
            return items
        }
    }
}

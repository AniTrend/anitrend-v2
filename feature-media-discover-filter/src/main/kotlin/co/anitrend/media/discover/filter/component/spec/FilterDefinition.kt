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

import co.anitrend.navigation.model.common.IParam

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
data class FilterDefinition<T, P : IParam>(
    val getSingleValue: (P) -> T?,
    val setSingleValue: (P, T?) -> P,
    val getMultiValue: (P) -> List<T>?,
    val setMultiValue: (P, List<T>?) -> P,
    val label: (T) -> String,
) {
    companion object {
        fun <T, P : IParam> List<FilterDefinition<T, P>>.buildSummaryItems(
            param: P,
            onParamChange: (P) -> Unit,
        ): List<Pair<String, () -> Unit>> {
            val items = mutableListOf<Pair<String, () -> Unit>>()
            forEach { definition ->
                val singleValue = definition.getSingleValue(param)
                if (singleValue != null) {
                    val label = definition.label(singleValue)
                    items += label to {
                        val updated = definition.setSingleValue(param, null)
                        onParamChange(updated)
                    }
                }
                // Handle multi-selection filter
                val multiValues = definition.getMultiValue(param)
                multiValues?.forEach { element ->
                    val labelText = definition.label(element)
                    items += labelText to {
                        val newList = multiValues.toMutableList().apply { remove(element) }
                        val updated = definition.setMultiValue(param, newList.ifEmpty { null })
                        onParamChange(updated)
                    }
                }
            }
            return items
        }
    }
}

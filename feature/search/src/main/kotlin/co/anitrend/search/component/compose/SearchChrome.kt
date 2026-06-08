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
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope

@Composable
internal fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(query)
                },
            ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    onSearch(query)
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.placeholder_search_query),
                )
            }
        },
        placeholder = {
            Text(text = stringResource(co.anitrend.search.R.string.placeholder_search_query))
        },
        modifier = modifier,
    )
}

@Composable
internal fun SearchScopeChips(
    scope: SearchScope,
    onScopeClick: (SearchScope) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(SearchScope.entries) { option ->
            FilterChip(
                selected = scope == option,
                onClick = { onScopeClick(option) },
                label = { Text(text = option.label()) },
            )
        }
    }
}

@Composable
internal fun SearchScope.label(): String =
    when (this) {
        SearchScope.HOME -> stringResource(R.string.label_search_scope_home)
        SearchScope.ALL -> stringResource(R.string.label_search_scope_all)
        SearchScope.ANIME -> stringResource(R.string.label_search_scope_anime)
        SearchScope.MANGA -> stringResource(R.string.label_search_scope_manga)
        SearchScope.USERS -> stringResource(R.string.label_search_scope_users)
        SearchScope.STUDIOS -> stringResource(R.string.label_search_scope_studios)
        SearchScope.STAFF -> stringResource(R.string.label_search_scope_staff)
        SearchScope.CHARACTERS -> stringResource(R.string.label_search_scope_characters)
    }

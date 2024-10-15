/*
 * Copyright (C) 2024 AniTrend
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf(query) }
    SearchBar(
        query = searchQuery,
        onQueryChange = { query ->
            searchQuery = query
            onQueryChange(query)
        },
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
        },
        placeholder = {
            Text(text = stringResource(co.anitrend.search.R.string.search_place_holder_text))
        },
        modifier = modifier,
    ) {}
}

@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(start = 16.dp, end = 16.dp),
    ) {
        SearchBarContent(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = onActiveChange,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(text = "Search results here")
    }
}

@Composable
fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                },
            )
        },
    ) { innerPadding ->
        SearchContent(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = onActiveChange,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun SearchScreenPreview() {
    PreviewTheme {
        SearchContent(
            query = "Fin",
            onQueryChange = { },
            onSearch = {},
            active = false,
            onActiveChange = {},
        )
    }
}

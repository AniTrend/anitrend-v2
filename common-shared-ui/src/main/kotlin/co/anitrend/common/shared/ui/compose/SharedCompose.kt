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
package co.anitrend.common.shared.ui.compose

import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem

@Composable
fun DefaultBottomAppBar(onBackPress: () -> Unit) {
    BottomAppBar(
        actions = {
            BackIconButton(onBackClick = onBackPress)
        },
    )
}

@Composable
fun DefaultScaffold(
    onBackPress: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            DefaultBottomAppBar(onBackPress)
        },
    ) { padding ->
        content(padding)
    }
}

@Composable
fun <T : Fragment> FragmentItemHost(
    modifier: Modifier = Modifier,
    fragmentItem: FragmentItem<T>,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val containerId = View.generateViewId()
            FragmentContainerView(context).apply { id = containerId }
        },
        update = { view ->
            fragmentItem.commit(view, view.context)
        },
    )
}

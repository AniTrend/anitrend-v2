/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.about.component.content

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.More
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.onBackgroundColor

@Composable
private fun CreateBottomAppBar(context: Context) {
    BottomAppBar {
        val parent = context as FragmentActivity
        IconButton(onClick = { parent.onBackPressed() }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Return to calling activity",
                tint = context.onBackgroundColor()
            )
        }
        // The actions should be at the end of the BottomAppBar
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { /* doSomething() */ }) {
            Icon(
                imageVector = Icons.Rounded.More,
                contentDescription = "Return to calling activity",
                tint = context.onBackgroundColor()
            )
        }
    }
}

@Preview
@Composable
fun AboutContent() {
    val context = LocalContext.current
    AniTrendTheme {
        Scaffold(
            bottomBar = {
                CreateBottomAppBar(context)
            }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Hello People"
                )
            }
        }
    }
}
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

package co.anitrend.core.android.extensions

import android.content.Intent
import android.view.View
import androidx.core.net.toUri
import timber.log.Timber

fun View.startViewIntent(url: String) {
    val intent = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        action = Intent.ACTION_VIEW
        data = url.toUri()
    }
    runCatching {
        context.startActivity(intent)
    }.onFailure { Timber.w(it) }
}
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
package co.anitrend.common.shared.ui.extension

import androidx.core.app.ShareCompat.IntentBuilder
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.extensions.stackTrace

/**
 * Triggers system intent chooser for easier content sharing
 *
 * @param action Delegates the complexities of creating an intent chooser
 */
inline fun FragmentActivity.shareContent(action: IntentBuilder.() -> IntentBuilder) =
    runCatching {
        val intentBuilder = IntentBuilder(this)
        action(intentBuilder)
            .createChooserIntent()
    }.onSuccess {
        it.run(::startActivity)
    }.stackTrace()

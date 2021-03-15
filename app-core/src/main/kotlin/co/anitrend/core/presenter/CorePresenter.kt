/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.presenter

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.core.android.settings.Settings
import timber.log.Timber

abstract class CorePresenter(
    context: Context,
    settings: Settings
) : SupportPresenter<Settings>(context, settings) {

    /**
     * Starts a view intent action given the [uri] as data
     */
    protected fun startViewIntent(uri: Uri) {
        val intent = Intent().apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            action = ACTION_VIEW
            data = uri
        }
        runCatching {
            context.startActivity(intent)
        }.onFailure { Timber.tag(moduleTag).w(it) }
    }
}
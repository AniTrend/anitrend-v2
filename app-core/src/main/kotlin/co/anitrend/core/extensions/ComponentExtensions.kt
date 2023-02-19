/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.core.extensions

import android.content.ComponentCallbacks
import co.anitrend.arch.analytics.contract.ISupportAnalytics
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import coil.ImageLoader
import org.koin.android.ext.android.get
import timber.log.Timber

internal fun ComponentCallbacks.themeHelper() = get<IThemeHelper>()

internal fun ComponentCallbacks.imageLoader() = get<ImageLoader>()

internal fun ComponentCallbacks.analyticsTree() = get<ISupportAnalytics>() as Timber.Tree
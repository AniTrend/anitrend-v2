/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.core.ui

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper

@Deprecated("Use androidx.startup instead to initialize dynamic module features")
interface ILifecycleFeature {

    /**
     * Expects a module helper if one is available for the current scope, otherwise return null
     */
    @Deprecated("Access DynamicFeatureModuleHelper directly in the androidx.startup initializer")
    fun featureModuleHelper(): DynamicFeatureModuleHelper?
}
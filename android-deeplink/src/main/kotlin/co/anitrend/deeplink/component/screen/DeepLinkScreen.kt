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

package co.anitrend.deeplink.component.screen

import android.os.Bundle
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.deeplink.R
import co.anitrend.deeplink.databinding.DeepLinkScreenBinding

class DeepLinkScreen : AniTrendScreen<DeepLinkScreenBinding>() {

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        configurationHelper.themeOverride = R.style.SupportTheme_Translucent
        super.configureActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeepLinkScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {

    }
}
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
package co.anitrend.android.core.ui.theme.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.settings.helper.theme.contract.IThemeHelper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import org.ocpsoft.prettytime.PrettyTime

private val PreviewThemeHelper =
    object : IThemeHelper {
        override val dynamicColor: Boolean = false

        /**
         * Sets the default night mode based on the theme set in settings
         */
        override fun applyDynamicNightModeFromTheme() {}

        /**
         * Applies settings theme resource or provided [themeOverride] which overrides settings
         */
        override fun applyApplicationTheme(
            context: FragmentActivity,
            themeOverride: Int?,
        ) {}
    }

private val PreviewModule =
    module {
        single {
            PrettyTime()
        }
        single {
            AniTrendDateHelper()
        }
    }

class DarkThemeProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Composable
fun PreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeHelper: IThemeHelper = PreviewThemeHelper,
    wrapInSurface: Boolean = false,
    content: @Composable () -> Unit,
) {
    AndroidThreeTen.init(LocalContext.current)
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(modules = PreviewModule)
        }
    }
    AniTrendTheme3(
        darkTheme = darkTheme,
        themeHelper = themeHelper,
        content = {
            if (wrapInSurface) {
                Surface {
                    content()
                }
            } else {
                content()
            }
        },
    )
}

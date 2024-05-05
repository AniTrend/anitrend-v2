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
package co.anitrend.core.android.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

annotation class AniTrendPreview {
    @Preview(
        uiMode = UI_MODE_NIGHT_NO,
        name = "Light",
    )
    annotation class Light

    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        name = "Dark",
    )
    annotation class Dark

    @Preview(
        showSystemUi = true,
        device = "spec:width=411dp,height=891dp",
        name = "Mobile",
    )
    annotation class Mobile

    @Preview(
        showSystemUi = true,
        device = "spec:width=673.5dp,height=841dp,dpi=480",
        name = "Foldable",
    )
    annotation class Foldable

    @Preview(
        showSystemUi = true,
        device = "spec:width=1280dp,height=800dp,dpi=480",
        name = "Tablet",
    )
    annotation class Tablet

    @Preview(
        showSystemUi = true,
        device = "spec:width=1920dp,height=1080dp,dpi=480",
        name = "Desktop",
    )
    annotation class Desktop
}

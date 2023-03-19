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

import co.anitrend.buildSrc.Libraries

plugins {
    id("co.anitrend.plugin")
}

dependencies {
    implementation(project(Libraries.AniTrend.CommonUi.character))
    implementation(project(Libraries.AniTrend.CommonUi.staff))
    implementation(project(Libraries.AniTrend.CommonUi.media))
    implementation(project(Libraries.AniTrend.CommonUi.genre))
    implementation(project(Libraries.AniTrend.CommonUi.tag))

    implementation(project(Libraries.AniTrend.CommonUi.markdown))

    implementation(libs.androidx.browser)
}

android {
    namespace = "co.anitrend.media"
}

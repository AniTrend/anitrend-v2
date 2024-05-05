/*
 * Copyright (C) 2019 AniTrend
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

plugins {
    id("co.anitrend.plugin")
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "co.anitrend.core"
}

dependencies {
    implementation(libs.androidx.browser)

    implementation(libs.anitrend.emojify)
    implementation(libs.anitrend.emojify.contract)
    implementation(libs.anitrend.emojify.kotlinx)


    implementation(libs.square.okhttp.logging)

    implementation(libs.google.flexbox)

    /** Timber Trees */
    implementation(libs.treessence)
}

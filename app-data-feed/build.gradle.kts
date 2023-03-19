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

plugins {
    id("co.anitrend.plugin")
}


dependencies {
    implementation(libs.threeTenBp)
	implementation(libs.square.retrofit.converter.simplexml) {
        exclude("xpp3", "xpp3")
        exclude("stax", "stax-api")
        exclude("stax", "stax")
    }
    // Holding off on using xml util for now, seems not to work and probably needs more testing
    implementation(libs.devrieze.xmlutil.android.serialization)
}

android {
    namespace = "co.anitrend.data.feed"
}

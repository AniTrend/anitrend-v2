import co.anitrend.buildSrc.Libraries

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
    id("kotlinx-serialization")
}

dependencies {
    implementation(Libraries.JetBrains.KotlinX.Serialization.json)

    implementation(Libraries.AniTrend.Sync.plugin)
    implementation(Libraries.CashApp.Copper.copper)
    implementation(Libraries.Dropbox.store)

    implementation(Libraries.retrofitSerializer)

    debugImplementation(Libraries.Chuncker.debug)
    releaseImplementation(Libraries.Chuncker.release)

    androidTestImplementation(Libraries.AndroidX.Room.test)
    androidTestImplementation(Libraries.Square.OkHttp.mockServer)
}

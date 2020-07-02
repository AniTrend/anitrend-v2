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
}

dependencies {

    /** Material Design */
    implementation(Libraries.Google.Material.material)
    implementation(Libraries.Google.Firebase.Analytics.analyticsKtx)
    implementation(Libraries.Google.Firebase.Crashlytics.crashlytics)

    /** Timber Trees */
    implementation(Libraries.treessence)

    /** debugImplementation because LeakCanary should only run in debug builds. */
    debugImplementation(Libraries.Square.LeakCanary.leakCanary)

    /** debugImplementation because debug-db should only run in debug builds */
    debugImplementation(Libraries.debugDb)
}

if (file("google-services.json").exists()) {
    plugins.apply("com.google.gms.google-services")
    plugins.apply("com.google.firebase.crashlytics")
}


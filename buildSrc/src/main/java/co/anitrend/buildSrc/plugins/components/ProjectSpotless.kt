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

package co.anitrend.buildSrc.plugins.components

import co.anitrend.buildSrc.extensions.libs
import co.anitrend.buildSrc.extensions.spotlessExtension
import org.gradle.api.Project
import java.io.File

internal fun Project.configureSpotless(): Unit = spotlessExtension().run {
    val withLicenseHeader: (String) -> File = { extension ->
        rootProject.file("spotless/copyright$extension")
    }
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint(libs.versions.ktlint.get())
        licenseHeaderFile(
            withLicenseHeader(".kt")
        )
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        licenseHeaderFile(withLicenseHeader(".kts"), "(^(?![\\/ ]\\*).*$)")
    }
    format("xml") {
        target("**/*.xml")
        targetExclude("**/build/**/*.xml")
        licenseHeaderFile(withLicenseHeader(".xml"), "(<[^!?])")
    }
    format("properties") {
        target("**/*.properties")
        targetExclude("**/build/**/*.properties")
        licenseHeaderFile(withLicenseHeader(".properties"), "(^(#).*\$)")
    }
}

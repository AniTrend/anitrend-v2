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
    val buildDirectory = layout.buildDirectory.get()
    kotlin {
        target("**/*.kt")
        targetExclude(
            "${buildDirectory}/**/*.kt",
            "**/src/test/**/*.kt",
            "**/src/**/com/kyant/**/*.kt",
            "**/src/**/io/material/**/*.kt",
        )
        ktlint(libs.versions.ktlint.get()).setEditorConfigPath(
            rootProject.file(".editorconfig")
        )
        licenseHeaderFile(
            withLicenseHeader(".kt")
        )
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:property-naming"
        }
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("${buildDirectory}/**/*.kts")
        licenseHeaderFile(withLicenseHeader(".kts"), "(^(?![\\/ ]\\*).*$)")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("xml") {
        target("**/*.xml")
        targetExclude("${buildDirectory}/**/*.xml")
        licenseHeaderFile(withLicenseHeader(".xml"), "^(<\\?xml.*\\?>\\s*)?(<.*)")
    }
}

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

package co.anitrend.buildSrc.resolver

import co.anitrend.buildSrc.extensions.libs
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

fun Configuration.handleConflicts(project: Project): Unit = with(project) {
    resolutionStrategy.eachDependency {
        when (requested.group) {
            "org.jetbrains.kotlin" -> {
                when (requested.name) {
                    "kotlin-reflect",
                    "kotlin-stdlib",
                    "kotlin-stdlib-common",
                    "kotlin-stdlib-jdk8",
                    "kotlin-stdlib-jdk7" -> useVersion(libs.versions.jetbrains.kotlin.get())
                }
            }
        }
        if (requested.name == "kotlinx-serialization-json") {
            useTarget(libs.jetbrains.kotlinx.serialization.json)
        }
        if (requested.group == "com.google.android.material") {
            useTarget(libs.google.android.material)
        }
        if (requested.group == "com.jakewharton.timber") {
            useTarget(libs.timber)
        }
        if (requested.group == "androidx.startup") {
            useTarget(libs.androidx.startup.runtime)
        }
    }
}

fun ComponentSelectionWithCurrent.handleDependencySelection() {
    val reject = listOf("preview", "m", "rc", "alpha", "beta")
        .map { qualifier ->
            val pattern = "(?i).*[.-]$qualifier[.\\d-]*"
            Regex(pattern, RegexOption.IGNORE_CASE)
        }
        .any { it.matches(candidate.version) }
    if (reject)
        reject("$candidate version does not fit acceptance criteria")
}
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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("co.anitrend.plugin")
}

tasks.withType(KotlinCompilationTask::class.java) {
    compilerOptions {
        optIn.add("kotlinx.coroutines.FlowPreview")
        optIn.add("coil.annotation.ExperimentalCoilApi")
        optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

dependencies {
    implementation(libs.anitrend.arch.recycler)
    implementation(libs.google.flexbox)
    implementation(libs.prettyTime)
    compileOnly(libs.pintrest.ktlint.rule.engine)
    compileOnly(libs.pintrest.ktlint.ruleset.core)
}

android {
    buildFeatures.buildConfig = true
    namespace = "co.anitrend.android.core"
}

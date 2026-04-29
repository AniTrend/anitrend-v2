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

import co.anitrend.buildSrc.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import kotlin.jvm.java

plugins {
    id("co.anitrend.plugin")
    id("kotlinx-serialization")
}

tasks.withType(KotlinCompilationTask::class.java) {
    compilerOptions {
        optIn.add("kotlinx.serialization.ExperimentalSerializationApi")
        optIn.add("kotlin.RequiresOptIn")
        optIn.add("androidx.paging.ExperimentalPagingApi")
    }
}

dependencies {
    implementation(project(Libraries.AniTrend.Data.edge))

    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.room.paging)

    implementation(libs.anitrend.sync) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
    }
    implementation(libs.cash.copper)

    implementation(libs.anitrend.emojify)
    implementation(libs.anitrend.emojify.contract)

    implementation(libs.anitrend.querybuilder.annotation)
    implementation(libs.anitrend.querybuilder.core)
    implementation(libs.anitrend.querybuilder.ext)
    ksp(libs.anitrend.querybuilder.processor)
}

android {
    buildFeatures.buildConfig = true
    namespace = "co.anitrend.data"
}

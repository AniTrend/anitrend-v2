/*
 * Copyright (C) 2020  AniTrend
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
    `kotlin-dsl`
    `version-catalog`
}

repositories {
    google()
    mavenCentral()
    maven {
        setUrl("https://www.jitpack.io")
    }
    maven {
        setUrl("https://oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}


dependencies {
    /** Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation(libs.android.gradle.plugin)

    /** Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation(libs.jetbrains.kotlin.gradle)

    /** Depend on the dokka plugin, since we want to access it in our plugin */
    implementation(libs.jetbrains.dokka.gradle)

    /** Dependency management */
    implementation(libs.gradle.versions)

    /** Spotless */
    implementation(libs.spotless.gradle)

    /** Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())

    /** Work around to include ../.gradle/LibrariesForLibs generated file for version catalog */
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

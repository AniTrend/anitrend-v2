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

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

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

tasks.withType(KotlinJvmCompile::class.java) {
    kotlinOptions {
        jvmTarget = "11"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

fun Project.library(alias: String) =
    extensions.getByType<VersionCatalogsExtension>()
        .named("libs")
        .findLibrary(alias)
        .get()

dependencies {
    /** Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation(library("android-gradle-plugin"))

    /** Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation(library("jetbrains-kotlin-gradle"))

    /** Depend on the dokka plugin, since we want to access it in our plugin */
    implementation(library("jetbrains-dokka-gradle"))

    /** Dependency management */
    implementation(library("gradle-versions"))

    /** Spotless */
    implementation(library("spotless-gradle"))

    /** Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}

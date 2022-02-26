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

val buildToolsVersion = "7.1.1"
val kotlinVersion = "1.5.31"
val dokkaVersion = "1.5.31"
val manesVersion = "0.36.0"
val spotlessVersion = "6.3.0"

dependencies {
    /** Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation("com.android.tools.build:gradle:$buildToolsVersion")

    /** Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

    /* Depend on the dokka plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")

    /** Dependency management */
    implementation("com.github.ben-manes:gradle-versions-plugin:$manesVersion")

    /** Spotless */
    implementation("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}

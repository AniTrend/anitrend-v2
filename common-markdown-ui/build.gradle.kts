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

import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    id("co.anitrend.plugin")
}

tasks.withType(KotlinCompile::class.java) {
	kotlinOptions {
		freeCompilerArgs = listOf("-opt-in=coil.annotation.ExperimentalCoilApi")
	}
}

dependencies {
	implementation(libs.saket.betterlinkmovementmethod)
	
	implementation(libs.markwon.core)
	implementation(libs.markwon.html)
	implementation(libs.markwon.coil)
}
android {
    namespace = "co.anitrend.common.markdown"
}

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

package co.anitrend.buildSrc.extensions

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

/**
 * Run cli commands on [Project.getRootProject] and returns the output in string format
 *
 * @param command The CLI command to execute
 *
 * @return [String]
 */
fun Project.runCommand(command: String): String {
    val outputStream = ByteArrayOutputStream()
    rootProject.exec {
        commandLine(command.split(' '))
        standardOutput = outputStream
    }
    return String(outputStream.toByteArray()).trim()
}

val Project.gitSha: String
    get() = runCommand("git rev-parse --short=8 HEAD")

val Project.gitCommitCount: String
    get() = runCommand("git rev-list count HEAD")

val Project.gitBranch: String
    get() = runCommand("git rev-parse --abbrev-ref HEAD")
/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.medialist.editor.component.compose.util

import co.anitrend.domain.medialist.enums.ScoreFormat

/**
 * Filters score input text according to [ScoreFormat].
 * - POINT_10_DECIMAL: allow digits and a single decimal separator, cap to 2 integer and 1 fractional digit.
 * - Others: digits only.
 */
fun filterScoreInput(
    input: String,
    scoreFormat: ScoreFormat,
): String =
    when (scoreFormat) {
        ScoreFormat.POINT_10_DECIMAL -> filterDecimalInputOnePlace(input, maxIntegerDigits = 2)
        else -> input.substringBefore('.').filter { it.isDigit() }
    }

/**
 * Generic decimal input filter that:
 * - keeps only digits and at most one '.'
 * - optionally limits integer part length
 * - limits fractional part to 1 digit
 */
fun filterDecimalInputOnePlace(
    input: String,
    maxIntegerDigits: Int? = null,
): String {
    val sanitized =
        input
            .replace(Regex("[^0-9.]"), "")
            .trimStart('.') // avoid leading dot in output
    val singleDot =
        sanitized
            .replaceFirst("\\.".toRegex(), "#")
            .replace(".", "")
            .replace("#", ".")

    if (!singleDot.contains('.')) {
        return if (maxIntegerDigits != null) singleDot.take(maxIntegerDigits + 1) else singleDot
    }

    val parts = singleDot.split('.')
    val integer = if (maxIntegerDigits != null) parts.first().take(maxIntegerDigits) else parts.first()
    val fractional = parts.getOrNull(1)?.take(1) ?: ""
    return if (fractional.isEmpty()) integer else "$integer.$fractional"
}

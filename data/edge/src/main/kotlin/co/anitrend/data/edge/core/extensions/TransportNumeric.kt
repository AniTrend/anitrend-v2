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
package co.anitrend.data.edge.core.extensions

/**
 * Central numeric adaptation point for Edge transport payloads.
 *
 * The Edge schema exposes most numeric fields as float-like scalars
 * (for example `NonNegativeFloat`, `PositiveFloat`, or untyped doubles), so the
 * generated response models carry them as [Double]. Local persistence types are
 * integral ([Int], [Long]). These helpers adapt values at converter boundaries
 * only and reject non-integral or out-of-range values instead of silently
 * truncating or saturating them, so a schema drift that starts emitting
 * fractional ids or counts fails loudly during mapping rather than corrupting
 * persisted rows.
 *
 * @param description Human readable name of the adapted field, used in error
 * messages so failures point at the offending transport field.
 */
internal fun Double?.requireIntegralLong(description: String): Long? = this?.requireIntegralLong(description)

/**
 * @see requireIntegralLong
 */
internal fun Double.requireIntegralLong(description: String): Long {
    if (isNaN() || isInfinite() || this % 1.0 != 0.0) {
        throw IllegalArgumentException(
            "Edge transport value for $description was not integral: $this",
        )
    }
    // Long.MIN_VALUE is exactly representable as a Double (-2^63). The upper
    // bound is exclusive 2^63: Long.MAX_VALUE.toDouble() rounds up to 2^63, so
    // comparing against it would admit out-of-range values that toLong() then
    // saturates instead of converting exactly.
    if (this < Long.MIN_VALUE.toDouble() || this >= -Long.MIN_VALUE.toDouble()) {
        throw IllegalArgumentException(
            "Edge transport value for $description was outside the Long range: $this",
        )
    }
    return toLong()
}

/**
 * @see requireIntegralLong
 */
internal fun Double?.requireIntegralInt(description: String): Int? = this?.requireIntegralInt(description)

/**
 * @see requireIntegralLong
 */
internal fun Double.requireIntegralInt(description: String): Int {
    if (isNaN() || isInfinite() || this % 1.0 != 0.0) {
        throw IllegalArgumentException(
            "Edge transport value for $description was not integral: $this",
        )
    }
    // Both Int bounds are exactly representable as Doubles; the upper bound is
    // exclusive 2^31 (-Int.MIN_VALUE.toDouble()).
    if (this < Int.MIN_VALUE.toDouble() || this >= -Int.MIN_VALUE.toDouble()) {
        throw IllegalArgumentException(
            "Edge transport value for $description was outside the Int range: $this",
        )
    }
    return toInt()
}

/**
 * Explicit, documented timestamp adaptation for Edge transport payloads.
 *
 * Edge emits epoch timestamps (seconds since the Unix epoch) as numeric scalars
 * which codegen represents as [Double]. Local persistence stores epoch seconds
 * as [Long]. Fractional values are rejected through [requireIntegralLong] rather
 * than silently truncated, matching the integral epoch-second contract of the
 * Edge transport.
 *
 * @param description Human readable name of the adapted field, used in error
 * messages so failures point at the offending transport field.
 */
internal fun Double?.asEpochSeconds(description: String): Long? = this?.asEpochSeconds(description)

/**
 * @see asEpochSeconds
 */
internal fun Double.asEpochSeconds(description: String): Long = requireIntegralLong("$description (epoch seconds)")

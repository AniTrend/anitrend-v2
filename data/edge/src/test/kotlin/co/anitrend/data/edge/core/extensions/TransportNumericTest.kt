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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TransportNumericTest {
    @Test
    fun `requireIntegralLong accepts integral in-range values`() {
        assertEquals(0L, 0.0.requireIntegralLong("field"))
        assertEquals(42L, 42.0.requireIntegralLong("field"))
        assertEquals(-7L, (-7.0).requireIntegralLong("field"))
        assertEquals(Long.MIN_VALUE, Long.MIN_VALUE.toDouble().requireIntegralLong("field"))
        // Largest Double strictly below 2^63.
        assertEquals(9223372036854774784L, 9223372036854774784.0.requireIntegralLong("field"))
    }

    @Test
    fun `requireIntegralLong rejects fractional values`() {
        assertFailsWith<IllegalArgumentException> {
            42.5.requireIntegralLong("field")
        }
    }

    @Test
    fun `requireIntegralLong rejects NaN and infinity`() {
        assertFailsWith<IllegalArgumentException> {
            Double.NaN.requireIntegralLong("field")
        }
        assertFailsWith<IllegalArgumentException> {
            Double.POSITIVE_INFINITY.requireIntegralLong("field")
        }
        assertFailsWith<IllegalArgumentException> {
            Double.NEGATIVE_INFINITY.requireIntegralLong("field")
        }
    }

    @Test
    fun `requireIntegralLong rejects values above the Long range`() {
        // Long.MAX_VALUE.toDouble() rounds up to 2^63 and must be rejected
        // instead of saturating to Long.MAX_VALUE.
        assertFailsWith<IllegalArgumentException> {
            Long.MAX_VALUE.toDouble().requireIntegralLong("field")
        }
        // Exactly 2^63.
        assertFailsWith<IllegalArgumentException> {
            9223372036854775808.0.requireIntegralLong("field")
        }
        assertFailsWith<IllegalArgumentException> {
            1.0e300.requireIntegralLong("field")
        }
    }

    @Test
    fun `requireIntegralLong rejects values below the Long range`() {
        assertFailsWith<IllegalArgumentException> {
            Long.MIN_VALUE.toDouble().minus(2048.0).requireIntegralLong("field")
        }
        assertFailsWith<IllegalArgumentException> {
            (-1.0e300).requireIntegralLong("field")
        }
    }

    @Test
    fun `nullable requireIntegralLong passes null through`() {
        assertNull((null as Double?).requireIntegralLong("field"))
        assertEquals(42L, 42.0.requireIntegralLong("field"))
    }

    @Test
    fun `requireIntegralInt accepts integral in-range values`() {
        assertEquals(0, 0.0.requireIntegralInt("field"))
        assertEquals(42, 42.0.requireIntegralInt("field"))
        assertEquals(-7, (-7.0).requireIntegralInt("field"))
        assertEquals(Int.MIN_VALUE, Int.MIN_VALUE.toDouble().requireIntegralInt("field"))
        assertEquals(Int.MAX_VALUE, Int.MAX_VALUE.toDouble().requireIntegralInt("field"))
    }

    @Test
    fun `requireIntegralInt rejects fractional values`() {
        assertFailsWith<IllegalArgumentException> {
            42.5.requireIntegralInt("field")
        }
    }

    @Test
    fun `requireIntegralInt rejects NaN and infinity`() {
        assertFailsWith<IllegalArgumentException> {
            Double.NaN.requireIntegralInt("field")
        }
        assertFailsWith<IllegalArgumentException> {
            Double.POSITIVE_INFINITY.requireIntegralInt("field")
        }
    }

    @Test
    fun `requireIntegralInt rejects values above the Int range`() {
        // Exactly 2^31, the first out-of-range value.
        assertFailsWith<IllegalArgumentException> {
            2147483648.0.requireIntegralInt("field")
        }
        assertFailsWith<IllegalArgumentException> {
            1.0e10.requireIntegralInt("field")
        }
    }

    @Test
    fun `requireIntegralInt rejects values below the Int range`() {
        assertFailsWith<IllegalArgumentException> {
            (-2147483649.0).requireIntegralInt("field")
        }
        assertFailsWith<IllegalArgumentException> {
            (-1.0e10).requireIntegralInt("field")
        }
    }

    @Test
    fun `nullable requireIntegralInt passes null through`() {
        assertNull((null as Double?).requireIntegralInt("field"))
        assertEquals(42, 42.0.requireIntegralInt("field"))
    }

    @Test
    fun `asEpochSeconds converts integral values and rejects fractional and out-of-range values`() {
        assertEquals(1710000000L, 1710000000.0.asEpochSeconds("field"))
        assertEquals(-1L, (-1.0).asEpochSeconds("field"))
        assertNull((null as Double?).asEpochSeconds("field"))
        assertFailsWith<IllegalArgumentException> {
            1710000000.5.asEpochSeconds("field")
        }
        assertFailsWith<IllegalArgumentException> {
            1.0e300.asEpochSeconds("field")
        }
    }
}

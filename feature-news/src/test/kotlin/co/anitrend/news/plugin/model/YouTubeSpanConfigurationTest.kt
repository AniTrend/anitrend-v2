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

package co.anitrend.news.plugin.model

import io.mockk.mockk
import io.noties.markwon.MarkwonConfiguration
import org.junit.Assert.*

import junit.framework.TestCase

class YouTubeSpanConfigurationTest : TestCase() {

    private lateinit var spanConfiguration: YouTubeSpanConfiguration

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    override fun setUp() {
        super.setUp()
        spanConfiguration = YouTubeSpanConfiguration(
            configuration = mockk(),
            renderProps = mockk(),
            tag = mockk(),
            magnificationScale = 2f,
            sizeMeasurementUnit = mockk()
        )
    }

    fun `test standard youtube url`() {
        val expected = "31HfP81oWDI"
        val input = "https://www.youtube.com/watch?v=31HfP81oWDI"
        val actual = spanConfiguration.getVideoId(input)

        assertEquals(expected, actual)
    }

    fun `test shortened youtube url`() {
        val expected = "31HfP81oWDI"
        val input = "https://youtu.be/31HfP81oWDI"
        val actual = spanConfiguration.getVideoId(input)

        assertEquals(expected, actual)
    }
}
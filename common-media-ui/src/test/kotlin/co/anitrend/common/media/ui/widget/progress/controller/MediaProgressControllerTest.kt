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

package co.anitrend.common.media.ui.widget.progress.controller

import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MediaProgressControllerTest {

    private val media = mockk<Media>()
    private val settings = mockk<IAuthenticationSettings>()
    private lateinit var controller: MediaProgressController

    @Before
    fun setUp() {
        every { media.mediaList } returns MediaList.Core.empty().copy(
            progress = MediaListProgress.Anime(
                5,
                0
            ),
            score = 5.8f,
            userId = 15
        )

        every { media.status } returns MediaStatus.RELEASING

        every { media.category } returns Media.Category.Anime.empty().copy(
            25,
            23
        )

        every { settings.authenticatedUserId.value } returns 15
        every { settings.isAuthenticated.value } returns true
        controller = MediaProgressController(media, settings)
    }

    @Test
    fun shouldHideWidget() {
        val expected = false
        val actual = controller.shouldHideWidget()

        assertEquals(expected, actual)
    }

    @Test
    fun hasCaughtUp() {
        val expected = false
        val actual = controller.hasCaughtUp()

        assertEquals(expected, actual)
    }

    @Test
    fun isIncrementPossible() {
        val expected = true
        val actual = controller.isIncrementPossible()

        assertEquals(expected, actual)
    }

    @Test
    fun getMaximumProgress() {
        val expected = 25
        val actual = controller.getMaximumProgress()

        assertEquals(expected, actual)
    }

    @Test
    fun getCurrentProgress() {
        val expected = 5
        val actual = controller.getCurrentProgress()

        assertEquals(expected, actual)
    }
}
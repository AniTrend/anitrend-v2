/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.data.user.cache

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.android.cache.helper.instantInFuture
import co.anitrend.data.core.CoreTestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.inject
import org.threeten.bp.Instant
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class UserCacheTest : CoreTestSuite() {

    private val viewCache by inject<UserCache.Viewer>()
    private val identity = UserCache.Viewer.Identity(100)
    private val startTime = Instant.ofEpochSecond(1649357078L)

    @Before
    fun setUp() {
        runBlocking(dispatchers.io) {
            viewCache.updateLastRequest(identity, startTime)
        }
    }

    fun testUserViewCache() = runBlocking(dispatchers.io) {
        assertTrue(viewCache.hasBeenRequested(identity))
        assertTrue(viewCache.shouldRefresh(identity, instantInFuture(minutes = 6)))
    }
}

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

package co.anitrend.data.news.datasource.remote

import android.net.Uri
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.cash.copper.flow.mapToList
import app.cash.copper.flow.observeQuery
import app.cash.turbine.test
import co.anitrend.data.core.CoreTestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@RunWith(AndroidJUnit4ClassRunner::class)
internal class NewsRemoteSourceTest : CoreTestSuite() {

    @Test
    @ExperimentalTime
    fun testNewsContentProvider() = runBlocking(dispatchers.io) {
        val query = Uri.Builder()
            .scheme("content")
            .authority("co.anitrend.crunchy")
            .appendPath("provider")
            .appendPath("news")
            .appendQueryParameter("page", "1")
            .appendQueryParameter("perPage", "15")
            .build()

        val newsFlow = contentResolver
            .observeQuery(query)
            .mapToList { cursor ->
                """
                    News(
                    id = ${cursor.getString(0).hashCode().toLong()},
                    guid = ${cursor.getString(0)},
                    title = ${cursor.getString(1)},
                    image = ${cursor.getString(2)},
                    author = ${cursor.getString(3)},
                    subTitle = ${cursor.getString(4)},
                    description = ${cursor.getString(5)},
                    content = ${cursor.getString(6)},
                    publishedOn = ${cursor.getLong(7)}
                )
                """.trimIndent()
            }

        newsFlow.test(timeout = 15.seconds) {
            val news = expectItem()
            Assert.assertTrue(news.isNotEmpty())
            Assert.assertEquals(15, news.size)
        }
    }
}
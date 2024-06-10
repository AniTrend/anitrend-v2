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

package co.anitrend.data.core

import android.content.ContentResolver
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.initializeKoin
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.KoinApplication
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.InputStreamReader
import kotlin.test.assertNotNull

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */
internal open class CoreTestSuite : KoinTest {

    private val source = CoreTestSuite::class.java
    private val koin: KoinApplication by lazy {
        val context = InstrumentationRegistry.getInstrumentation().context
        initializeKoin(context)
    }

    protected val json by inject<Json>()
    protected val server by inject<MockWebServer>()
    protected val store by inject<IAniTrendStore>()
    protected val dispatchers by inject<ISupportDispatcher>()
    protected val contentResolver by inject<ContentResolver>()

    @Before
    fun startUp() {
        // runs for every test
        runCatching { koin }
    }

    protected inline fun <reified T> String.load(deserializer: DeserializationStrategy<T?>): T? {
        val resource = source.getResourceAsStream("templates/${this}")
        assertNotNull(resource)
        val result: T? = InputStreamReader(resource).use {
            json.decodeFromString(deserializer, it.readText())
        }
        assertNotNull(result)
        return result
    }

    protected inline fun <reified T> String.load(): T? {
        val resource = source.getResourceAsStream("templates/${this}")
        assertNotNull(resource)
        val result: T? = InputStreamReader(resource).use {
            json.decodeFromString(it.readText())
        }
        assertNotNull(result)
        return result
    }

    @After
    open fun shutdown() {
        // (store as AniTrendStore).close()
    }
}

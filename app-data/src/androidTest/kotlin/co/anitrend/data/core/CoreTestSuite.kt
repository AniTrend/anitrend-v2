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

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.initializeKoin
import com.google.gson.GsonBuilder
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.koin.core.KoinApplication
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.InputStreamReader
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */
internal open class CoreTestSuite : KoinTest {

    private val source = CoreTestSuite::class.java

    protected val store by inject<IAniTrendStore>()
    protected val dispatchers by inject<SupportDispatchers>()

    protected val koin: KoinApplication by lazy { initializeKoin() }

    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    @Before
    fun startUp() {
        // runs for every test
        runCatching { koin }
    }

    protected inline fun <reified T> String.load(): T? {
        val resource = source.getResourceAsStream("templates/${this}")
        assertNotNull(resource)
        val inputStreamReader = InputStreamReader(resource)
        val superType = typeOf<T>().javaType
        val result: T? = gson.fromJson(inputStreamReader, superType)
        assertNotNull(result)
        return result
    }

    @After
    open fun shutdown() {
        // (store as AniTrendStore).close()
    }
}
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

import android.content.Context
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.arch.database.AniTrendStore
import com.google.gson.GsonBuilder
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import java.net.URI
import kotlin.reflect.typeOf

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */
internal open class CoreTestSuite {

    protected val context: Context by lazy {
        InstrumentationRegistry.getInstrumentation().context
    }

    protected val store: AniTrendStore by lazy {
        Room.inMemoryDatabaseBuilder(
            context,
            AniTrendStore::class.java
        ).build()
    }

    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    @OptIn(ExperimentalStdlibApi::class)
    protected inline fun <reified T> String.load(): T? {
        val resource = javaClass.getResourceAsStream(this)?.bufferedReader()
        val superType = typeOf<T>().javaClass
        return gson.fromJson<T>(resource, superType)
    }

    /**
     * Should invoke `store.close()` if applicable
     */
    @After
    open fun shutdown() {
        store.close()
    }
}
/*
 * Copyright (C) 2026 AniTrend
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

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.android.database.AniTrendStore
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.testModules
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertSame

/**
 * Test harness with a fresh per-test [AniTrendStore] bound over the production
 * [IAniTrendStore] graph, unlike [CoreTestSuite] which shares one process-wide store.
 *
 * Global Koin state is started and stopped per test, so suites using this harness
 * must run sequentially and never in parallel with other Koin consumers.
 */
internal open class IsolatedSidecarTestSuite : KoinTest {
    protected lateinit var database: AniTrendStore

    @Before
    fun startIsolatedGraph() {
        runCatching { stopKoin() }
        database =
            Room
                .inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    AniTrendStore::class.java,
                ).build()
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            allowOverride(true)
            modules(testModules)
            modules(
                module {
                    // primary key must be IAniTrendStore: production binds
                    // singleOf(AniTrendStore::create) whose return type wins that key,
                    // and IAniTrendStore.BINDINGS intentionally excludes it
                    single<IAniTrendStore> { database } binds IAniTrendStore.BINDINGS
                    single<AniTrendStore> { database }
                },
            )
        }
        // the isolated store must win over the production store binding
        assertSame(database, get<IAniTrendStore>())
    }

    @After
    fun stopIsolatedGraph() {
        runCatching { stopKoin() }
        database.close()
    }
}

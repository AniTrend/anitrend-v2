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
package co.anitrend.data.edge.navigation.datasource

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.android.database.AniTrendStore
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.entity.EdgeConfigImageEntity
import co.anitrend.data.edge.config.entity.EdgeConfigSettingsEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationGroupEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class EdgeNavigationLocalSourceTest {
    private lateinit var database: AniTrendStore

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    AniTrendStore::class.java,
                ).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun replaceByConfigIdUsesStableKeysAndLatestSnapshot() =
        runTest {
            val navigationDao = database.edgeNavigationDao()
            navigationDao.clear()
            database.edgeConfigDao().upsert(configEntity())

            navigationDao.replaceByConfigId(
                configId = EdgeConfigEntity.DEFAULT_ID,
                entities =
                    listOf(
                        navigation(key = "home", destination = "home-old"),
                        navigation(key = "home", destination = "home-current"),
                        navigation(key = "discover", destination = "discover"),
                    ),
            )

            val storedNavigation =
                checkNotNull(
                    database
                        .edgeConfigDao()
                        .edgeConfigById(EdgeConfigEntity.DEFAULT_ID)
                        .first(),
                ).navigation
            assertEquals(2, storedNavigation.size)
            assertEquals(
                "home-current",
                storedNavigation.single { entity -> entity.key == "home" }.destination,
            )
            assertEquals(setOf("home", "discover"), storedNavigation.map(EdgeNavigationEntity::key).toSet())

            navigationDao.replaceByConfigId(
                configId = EdgeConfigEntity.DEFAULT_ID,
                entities = listOf(navigation(key = "airing", destination = "airing")),
            )

            val updatedNavigation =
                checkNotNull(
                    database
                        .edgeConfigDao()
                        .edgeConfigById(EdgeConfigEntity.DEFAULT_ID)
                        .first(),
                ).navigation
            assertEquals(listOf("airing"), updatedNavigation.map(EdgeNavigationEntity::key))
            assertEquals(1, navigationDao.count())
            navigationDao.clear()
        }

    private fun configEntity() =
        EdgeConfigEntity(
            settings =
                EdgeConfigSettingsEntity(
                    analyticsEnabled = false,
                    platformSource = null,
                ),
            image =
                EdgeConfigImageEntity(
                    banner = "banner",
                    poster = "poster",
                    loading = "loading",
                    error = "error",
                    info = "info",
                    standard = "standard",
                ),
        )

    private fun navigation(
        key: String,
        destination: String,
    ) = EdgeNavigationEntity(
        configId = EdgeConfigEntity.DEFAULT_ID,
        criteria = key,
        destination = destination,
        group =
            EdgeNavigationGroupEntity(
                authenticated = false,
                i18n = "navigation.group.guest",
            ),
        i18n = "navigation.$key",
        icon = "ic_$key",
        key = key,
    )
}

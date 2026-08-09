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
package co.anitrend.data.edge.config.converters

import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.genre.converters.EdgeGenreModelConverter
import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.data.edge.navigation.converters.EdgeNavigationModelConverter
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EdgeConfigModelConverterTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

    private val configPayload =
        """
        {
          "config": {
            "settings": {
              "analyticsEnabled": true,
              "platformSource": "anitrend"
            },
            "image": {
              "banner": "banner.jpg",
              "default": "default.jpg",
              "error": "error.jpg",
              "info": "info.jpg",
              "loading": "loading.jpg",
              "poster": "poster.jpg"
            },
            "genres": [
              {
                "mediaId": 100,
                "name": "Action"
              },
              {
                "mediaId": 200,
                "name": "Comedy"
              }
            ],
            "navigation": [
              {
                "criteria": "media:airing",
                "destination": "media/airing",
                "group": {
                  "authenticated": false,
                  "i18n": "navigation.group.guest"
                },
                "icon": "ic_airing",
                "i18n": "navigation.airing",
                "key": "airing"
              },
              {
                "criteria": "profile:list",
                "destination": "profile/list",
                "group": {
                  "authenticated": true,
                  "i18n": "navigation.group.user"
                },
                "icon": "ic_list",
                "i18n": "navigation.list",
                "key": "list"
              }
            ]
          }
        }
        """.trimIndent()

    @Test
    fun `converter maps generated config payload into config entity`() {
        val result = json.decodeFromString<GetConfigData>(configPayload)

        val entity = EdgeConfigModelConverter().convertFrom(result)

        assertEquals(true, entity.settings.analyticsEnabled)
        assertEquals("anitrend", entity.settings.platformSource)
        assertEquals("banner.jpg", entity.image.banner)
        assertEquals("default.jpg", entity.image.standard)
        assertEquals("poster.jpg", entity.image.poster)
    }

    @Test
    fun `genre converter adapts integral media ids and rejects fractional ones`() {
        val result = json.decodeFromString<GetConfigData>(configPayload)
        val genres = result.config?.genres.orEmpty().mapNotNull { it }

        assertEquals(2, genres.size)
        val first = EdgeGenreModelConverter().convertFrom(genres.first())
        assertEquals(100L, first.id)
        assertEquals("Action", first.name)
        assertEquals(EdgeConfigEntity.DEFAULT_ID, first.configId)

        val fractional = json.decodeFromString<GetConfigData.ConfigGenres>("""{"mediaId": 100.5, "name": "Action"}""")
        assertFailsWith<IllegalArgumentException> {
            EdgeGenreModelConverter().convertFrom(fractional)
        }
    }

    @Test
    fun `navigation converter ignores generated key at the persistence boundary`() {
        val result = json.decodeFromString<GetConfigData>(configPayload)
        val navigation = result.config?.navigation.orEmpty().mapNotNull { it }

        assertEquals(2, navigation.size)
        val first = EdgeNavigationModelConverter().convertFrom(navigation.first())
        assertEquals("media:airing", first.criteria)
        assertEquals("media/airing", first.destination)
        assertEquals("ic_airing", first.icon)
        assertEquals("navigation.airing", first.i18n)
        assertEquals(false, first.group.authenticated)
        assertEquals("navigation.group.guest", first.group.i18n)
        assertEquals(EdgeConfigEntity.DEFAULT_ID, first.configId)

        val second = EdgeNavigationModelConverter().convertFrom(navigation[1])
        assertEquals(true, second.group.authenticated)
    }

    @Test
    fun `converter rejects payloads without a config root`() {
        val result = json.decodeFromString<GetConfigData>("""{"config": null}""")

        assertNull(result.config)
        assertFailsWith<IllegalStateException> {
            EdgeConfigModelConverter().convertFrom(result)
        }
    }
}

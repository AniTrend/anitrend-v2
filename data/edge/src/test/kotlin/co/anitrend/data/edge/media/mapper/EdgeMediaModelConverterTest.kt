package co.anitrend.data.edge.media.mapper

import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class EdgeMediaModelConverterTest {

    private val converter = EdgeMediaModelConverter()

    @Test
    fun `maps full media payload preferring aniList id`() {
        val media = EdgeMediaModel.Media(
            id = "9999",
            title = EdgeMediaModel.SeriesTitleType(
                english = "English Title",
                romaji = "Romaji Title",
                native = "Native Title",
            ),
            cover = EdgeMediaModel.SeriesCoverImageType(
                extraLarge = "https://img/xl.jpg",
                large = "https://img/l.jpg",
                medium = "https://img/m.jpg",
                color = "#112233",
            ),
            banner = "https://img/banner.jpg",
            description = "Some description",
            fanart = "https://img/fanart.jpg",
            format = "TV",
            source = "ORIGINAL",
            status = "RELEASING",
            ageRating = "PG-13",
            isAdult = false,
            mediaId = EdgeMediaModel.SeriesIdType(
                aniList = 321,
                myAnimeList = 654,
                notify = "notif-1",
                trakt = 777,
                tvdb = 888,
                tmdb = 999,
            ),
            image = EdgeMediaModel.SeriesImageType(),
            schedule = EdgeMediaModel.SeriesScheduleType(),
            seasons = listOf(
                EdgeMediaModel.SeriesSeasonType(name = "SPRING", year = 2024)
            ),
            networks = emptyList(),
            themeSongs = emptyList(),
            trailers = emptyList(),
            updatedAt = 1700000000L,
        )

        val entity: EdgeMediaEntity = converter.convertFrom(media)

        // id preference and basic scalars
        assertEquals(321, entity.id)
        assertEquals("TV", entity.format)
        assertEquals("RELEASING", entity.status)
        assertEquals("https://img/banner.jpg", entity.bannerImage)
        assertEquals("Some description", entity.description)
        assertEquals("https://img/fanart.jpg", entity.fanart)

        // titles
        assertEquals("Romaji Title", entity.title.romaji)
        assertEquals("English Title", entity.title.english)
        assertEquals("Native Title", entity.title.native)

        // cover
        assertEquals("https://img/m.jpg", entity.cover.medium)
        assertEquals("https://img/l.jpg", entity.cover.large)
        assertEquals("https://img/xl.jpg", entity.cover.extraLarge)
        assertEquals("#112233", entity.cover.color)

        // season snapshot
        assertEquals("SPRING", entity.season)
        assertEquals(2024, entity.seasonYear)

        // ratings/flags
        assertEquals("PG-13", entity.ageRating)
        assertFalse(entity.isAdult ?: true)

        // external ids
        assertEquals(321, entity.externalIds.aniList)
        assertEquals(654, entity.externalIds.myAnimeList)
        assertEquals("notif-1", entity.externalIds.notify)
        assertEquals(777, entity.externalIds.trakt)
        assertEquals(888, entity.externalIds.tvdb)
        assertEquals(999, entity.externalIds.tmdb)

        // audit
        assertEquals(1700000000L, entity.updatedAt)
    }

    @Test
    fun `falls back to parsed string id when aniList id absent`() {
        val media = EdgeMediaModel.Media(
            id = "12345",
            title = null,
            cover = null,
            banner = null,
            description = null,
            fanart = null,
            format = null,
            source = null,
            status = null,
            ageRating = null,
            isAdult = null,
            mediaId = null,
            image = null,
            schedule = null,
            seasons = null,
            networks = null,
            themeSongs = null,
            trailers = null,
            updatedAt = null,
        )

        val entity = converter.convertFrom(media)
        assertEquals(12345, entity.id)

        // nullable fields remain null
        assertNull(entity.title.romaji)
        assertNull(entity.cover.medium)
        assertNull(entity.ageRating)
        assertNull(entity.updatedAt)
    }
}

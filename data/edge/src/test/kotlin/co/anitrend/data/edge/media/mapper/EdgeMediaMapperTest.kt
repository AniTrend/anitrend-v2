package co.anitrend.data.edge.media.mapper

import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class EdgeMediaMapperTest {

    @Test
    fun `maps and persists entity via local upsert`() = runBlocking {
        val dao = mockk<EdgeMediaLocalSource>(relaxed = true)
        val converter = EdgeMediaModelConverter()
        val mapper = EdgeMediaMapper(dao, converter)

        val media = EdgeMediaModel.Media(
            id = "444",
            title = EdgeMediaModel.SeriesTitleType(romaji = "r", english = null, native = null),
            cover = EdgeMediaModel.SeriesCoverImageType(medium = "m", large = null, extraLarge = null, color = null),
            banner = null, description = null, fanart = null,
            format = "TV", source = null, status = null, ageRating = null, isAdult = null,
            mediaId = EdgeMediaModel.SeriesIdType(aniList = 10),
            image = null, schedule = null, seasons = null, networks = null, themeSongs = null, trailers = null,
            updatedAt = 1L,
        )

        val response = EdgeMediaModel(media = media)
        val entity = mapper.onResponseMapFrom(response)
        assertEquals(10, entity.id)
        mapper.onResponseDatabaseInsert(entity)

        val captured = slot<EdgeMediaEntity>()
        coVerify { dao.upsert(capture(captured)) }
        assertEquals(entity, captured.captured)
    }
}

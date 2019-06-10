package co.anitrend.data.mapper.media

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.dao.query.MediaGenreDao
import co.anitrend.data.model.response.collection.GenreCollection
import co.anitrend.data.model.response.general.media.MediaGenre
import io.github.wax911.library.model.body.GraphContainer
import timber.log.Timber

class MediaGenreMapper(
    private val mediaGenreDao: MediaGenreDao
) : GraphQLMapper<GenreCollection, List<MediaGenre>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: GraphContainer<GenreCollection>): List<MediaGenre> {
        return source.data?.genreCollection?.map {
            MediaGenre(it)
        }.orEmpty()
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [handleResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaGenre>) {
        if (mappedData.isNotEmpty()) {
            val current = mediaGenreDao.count()
            if (current < 1)
                mediaGenreDao.insert(mappedData)
            else
                mediaGenreDao.upsert(mappedData)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}
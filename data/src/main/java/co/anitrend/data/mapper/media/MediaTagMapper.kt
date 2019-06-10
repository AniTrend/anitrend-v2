package co.anitrend.data.mapper.media

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.dao.query.MediaTagDao
import co.anitrend.data.model.response.collection.MediaTagCollection
import co.anitrend.data.model.response.general.media.MediaTag
import io.github.wax911.library.model.body.GraphContainer
import timber.log.Timber

class MediaTagMapper(
    private val mediaTagDao: MediaTagDao
) : GraphQLMapper<MediaTagCollection, List<MediaTag>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: GraphContainer<MediaTagCollection>): List<MediaTag> {
        return source.data?.mediaTagCollection.orEmpty()
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaTag>) {
        if (mappedData.isNotEmpty()) {
            val current = mediaTagDao.count()
            if (current < 1)
                mediaTagDao.insert(mappedData)
            else
                mediaTagDao.upsert(mappedData)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}
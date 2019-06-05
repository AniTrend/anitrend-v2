package co.anitrend.data.mapper.media

import androidx.lifecycle.MutableLiveData
import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.dao.query.MediaTagDao
import co.anitrend.data.model.response.collection.MediaTagCollection
import co.anitrend.data.model.response.general.media.MediaTag
import io.github.wax911.library.model.body.GraphContainer
import io.wax911.support.data.model.NetworkState
import retrofit2.Response
import timber.log.Timber

class MediaTagMapper(
    networkState: MutableLiveData<NetworkState>,
    private val mediaTagDao: MediaTagDao
) : GraphQLMapper<MediaTagCollection, List<MediaTag>>(networkState = networkState) {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [responseCallback]
     *
     * @param response retrofit response containing data
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(response: Response<GraphContainer<MediaTagCollection>?>): List<MediaTag> {
        return response.body()?.data?.mediaTagCollection.orEmpty()
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
            val array = mappedData.toTypedArray()
            if (current < 1)
                mediaTagDao.insert(*array)
            else
                mediaTagDao.update(*array)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}
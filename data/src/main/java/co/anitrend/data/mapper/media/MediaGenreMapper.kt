package co.anitrend.data.mapper.media

import androidx.lifecycle.MutableLiveData
import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.dao.query.MediaGenreDao
import co.anitrend.data.model.response.collection.GenreCollection
import co.anitrend.data.model.response.general.media.MediaGenre
import io.github.wax911.library.model.body.GraphContainer
import io.wax911.support.data.model.NetworkState
import retrofit2.Response
import timber.log.Timber

class MediaGenreMapper(
    networkState: MutableLiveData<NetworkState>,
    private val mediaGenreDao: MediaGenreDao
) : GraphQLMapper<GenreCollection, List<MediaGenre>>(networkState = networkState) {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [responseCallback]
     *
     * @param response retrofit response containing data
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(response: Response<GraphContainer<GenreCollection>?>): List<MediaGenre> {
        return response.body()?.data?.genreCollection?.map {
            MediaGenre(it)
        }.orEmpty()
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaGenre>) {
        if (mappedData.isNotEmpty()) {
            val current = mediaGenreDao.count()
            val array = mappedData.toTypedArray()
            if (current < 1)
                mediaGenreDao.insert(*array)
            else
                mediaGenreDao.update(*array)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}
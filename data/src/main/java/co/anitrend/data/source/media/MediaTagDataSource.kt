package co.anitrend.data.source.media

import android.os.Bundle
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.arch.source.WorkerDataSource
import co.anitrend.data.dao.DatabaseHelper
import co.anitrend.data.mapper.media.MediaTagMapper
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.data.model.NetworkState
import org.koin.core.inject

class MediaTagDataSource(
    private val mediaEndPoint: MediaEndPoint
) : WorkerDataSource() {

    override val databaseHelper by inject<DatabaseHelper>()

    /**
     * Handles the requesting data from a the network source and informs the
     * network state that it is in the loading state
     *
     * @param bundle request parameters or more
     */
    override suspend fun startRequestForType(bundle: Bundle?): NetworkState {
        val callRequest = mediaEndPoint.getMediaTags(
            GraphUtil.getDefaultQuery()
        )

        return MediaTagMapper(
            networkState,
            databaseHelper.mediaTagDao()
        ).executeUsing(callRequest)
    }

    /**
     * Clears all the data in a database table which will assure that
     * and refresh the backing storage medium with new network data
     *
     * @param bundle the request request parameters to use
     */
    override suspend fun refreshOrInvalidate(bundle: Bundle?): NetworkState {
        databaseHelper.mediaTagDao().deleteAll()
        return startRequestForType(bundle)
    }
}
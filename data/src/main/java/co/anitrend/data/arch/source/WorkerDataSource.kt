package co.anitrend.data.arch.source

import android.os.Bundle
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.SupportDataSource

/**
 * A type of data source that should be be used by coroutine workers/objects
 */
abstract class WorkerDataSource : SupportDataSource() {

    /**
     * Handles the requesting data from a the network source and informs the
     * network state that it is in the loading state
     *
     * @param bundle request parameters or more
     */
    abstract suspend fun startRequestForType(bundle: Bundle? = null): NetworkState

    /**
     * Clears all the data in a database table which will assure that
     * and refresh the backing storage medium with new network data
     *
     * @param bundle the request request parameters to use
     */
    abstract suspend fun refreshOrInvalidate(bundle: Bundle? = null): NetworkState
}
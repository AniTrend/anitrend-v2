package co.anitrend.data.model.contract

/**
 * Model query contract, enforces implementation of map conversion
 */
interface IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    fun toMap(): Map<String, Any?>
}
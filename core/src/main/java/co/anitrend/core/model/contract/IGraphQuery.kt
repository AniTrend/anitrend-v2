package co.anitrend.core.model.contract

import co.anitrend.core.api.RetroFactory
import co.anitrend.core.extension.getTypeToken
import timber.log.Timber

/**
 * Model query contract, enforces implementation of map conversion
 */
interface IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    @Throws(Throwable::class)
    fun toMap(): Map<String, Any> {
        return try {
            RetroFactory.gson.fromJson(toJsonString(),
                getTypeToken<Map<String, Any>>()
            )
        } catch (e: Exception) {
            Timber.e(e)
            throw Throwable("Did you forget to override the toString method in your query class?")
        }
    }

    /**
     * Converts a class properties to a json string
     */
    private fun toJsonString(): String =
        RetroFactory.gson.toJson(this)
}
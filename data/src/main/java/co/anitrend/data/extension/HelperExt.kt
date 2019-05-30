package co.anitrend.data.extension

import com.google.gson.reflect.TypeToken
import io.wax911.support.data.factory.contract.IRetrofitFactory
import java.lang.reflect.Type

/**
 * Extension function to get any type of given generic type
 */
inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type


/**
 * Creates a retrofit endpoint of the given class of type <T>
 */
inline fun <reified T> IRetrofitFactory.getEndPointOf() : T =
    createService(T::class.java)
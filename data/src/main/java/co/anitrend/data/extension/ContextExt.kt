package co.anitrend.data.extension

import android.content.Context
import co.anitrend.data.api.RetroFactory
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Extension function to get any type of given generic type
 */
inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type

/**
 * Creates a retrofit endpoint of the given class
 */
inline fun <reified T> Context.getEndPointOf() : T =
    RetroFactory.getInstance(applicationContext)
        .createService(T::class.java)

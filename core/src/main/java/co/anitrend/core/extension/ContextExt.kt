package co.anitrend.core.extension

import android.content.Context
import co.anitrend.core.api.RetroFactory
import co.anitrend.core.dao.DatabaseHelper
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun Context.getDatabaseHelper() =
    DatabaseHelper.getInstance(this)

inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type

/**
 * Creates a retrofit endpoint given the application context
 */
inline fun <reified T> Context.getEndPointOf() : T =
    RetroFactory.getInstance(applicationContext)
        .createService(T::class.java)
package co.anitrend.core.util

import android.content.Context
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.core.preference.SupportPreference

class Settings private constructor(context: Context) : SupportPreference(context) {


    var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(AUTHENTICATED_USER, -1)
        set(value) {
            field = value
            sharedPreferences.edit().putLong(AUTHENTICATED_USER, value).apply()
        }

    companion object : InstanceCreator<Settings, Context>({ Settings(it) }) {

        const val INVALID_USER_ID: Long = -1
        private const val updateChannel = "updateChannel"
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}
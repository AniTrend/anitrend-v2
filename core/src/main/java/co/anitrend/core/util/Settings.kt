package co.anitrend.core.util

import android.content.Context
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.core.preference.SupportPreference

class Settings private constructor(context: Context) : SupportPreference(context) {

    fun setAuthenticatedUser(userId: Long = -1) {
        val editor = sharedPreferences.edit()
        editor.putLong(AUTHENTICATED_USER, userId)
        editor.apply()
    }

    fun getAuthenticatedUser() : Long =
        sharedPreferences.getLong(AUTHENTICATED_USER, -1)

    companion object : InstanceCreator<Settings, Context>({ Settings(it) }) {

        private const val updateChannel = "updateChannel"
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}
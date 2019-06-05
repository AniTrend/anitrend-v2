package co.anitrend.data.util

import android.content.Context
import io.wax911.support.extension.preference.SupportPreference

class Settings(context: Context) : SupportPreference(context) {

    var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(AUTHENTICATED_USER, -1)
        set(value) {
            field = value
            sharedPreferences.edit().putLong(AUTHENTICATED_USER, value).apply()
        }

    // TODO: Implement preference values for sorting
    var isSortOrderDescending: Boolean = false


    companion object  {
        const val INVALID_USER_ID: Long = -1
        private const val updateChannel = "updateChannel"
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}
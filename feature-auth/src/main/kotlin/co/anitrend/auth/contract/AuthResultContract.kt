/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.auth.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import co.anitrend.auth.model.Authentication
import co.anitrend.data.auth.helper.AuthenticationType
import co.anitrend.data.auth.helper.authenticationUri

class AuthResultContract(
    private val clientId: String
): ActivityResultContract<Unit, Authentication>() {
    /** Create an intent that can be used for [Activity.startActivityForResult]  */
    override fun createIntent(context: Context, input: Unit?): Intent {
        val uri = authenticationUri(AuthenticationType.TOKEN, clientId)
        return Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_VIEW
            data = uri
        }
    }

    /** Convert result obtained from [Activity.onActivityResult] to O  */
    override fun parseResult(resultCode: Int, intent: Intent?): Authentication {
        TODO("Not yet implemented")
    }
}
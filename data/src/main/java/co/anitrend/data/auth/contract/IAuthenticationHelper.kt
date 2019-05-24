package co.anitrend.data.auth.contract

import android.net.Uri
import co.anitrend.data.auth.model.JsonWebToken
import io.wax911.support.core.util.SupportCoroutineUtil
import org.koin.core.KoinComponent

interface IAuthenticationHelper: SupportCoroutineUtil, KoinComponent {

    val jsonWebToken: JsonWebToken?

    /**
     * Once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an access_token parameter that includes the JWT access token
     * used to make requests on their behalf.
     *
     * @param callbackUri callback uri from authentication process
     * @return True if the operation was successful, false otherwise
     */
    suspend fun handleCallbackUri(callbackUri: Uri): Boolean

    companion object {
        const val CALLBACK_QUERY_KEY = "access_token"

        const val AUTHORIZATION = "Authorization"
    }
}
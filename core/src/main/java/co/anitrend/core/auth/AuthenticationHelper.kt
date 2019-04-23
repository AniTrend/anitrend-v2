package co.anitrend.core.auth

import android.net.Uri
import co.anitrend.core.BuildConfig
import co.anitrend.core.auth.contract.IAuthenticationHelper
import co.anitrend.core.auth.model.JsonWebToken
import co.anitrend.core.dao.DatabaseHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Provides an api to handle authentication based processes
 */
class AuthenticationHelper(private val databaseHelper: DatabaseHelper): IAuthenticationHelper, KoinComponent {

    override val jsonWebToken by lazy {
        databaseHelper.jsonWebTokenDao().findLatest()
    }

    /**
     * Once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an [CALLBACK_QUERY_KEY] parameter that includes the
     * JWT access token used to make requests on their behalf.
     *
     * @param callbackUri callback uri from authentication process
     * @return True if the operation was successful, false otherwise
     */
    override suspend fun handleCallbackUri(callbackUri: Uri): Boolean {
        val jwt = callbackUri.getQueryParameter(CALLBACK_QUERY_KEY)
        return if (!jwt.isNullOrBlank()) {
            val jsonWebTokenId = jsonWebToken?.id ?: 1
            databaseHelper.jsonWebTokenDao().insert(
                JsonWebToken(jsonWebTokenId, jwt)
            )
            true
        } else false
    }

    companion object {

        const val CALLBACK_QUERY_KEY = "access_token"

        const val AUTHORIZATION = "Authorization"

        val authenticationUri: Uri by lazy {
            Uri.Builder()
                .scheme("https")
                .authority(BuildConfig.apiAuthUrl)
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.cliendId)
                .appendQueryParameter("response_type", "token")
                .build()
        }
    }
}
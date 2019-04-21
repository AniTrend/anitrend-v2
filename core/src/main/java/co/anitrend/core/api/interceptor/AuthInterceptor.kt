package co.anitrend.core.api.interceptor

import android.content.Context
import co.anitrend.core.auth.AuthenticationHelper
import co.anitrend.core.auth.contract.IAuthenticationHelper
import co.anitrend.core.extension.getDatabaseHelper
import co.anitrend.core.util.Settings
import io.wax911.support.extension.isConnectedToNetwork
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Authentication interceptor add headers dynamically when the application is authenticated.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class AuthInterceptor(private val settings: Settings, private val context: Context?) : Interceptor {

    private val databaseHelper by lazy {
        context?.getDatabaseHelper()
    }

    private val authenticationHelper: IAuthenticationHelper by lazy {
        AuthenticationHelper(databaseHelper)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (settings.isAuthenticated) {
            val jsonWebToken = authenticationHelper.jsonWebToken
            when {
                jsonWebToken != null -> builder.addHeader(
                    AuthenticationHelper.AUTHORIZATION,
                    jsonWebToken.getTokenKey()
                )
                context.isConnectedToNetwork() -> {
                    settings.isAuthenticated = false
                    settings.setAuthenticatedUser()
                    databaseHelper?.clearAllTables()
                    Timber.e("${toString()} -> authentication token is null, application is logging user out!")
                }
                else ->
                    Timber.i("${toString()} -> Cannot refresh authentication token, application currently offline.")
            }
        }

        return chain.proceed(builder.build())
    }
}

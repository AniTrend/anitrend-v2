package co.anitrend.data.api.interceptor

import io.wax911.support.data.auth.contract.ISupportAuthentication
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.data.model.extension.isUnauthorized
import okhttp3.*

/**
 * Authentication interceptor add headers dynamically when the application is authenticated.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class AuthInterceptor(private val authenticationHelper: ISupportAuthentication) : Authenticator {

    /**
     * Returns a request that includes a credential to satisfy an authentication challenge in `response`.
     * Returns null if the challenge cannot be satisfied.
     *
     *
     * The route is best effort, it currently may not always be provided even when logically
     * available. It may also not be provided when an authenticator is re-used manually in an
     * application interceptor, such as when implementing client-specific retries.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        val networkState = NetworkState(code = response.code(), status = SupportStateType.LOADING)
        if (networkState.isUnauthorized()) {
            val requestBuilder = response.request().newBuilder()
            if (authenticationHelper.isAuthenticated) {
                authenticationHelper.injectHeaders(requestBuilder)
                return requestBuilder.build()
            }
        }
        return null
    }
}

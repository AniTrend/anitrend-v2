package co.anitrend.core.auth

import co.anitrend.core.BuildConfig
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class AuthenticationHelperTest {

    /**
     * Test will fail because of http://g.co/androidstudio/not-mocked
     */
    fun callBackUrlIsCorrect() {
        assertEquals(
            "${BuildConfig.apiAuthUrl}/authorize?client_id=${BuildConfig.cliendId}&response_type=token'",
            AuthenticationHelper.authenticationUri.toString()
        )
    }
}
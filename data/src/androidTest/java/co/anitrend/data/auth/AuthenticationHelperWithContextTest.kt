package co.anitrend.data.auth

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class AuthenticationHelperWithContextTest {

    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().context }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("co.anitrend.data.auth.test", appContext.packageName)
    }

    @Test
    fun callBackUrlIsCorrect() {
        // This test may fail due to Uri class not being mock-able
        val authRequestUrl = "${BuildConfig.apiAuthUrl}/authorize?client_id=${BuildConfig.cliendId}&response_type=token'"
        val authRequestUri = AuthenticationHelper.authenticationUri

        assertEquals(authRequestUrl, authRequestUri.toString())
    }
}

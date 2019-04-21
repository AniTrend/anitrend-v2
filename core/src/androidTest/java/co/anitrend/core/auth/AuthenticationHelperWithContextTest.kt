package co.anitrend.core.auth

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
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
        assertEquals("co.anitrend.core.auth.test", appContext.packageName)
    }
}
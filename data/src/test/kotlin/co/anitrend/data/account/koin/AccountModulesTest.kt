package co.anitrend.data.account.koin

import co.anitrend.data.account.AccountInteractor
import co.anitrend.data.auth.source.contract.AuthSource
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class AccountModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun accountModulesResolveInteractorPath() {
        stopKoin()

        startKoin {
            modules(
                testAccountModule,
                accountModules,
            )
        }

        assertNotNull(getKoin().get<AccountInteractor>())
    }
}

private val testAccountModule =
    module {
        single<AuthSource> { mockk(relaxed = true) }
    }

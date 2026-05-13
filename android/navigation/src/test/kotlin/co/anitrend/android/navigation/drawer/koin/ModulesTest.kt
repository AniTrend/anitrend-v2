package co.anitrend.android.navigation.drawer.koin

import co.anitrend.android.navigation.drawer.component.content.NavigationDrawerHostFragment
import co.anitrend.core.koin.scope.AppScope
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull

class ModulesTest {
    @Test
    fun `drawer scope resolves the navigation drawer host fragment`() {
        val application =
            koinApplication {
                modules(fragmentModule)
            }
        val scope =
            application.koin.createScope(
                scopeId = "drawer-test",
                qualifier = AppScope.BOTTOM_NAV_DRAWER.qualifier,
            )

        val hostFragment = scope.getOrNull<NavigationDrawerHostFragment>()

        assertNotNull(hostFragment)

        scope.close()
        application.close()
    }
}

package co.anitrend.data.favourite.koin

import co.anitrend.data.favourite.mapper.FavouriteMapper
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class FavouriteModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun favouriteModulesResolveMapperPath() {
        stopKoin()

        startKoin {
            modules(favouriteModules)
        }

        assertNotNull(getKoin().get<FavouriteMapper>())
    }
}

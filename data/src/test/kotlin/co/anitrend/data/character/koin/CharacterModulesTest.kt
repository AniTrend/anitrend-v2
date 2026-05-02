package co.anitrend.data.character.koin

import co.anitrend.data.character.converter.CharacterConverter
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class CharacterModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun characterModulesResolveConverterPath() {
        stopKoin()

        startKoin {
            modules(*characterModules.toTypedArray())
        }

        assertNotNull(getKoin().get<CharacterConverter>())
    }
}

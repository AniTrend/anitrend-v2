package co.anitrend.data.character.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.character.converter.CharacterConverter
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.test.Test
import kotlin.test.assertNotNull

class CharacterModulesTest {
    @Test
    fun characterModulesResolveConverterPath() {
        stopKoin()

        val store = mockk<IAniTrendStore>()
        every { store.characterDao() } returns mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single<IAniTrendStore> { store }
                },
                characterModules,
            )
        }

        assertNotNull(getKoin().get<CharacterConverter>())
    }
}

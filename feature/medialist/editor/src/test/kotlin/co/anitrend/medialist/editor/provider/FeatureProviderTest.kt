package co.anitrend.medialist.editor.provider

import co.anitrend.medialist.editor.component.content.MediaListEditorSheet
import kotlin.test.Test
import kotlin.test.assertEquals

class FeatureProviderTest {

    @Test
    fun `sheet always routes to compose editor`() {
        val provider = FeatureProvider()

        assertEquals(MediaListEditorSheet::class.java, provider.sheet())
    }
}

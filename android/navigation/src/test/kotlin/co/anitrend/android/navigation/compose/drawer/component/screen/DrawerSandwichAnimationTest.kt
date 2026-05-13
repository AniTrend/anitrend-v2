package co.anitrend.android.navigation.compose.drawer.component.screen

import kotlin.test.Test
import kotlin.test.assertEquals

class DrawerSandwichAnimationTest {
    @Test
    fun `closed sandwich keeps navigation visible and account hidden`() {
        val animation = calculateDrawerSandwichAnimation(progress = 0f)

        assertEquals(0f, animation.navigationProgress)
        assertEquals(0f, animation.accountProgress)
        assertEquals(1f, animation.foregroundAlpha)
        assertEquals(1f, animation.avatarAlpha)
        assertEquals(1f, animation.cutoutProgress)
    }

    @Test
    fun `halfway sandwich has hidden navigation before account enters`() {
        val animation = calculateDrawerSandwichAnimation(progress = 0.5f)

        assertEquals(1f, animation.navigationProgress)
        assertEquals(0f, animation.accountProgress)
        assertEquals(0f, animation.foregroundAlpha)
        assertEquals(0f, animation.avatarAlpha)
        assertEquals(0f, animation.cutoutProgress)
    }

    @Test
    fun `open sandwich keeps navigation hidden and account visible`() {
        val animation = calculateDrawerSandwichAnimation(progress = 1f)

        assertEquals(1f, animation.navigationProgress)
        assertEquals(1f, animation.accountProgress)
        assertEquals(0f, animation.foregroundAlpha)
        assertEquals(0f, animation.avatarAlpha)
        assertEquals(0f, animation.cutoutProgress)
    }
}

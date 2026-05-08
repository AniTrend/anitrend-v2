package co.anitrend.android.core.compose.shape

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DrawerCutoutShapeTest {
    @Test
    fun `cutout arc sweeps match legacy semicircle edge treatment`() {
        val geometry =
            calculateDrawerCutoutGeometry(
                width = 360f,
                cutoutDiameter = 32f,
                cutoutMargin = 8f,
                cutoutRoundedCornerRadius = 24f,
                cutoutVerticalOffset = 0f,
                cutoutProgress = 1f,
            ) as DrawerCutoutGeometry.Cutout

        assertTrue(geometry.leftRoundedCornerSweep > 0f)
        assertTrue(geometry.mainCutoutSweep < 0f)
        assertTrue(geometry.rightRoundedCornerSweep > 0f)
    }

    @Test
    fun `legacy cradle width matches avatar size`() {
        val geometry =
            calculateDrawerCutoutGeometry(
                width = 360f,
                cutoutDiameter = 32f,
                cutoutMargin = 8f,
                cutoutRoundedCornerRadius = 24f,
                cutoutVerticalOffset = 0f,
                cutoutProgress = 1f,
            ) as DrawerCutoutGeometry.Cutout

        assertEquals(48f, geometry.cradleDiameter)
    }
}

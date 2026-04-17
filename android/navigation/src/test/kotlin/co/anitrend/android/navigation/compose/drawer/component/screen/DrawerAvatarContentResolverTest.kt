package co.anitrend.android.navigation.compose.drawer.component.screen

import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.account.Account
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DrawerAvatarContentResolverTest {
    @Test
    fun `anonymous account uses adaptive local image content`() {
        val account =
            Account.Anonymous(
                titleRes = R.string.label_account_anonymous,
                imageRes = co.anitrend.core.R.mipmap.ic_launcher,
                isActiveUser = true,
            )

        val content = resolveDrawerAvatarContent(account)

        val adaptiveImage = assertIs<DrawerAvatarContent.AdaptiveLocalImage>(content)
        assertEquals(co.anitrend.core.R.mipmap.ic_launcher, adaptiveImage.imageRes)
        assertEquals(R.string.label_account_anonymous, adaptiveImage.contentDescriptionRes)
    }

    @Test
    fun `missing account uses tinted raster icon content`() {
        val content = resolveDrawerAvatarContent(account = null)

        val fallbackIcon = assertIs<DrawerAvatarContent.TintedIcon>(content)
        assertEquals(co.anitrend.core.R.mipmap.ic_launcher_foreground, fallbackIcon.iconRes)
    }
}

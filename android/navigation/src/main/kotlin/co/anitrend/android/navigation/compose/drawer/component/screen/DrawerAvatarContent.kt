package co.anitrend.android.navigation.compose.drawer.component.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.anitrend.android.navigation.drawer.model.account.Account
import co.anitrend.domain.common.entity.contract.ICoverImage

internal sealed interface DrawerAvatarContent {
    data class RemoteImage(
        val image: ICoverImage,
    ) : DrawerAvatarContent

    data class AdaptiveLocalImage(
        @DrawableRes val imageRes: Int,
        @StringRes val contentDescriptionRes: Int,
    ) : DrawerAvatarContent

    data class TintedIcon(
        @DrawableRes val iconRes: Int,
    ) : DrawerAvatarContent
}

internal fun resolveDrawerAvatarContent(account: Account?): DrawerAvatarContent =
    when (account) {
        is Account.Authenticated ->
            DrawerAvatarContent.RemoteImage(
                image = account.coverImage,
            )
        is Account.Anonymous ->
            DrawerAvatarContent.AdaptiveLocalImage(
                imageRes = account.imageRes,
                contentDescriptionRes = account.titleRes,
            )
        else ->
            DrawerAvatarContent.TintedIcon(
                iconRes = co.anitrend.core.R.mipmap.ic_launcher_foreground,
            )
    }

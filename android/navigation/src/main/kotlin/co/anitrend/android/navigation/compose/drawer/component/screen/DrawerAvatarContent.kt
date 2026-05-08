/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

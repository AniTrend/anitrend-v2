/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.settings.component.content.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.extensions.startActivity
import org.koin.compose.koinInject

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    settings: IAuthenticationSettings = koinInject(),
) {
    val context = LocalContext.current
    AccountContent(
        modifier = modifier,
        onAddNewAccount = {
            AuthRouter.startActivity(context)
        },
    )
}

@Composable
private fun AccountContent(
    modifier: Modifier = Modifier,
    onAddNewAccount: () -> Unit,
) {
    Column(modifier = modifier) {
        AniTrendHintCard(
            title = "Multiple accounts",
            description = "You can switch between authenticated accounts easily. Tap here to add a new account.",
            icon = Icons.Outlined.AccountTree,
            onClick = onAddNewAccount,
        )
    }
}

@AniTrendPreview.Default
@Composable
fun AccountScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AccountContent(
            onAddNewAccount = {},
        )
    }
}

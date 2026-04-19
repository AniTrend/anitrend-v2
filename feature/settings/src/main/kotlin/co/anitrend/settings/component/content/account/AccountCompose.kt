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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsValueRow
import org.koin.compose.koinInject

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    settings: IAuthenticationSettings = koinInject(),
    onAniListSettings: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    AccountContent(
        modifier = modifier,
        isAuthenticated = settings.isAuthenticated.value,
        userId = settings.authenticatedUserId.value,
        onAniListSettings = onAniListSettings,
        onAddNewAccount = {
            AuthRouter.startActivity(context)
        },
    )
}

@Composable
private fun AccountContent(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    userId: Long = IAuthenticationSettings.INVALID_USER_ID,
    onAniListSettings: (() -> Unit)?,
    onAddNewAccount: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(listOf(Unit)) {
            AniTrendHintCard(
                title =
                    if (isAuthenticated) {
                        stringResource(
                            R.string.title_settings_account_connected,
                        )
                    } else {
                        stringResource(R.string.title_settings_account_sign_in)
                    },
                description =
                    if (isAuthenticated) {
                        stringResource(R.string.summary_settings_account_connected)
                    } else {
                        stringResource(R.string.summary_settings_account_sign_in)
                    },
                icon = Icons.Outlined.AccountTree,
                onClick = onAddNewAccount,
            )
        }
        if (isAuthenticated && onAniListSettings != null) {
            items(listOf(Unit)) {
                SettingsSectionCard(
                    title = stringResource(R.string.title_settings_account_anilist_settings),
                    description = stringResource(R.string.summary_settings_account_anilist_settings),
                ) {
                    SettingsValueRow(
                        title = stringResource(R.string.action_settings_account_view_anilist_settings),
                        summary = stringResource(R.string.summary_settings_account_view_anilist_settings),
                        icon = Icons.Outlined.AccountCircle,
                        currentValue = stringResource(R.string.label_settings_account_read_only),
                        onClick = onAniListSettings,
                    )
                }
            }
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_account_authentication),
                description =
                    if (isAuthenticated) {
                        stringResource(
                            R.string.summary_settings_account_viewer_id,
                            userId,
                        )
                    } else {
                        stringResource(R.string.summary_settings_account_no_authenticated)
                    },
            ) {
                SettingsValueRow(
                    title = stringResource(R.string.action_settings_account_add_or_refresh_sign_in),
                    summary = stringResource(R.string.summary_settings_account_add_or_refresh_sign_in),
                    icon = Icons.Outlined.AccountTree,
                    currentValue =
                        if (isAuthenticated) {
                            stringResource(
                                R.string.label_settings_account_connected,
                            )
                        } else {
                            stringResource(R.string.label_settings_account_required)
                        },
                    onClick = onAddNewAccount,
                )
            }
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
fun AccountScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AccountContent(
            isAuthenticated = true,
            userId = 42,
            onAniListSettings = {},
            onAddNewAccount = {},
        )
    }
}

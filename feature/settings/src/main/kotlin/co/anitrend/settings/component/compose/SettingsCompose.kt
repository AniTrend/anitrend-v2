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
package co.anitrend.settings.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.navigation.SettingsRouter
import co.anitrend.settings.component.content.anilist.AniListSettingsScreen
import co.anitrend.settings.component.content.account.AccountScreen
import co.anitrend.settings.component.content.developer.DeveloperScreen
import co.anitrend.settings.component.content.featureflags.FeatureFlagsScreen
import co.anitrend.settings.component.content.log.LogViewerScreen
import co.anitrend.settings.component.content.task.TaskScreen
import co.anitrend.settings.model.SettingItem
import co.anitrend.settings.component.content.privacy.PrivacyScreen
import co.anitrend.settings.component.content.filters.FiltersScreen
import co.anitrend.settings.component.content.power.PowerScreen
import co.anitrend.settings.component.content.locale.LocaleScreen
import co.anitrend.settings.component.content.theme.ThemeScreen
import co.anitrend.settings.component.content.notification.NotificationScreen
import co.anitrend.settings.component.content.sync.SynchronizationScreen
import co.anitrend.settings.component.content.storage.StorageScreen
import co.anitrend.settings.component.presenter.SettingsPresenter

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    SettingsItemsList(modifier = modifier, settingsItems = settingsItems)
}

@Composable
fun SettingsContentScreen(
    navigationController: NavHostController,
    settingsItems: List<SettingItem>,
    presenter: SettingsPresenter,
    onBackPress: () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navigationController,
            startDestination = SettingsRouter.Destination.ROOT.name,
        ) {
            composable(
                route = SettingsRouter.Destination.ROOT.name,
                content = {
                    SettingsContent(
                        settingsItems = settingsItems,
                    )
                },
            )
            composable(
                route = SettingsRouter.Destination.ACCOUNTS.name,
                content = {
                    AccountScreen(
                        onAniListSettings = {
                            navigationController.navigate(SettingsRouter.Destination.ANILIST_SETTINGS.name)
                        },
                    )
                },
            )
            composable(
                route = SettingsRouter.Destination.ANILIST_SETTINGS.name,
                content = {
                    AniListSettingsScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.PRIVACY.name,
                content = {
                    PrivacyScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.FILTERS.name,
                content = {
                    FiltersScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.POWER.name,
                content = {
                    PowerScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.LOCALE.name,
                content = {
                    LocaleScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.THEME.name,
                content = {
                    ThemeScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.NOTIFICATION.name,
                content = {
                    NotificationScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.SYNCHRONIZATION.name,
                content = {
                    SynchronizationScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.STORAGE.name,
                content = {
                    StorageScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.DEVELOPER.name,
                content = {
                    DeveloperScreen(
                        presenter = presenter,
                        navigateTo = {
                            navigationController.navigate(it.name)
                        },
                    )
                },
            )
            composable(
                route = SettingsRouter.Destination.FEATURE_FLAGS.name,
                content = {
                    FeatureFlagsScreen(presenter = presenter)
                },
            )
            composable(
                route = SettingsRouter.Destination.LOGS.name,
                content = {
                    LogViewerScreen()
                },
            )
            composable(
                route = SettingsRouter.Destination.TASK.name,
                content = {
                    TaskScreen()
                },
            )
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun SettingsScreenPreview() {
    PreviewTheme(wrapInSurface = true) {
        SettingsContent(settingsItems = previewData())
    }
}

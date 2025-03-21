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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.compose.design.cards.AniTrendHintCard
import co.anitrend.core.android.compose.design.category.AniTrendCategoryHeader
import co.anitrend.core.android.compose.design.category.AniTrendCategoryItem
import co.anitrend.core.android.compose.design.toggle.AniTrendSwitch
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.navigation.SettingsRouter
import co.anitrend.settings.component.content.account.AccountScreen
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

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = settingsItems.size,
            key = { settingsItems[it].id },
        ) { index ->
            when (val item = settingsItems[index]) {
                is SettingItem.CategoryHeader -> AniTrendCategoryHeader(text = item.title)
                is SettingItem.HintCard ->
                    AniTrendHintCard(
                        title = item.title,
                        description = item.description,
                        icon = item.icon,
                        onClick = item.onClick,
                    )
                is SettingItem.SwitchSetting ->
                    AniTrendSwitch(
                        title = item.title,
                        description = item.summary,
                        icon = item.icon,
                        enabled = true,
                        isChecked = item.onClick(),
                        onClick = { item.onValueChange(!item.onClick()) },
                    )
                is SettingItem.ClickableSetting ->
                    AniTrendCategoryItem(
                        title = item.title,
                        description = item.summary,
                        icon = item.icon,
                        onClick = item.onClick,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                            )
                        },
                    )
                is SettingItem.DialogSetting<*> -> PreferenceDialog(item = item)
            }
        }
    }
}

@Composable
fun SettingsContentScreen(
    navigationController: NavHostController,
    settingsItems: List<SettingItem>,
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
                    AccountScreen()
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

@AniTrendPreview.Default
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        SettingsContent(settingsItems = PreviewData)
    }
}

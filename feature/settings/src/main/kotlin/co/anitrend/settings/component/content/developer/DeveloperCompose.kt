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
package co.anitrend.settings.component.content.developer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.settings.developer.IDeveloperSettings
import co.anitrend.data.settings.refresh.IRefreshBehaviourSettings
import co.anitrend.navigation.SettingsRouter
import co.anitrend.settings.component.compose.SettingsItemsList
import co.anitrend.settings.component.compose.previewDeveloperData
import co.anitrend.settings.component.presenter.SettingsPresenter
import co.anitrend.settings.model.SettingItem
import org.koin.compose.koinInject

internal data class DeveloperScreenState(
    val automaticHeapDump: Boolean,
    val showLeakLauncher: Boolean,
    val clearDataOnSwipeRefresh: Boolean,
) {
    fun asPresenterState() =
        SettingsPresenter.DeveloperSettingsState(
            automaticHeapDump = automaticHeapDump,
            showLeakLauncher = showLeakLauncher,
            clearDataOnSwipeRefresh = clearDataOnSwipeRefresh,
        )

    fun updateAutomaticHeapDump(
        newValue: Boolean,
        developerSettings: IDeveloperSettings,
    ): DeveloperScreenState {
        developerSettings.automaticHeapDump.value = newValue
        return copy(automaticHeapDump = newValue)
    }

    fun updateShowLeakLauncher(
        newValue: Boolean,
        developerSettings: IDeveloperSettings,
    ): DeveloperScreenState {
        developerSettings.showLeakLauncher.value = newValue
        return copy(showLeakLauncher = newValue)
    }

    fun updateClearDataOnSwipeRefresh(
        newValue: Boolean,
        refreshBehaviourSettings: IRefreshBehaviourSettings,
    ): DeveloperScreenState {
        refreshBehaviourSettings.clearDataOnSwipeRefresh.value = newValue
        return copy(clearDataOnSwipeRefresh = newValue)
    }
}

@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier,
    navigateTo: (SettingsRouter.Destination) -> Unit,
    presenter: SettingsPresenter = koinInject(),
    developerSettings: IDeveloperSettings = koinInject(),
    refreshBehaviourSettings: IRefreshBehaviourSettings = koinInject(),
) {
    var state by
        remember {
            mutableStateOf(
                DeveloperScreenState(
                    automaticHeapDump = developerSettings.automaticHeapDump.value,
                    showLeakLauncher = developerSettings.showLeakLauncher.value,
                    clearDataOnSwipeRefresh = refreshBehaviourSettings.clearDataOnSwipeRefresh.value,
                ),
            )
        }

    DeveloperContent(
        modifier = modifier,
        settingsItems =
            presenter
                .getDeveloperSettingsItems(
                    state = state.asPresenterState(),
                    navigateTo = navigateTo,
                ).withDeveloperScreenState(
                    state = state,
                    onStateChange = { state = it },
                    developerSettings = developerSettings,
                    refreshBehaviourSettings = refreshBehaviourSettings,
                ),
    )
}

private fun List<SettingItem>.withDeveloperScreenState(
    state: DeveloperScreenState,
    onStateChange: (DeveloperScreenState) -> Unit,
    developerSettings: IDeveloperSettings,
    refreshBehaviourSettings: IRefreshBehaviourSettings,
): List<SettingItem> =
    map { item ->
        when (item) {
            is SettingItem.SwitchSetting ->
                when (item.id) {
                    "heap_dump" ->
                        item.copy(
                            onValueChange = { newValue ->
                                onStateChange(state.updateAutomaticHeapDump(newValue, developerSettings))
                            },
                        )
                    "show_leak_launcher" ->
                        item.copy(
                            onValueChange = { newValue ->
                                onStateChange(state.updateShowLeakLauncher(newValue, developerSettings))
                            },
                        )
                    "clear_db_on_refresh" ->
                        item.copy(
                            onValueChange = { newValue ->
                                onStateChange(
                                    state.updateClearDataOnSwipeRefresh(newValue, refreshBehaviourSettings),
                                )
                            },
                        )
                    else -> item
                }
            else -> item
        }
    }

@Composable
private fun DeveloperContent(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    SettingsItemsList(modifier = modifier, settingsItems = settingsItems)
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun DeveloperScreenPreview() {
    PreviewTheme(wrapInSurface = true) {
        DeveloperContent(settingsItems = previewDeveloperData())
    }
}

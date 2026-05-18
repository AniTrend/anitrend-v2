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
package co.anitrend.settings.component.content.featureflags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.settings.feature.FeatureFlag
import co.anitrend.data.settings.feature.FeatureFlags
import co.anitrend.data.settings.feature.IFeatureFlagSetting
import co.anitrend.settings.component.compose.SettingsItemsList
import co.anitrend.settings.component.compose.previewFeatureFlagData
import co.anitrend.settings.component.presenter.SettingsPresenter
import co.anitrend.settings.model.SettingItem
import org.koin.compose.koinInject

internal data class FeatureFlagsScreenState(
    val featureFlags: Set<String>,
) {
    fun asPresenterState() = SettingsPresenter.FeatureFlagSettingsState(featureFlags = featureFlags)

    fun updateExperimentalComposeUi(
        enabled: Boolean,
        featureFlagSetting: IFeatureFlagSetting,
    ): FeatureFlagsScreenState {
        val updatedFlags =
            FeatureFlags.setEnabled(
                flags = featureFlags,
                flag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI,
                enabled = enabled,
            )
        featureFlagSetting.featureFlags.value = updatedFlags
        return copy(featureFlags = updatedFlags)
    }
}

@Composable
fun FeatureFlagsScreen(
    modifier: Modifier = Modifier,
    presenter: SettingsPresenter = koinInject(),
    featureFlagSetting: IFeatureFlagSetting = koinInject(),
) {
    var state by remember { mutableStateOf(FeatureFlagsScreenState(featureFlags = featureFlagSetting.featureFlags.value)) }

    FeatureFlagsContent(
        modifier = modifier,
        settingsItems =
            presenter
                .getFeatureFlagSettingsItems(
                    state = state.asPresenterState(),
                ).withFeatureFlagScreenState(
                    state = state,
                    onStateChange = { state = it },
                    featureFlagSetting = featureFlagSetting,
                ),
    )
}

private fun List<SettingItem>.withFeatureFlagScreenState(
    state: FeatureFlagsScreenState,
    onStateChange: (FeatureFlagsScreenState) -> Unit,
    featureFlagSetting: IFeatureFlagSetting,
): List<SettingItem> =
    map { item ->
        when (item) {
            is SettingItem.SwitchSetting ->
                if (item.id == FeatureFlag.EXPERIMENTAL_COMPOSE_UI.key) {
                    item.copy(
                        onValueChange = { enabled ->
                            onStateChange(state.updateExperimentalComposeUi(enabled, featureFlagSetting))
                        },
                    )
                } else {
                    item
                }
            else -> item
        }
    }

@Composable
private fun FeatureFlagsContent(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    SettingsItemsList(modifier = modifier, settingsItems = settingsItems)
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun FeatureFlagsScreenPreview() {
    PreviewTheme(wrapInSurface = true) {
        FeatureFlagsContent(
            settingsItems = previewFeatureFlagData(enabledFlag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI),
        )
    }
}

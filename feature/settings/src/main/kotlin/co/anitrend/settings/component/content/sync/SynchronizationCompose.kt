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
package co.anitrend.settings.component.content.sync

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.choice.AniTrendSingleChoiceItem
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsHeroCard
import co.anitrend.settings.component.compose.SettingsSectionCard
import org.koin.compose.koinInject
import kotlin.math.max

@Composable
fun SynchronizationScreen(
    modifier: Modifier = Modifier,
    settings: ISyncSettings = koinInject(),
) {
    var metadataInterval by remember { mutableIntStateOf(max(settings.metaSyncInterval.value, ISyncSettings.MINIMUM_INTERVAL)) }
    var listInterval by remember { mutableIntStateOf(max(settings.listSyncInterval.value, ISyncSettings.MINIMUM_INTERVAL)) }
    var userInterval by remember { mutableIntStateOf(settings.userSyncInterval.value) }
    val metaOptions = listOf(900, 1800, 3600, 7200, 14400, 43200)
    val listOptions = listOf(900, 1800, 3600, 7200, 14400, 43200)
    val userOptions = listOf(300, 900, 1800, 3600, 7200)

    SynchronizationContent(
        modifier = modifier,
        metadataInterval = metadataInterval,
        listInterval = listInterval,
        userInterval = userInterval,
        metaOptions = metaOptions,
        listOptions = listOptions,
        userOptions = userOptions,
        onMetadataSelected = {
            settings.metaSyncInterval.value = it
            metadataInterval = it
        },
        onListSelected = {
            settings.listSyncInterval.value = it
            listInterval = it
        },
        onUserSelected = {
            settings.userSyncInterval.value = it
            userInterval = it
        },
    )
}

@Composable
private fun SynchronizationContent(
    modifier: Modifier = Modifier,
    metadataInterval: Int,
    listInterval: Int,
    userInterval: Int,
    metaOptions: List<Int>,
    listOptions: List<Int>,
    userOptions: List<Int>,
    onMetadataSelected: (Int) -> Unit,
    onListSelected: (Int) -> Unit,
    onUserSelected: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            SettingsHeroCard(
                title = stringResource(R.string.preference_title_sync),
                description = stringResource(R.string.summary_settings_sync_overview),
                icon = Icons.Outlined.Sync,
                currentValue = labelForSeconds(listInterval),
            )
        }
        item {
            SyncIntervalSection(
                title = stringResource(R.string.title_settings_sync_metadata_refresh),
                description = stringResource(R.string.summary_settings_sync_metadata_refresh),
                selected = metadataInterval,
                options = metaOptions,
                onSelected = onMetadataSelected,
            )
        }
        item {
            SyncIntervalSection(
                title = stringResource(R.string.title_settings_sync_list_refresh),
                description = stringResource(R.string.summary_settings_sync_list_refresh),
                selected = listInterval,
                options = listOptions,
                onSelected = onListSelected,
            )
        }
        item {
            SyncIntervalSection(
                title = stringResource(R.string.title_settings_sync_profile_refresh),
                description = stringResource(R.string.summary_settings_sync_profile_refresh),
                selected = userInterval,
                options = userOptions,
                onSelected = onUserSelected,
            )
        }
    }
}

@Composable
private fun SyncIntervalSection(
    title: String,
    description: String,
    selected: Int,
    options: List<Int>,
    onSelected: (Int) -> Unit,
) {
    SettingsSectionCard(title = title, description = description) {
        options.forEach { option ->
            AniTrendSingleChoiceItem(
                text = labelForSeconds(option),
                selected = selected == option,
                onOptionSelected = { onSelected(option) },
            )
        }
    }
}

@Composable
private fun labelForSeconds(seconds: Int): String {
    val minutes = seconds / 60
    return when {
        minutes < 60 -> stringResource(R.string.label_settings_sync_every_minutes, minutes)
        minutes % 60 == 0 -> {
            val hours = minutes / 60
            if (hours ==
                1
            ) {
                stringResource(R.string.label_settings_sync_every_hour, hours)
            } else {
                stringResource(R.string.label_settings_sync_every_hours, hours)
            }
        }
        else -> {
            val hours = minutes / 60
            val rem = minutes % 60
            stringResource(R.string.label_settings_sync_every_hr_min, hours, rem)
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun SynchronizationScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        SynchronizationContent(
            metadataInterval = 3600,
            listInterval = 1800,
            userInterval = 300,
            metaOptions = listOf(900, 1800, 3600, 7200, 14400, 43200),
            listOptions = listOf(900, 1800, 3600, 7200, 14400, 43200),
            userOptions = listOf(300, 900, 1800, 3600, 7200),
            onMetadataSelected = {},
            onListSelected = {},
            onUserSelected = {},
        )
    }
}

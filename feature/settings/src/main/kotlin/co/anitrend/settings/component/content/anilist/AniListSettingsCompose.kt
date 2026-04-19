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
package co.anitrend.settings.component.content.anilist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.core.extensions.handleViewIntent
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsHeroCard
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsValueRow
import co.anitrend.settings.component.content.anilist.model.AniListSettingsEntry
import co.anitrend.settings.component.content.anilist.model.AniListSettingsGroup
import co.anitrend.settings.component.content.anilist.model.AniListSettingsMapper
import co.anitrend.settings.component.content.anilist.model.AniListSettingsUiState
import co.anitrend.settings.component.content.anilist.viewmodel.AniListSettingsViewModel
import org.koin.compose.koinInject

private const val ANILIST_SETTINGS_URL = "https://anilist.co/settings"

@Composable
fun AniListSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: AniListSettingsViewModel = koinInject(),
    settings: IAuthenticationSettings = koinInject(),
) {
    val context = LocalContext.current
    val user by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()
    val isAuthenticated = settings.isAuthenticated.value

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            viewModel.load()
        }
    }

    if (!isAuthenticated) {
        AniListSettingsSignedOutContent(
            onSignIn = {
                AuthRouter.startActivity(context)
            },
        )
        return
    }

    AniListSettingsContent(
        modifier = modifier,
        uiState = (user as? User.Authenticated)?.let(AniListSettingsMapper::from),
        loadState = loadState,
        onOpenAniListSettings = { context.handleViewIntent(ANILIST_SETTINGS_URL) },
        onSync = viewModel::sync,
    )
}

@Composable
private fun AniListSettingsContent(
    modifier: Modifier = Modifier,
    uiState: AniListSettingsUiState?,
    loadState: LoadState?,
    onOpenAniListSettings: () -> Unit,
    onSync: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            SettingsHeroCard(
                title = stringResource(R.string.title_settings_anilist_overview),
                description = stringResource(R.string.summary_settings_anilist_overview),
                icon = Icons.Outlined.AccountCircle,
                currentValue = syncStateLabel(loadState = loadState, hasData = uiState != null),
            )
        }
        item {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_anilist_actions),
                description = stringResource(R.string.summary_settings_anilist_actions),
            ) {
                SettingsValueRow(
                    title = stringResource(R.string.action_settings_anilist_open_web_settings),
                    summary = stringResource(R.string.summary_settings_anilist_open_web_settings),
                    icon = Icons.Outlined.AccountCircle,
                    onClick = onOpenAniListSettings,
                )
                SettingsValueRow(
                    title = stringResource(R.string.action_settings_anilist_sync),
                    summary = stringResource(R.string.summary_settings_anilist_sync),
                    icon = Icons.Outlined.Sync,
                    currentValue =
                        when (loadState) {
                            is LoadState.Error -> stringResource(R.string.label_settings_anilist_sync_retry)
                            is LoadState.Loading -> stringResource(R.string.label_settings_anilist_sync_loading)
                            else -> stringResource(R.string.label_settings_anilist_sync_refresh)
                        },
                    onClick = onSync,
                )
            }
        }
        if (uiState == null && loadState is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp).align(Alignment.Center),
                    )
                }
            }
        }
        if (loadState is LoadState.Error) {
            item {
                SettingsSectionCard(
                    title =
                        if (uiState == null) {
                            stringResource(R.string.title_settings_anilist_unavailable)
                        } else {
                            stringResource(R.string.title_settings_anilist_sync_issue)
                        },
                    description =
                        loadState.details.message?.takeIf(String::isNotBlank)
                            ?: if (uiState == null) {
                                stringResource(R.string.summary_settings_anilist_unavailable)
                            } else {
                                stringResource(R.string.summary_settings_anilist_sync_issue)
                            },
                ) {}
            }
        }
        if (uiState != null) {
            items(uiState.groups) { group ->
                SettingsSectionCard(
                    title = group.group.title(),
                    description = group.group.description(),
                ) {
                    group.entries.forEachIndexed { index, entry ->
                        AniListSettingsReadOnlyRow(entry = entry)
                        if (index < group.entries.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AniListSettingsSignedOutContent(onSignIn: () -> Unit) {
    SettingsHeroCard(
        title = stringResource(R.string.title_settings_anilist_signed_out),
        description = stringResource(R.string.summary_settings_anilist_signed_out),
        icon = Icons.Outlined.AccountTree,
        currentValue = stringResource(R.string.label_settings_account_required),
        actionLabel = stringResource(R.string.action_settings_account_add_or_refresh_sign_in),
        onClick = onSignIn,
    )
}

@Composable
private fun AniListSettingsReadOnlyRow(entry: AniListSettingsUiState.Entry) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Text(
            text = entry.entry.label(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = entry.value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun syncStateLabel(
    loadState: LoadState?,
    hasData: Boolean,
): String =
    when (loadState) {
        is LoadState.Error ->
            if (hasData) {
                stringResource(R.string.label_settings_anilist_state_cached)
            } else {
                stringResource(R.string.label_settings_anilist_state_unavailable)
            }
        is LoadState.Loading ->
            if (hasData) {
                stringResource(R.string.label_settings_anilist_state_refreshing)
            } else {
                stringResource(R.string.label_settings_anilist_state_loading)
            }
        else -> stringResource(R.string.label_settings_anilist_state_synced)
    }

@Composable
private fun AniListSettingsGroup.title(): String =
    when (this) {
        AniListSettingsGroup.Profile -> stringResource(R.string.title_settings_anilist_group_profile)
        AniListSettingsGroup.General -> stringResource(R.string.title_settings_anilist_group_general)
        AniListSettingsGroup.Notifications -> stringResource(R.string.title_settings_anilist_group_notifications)
        AniListSettingsGroup.AnimeList -> stringResource(R.string.title_settings_anilist_group_anime_list)
        AniListSettingsGroup.MangaList -> stringResource(R.string.title_settings_anilist_group_manga_list)
    }

@Composable
private fun AniListSettingsGroup.description(): String =
    when (this) {
        AniListSettingsGroup.Profile -> stringResource(R.string.summary_settings_anilist_group_profile)
        AniListSettingsGroup.General -> stringResource(R.string.summary_settings_anilist_group_general)
        AniListSettingsGroup.Notifications -> stringResource(R.string.summary_settings_anilist_group_notifications)
        AniListSettingsGroup.AnimeList -> stringResource(R.string.summary_settings_anilist_group_anime_list)
        AniListSettingsGroup.MangaList -> stringResource(R.string.summary_settings_anilist_group_manga_list)
    }

@Composable
private fun AniListSettingsEntry.label(): String =
    when (this) {
        AniListSettingsEntry.ProfileAbout -> stringResource(R.string.label_settings_anilist_profile_about)
        AniListSettingsEntry.ProfileDonatorBadge -> stringResource(R.string.label_settings_anilist_profile_donator_badge)
        AniListSettingsEntry.ProfileColor -> stringResource(R.string.label_settings_anilist_profile_color)
        AniListSettingsEntry.TitleLanguage -> stringResource(R.string.label_settings_anilist_title_language)
        AniListSettingsEntry.StaffNameLanguage -> stringResource(R.string.label_settings_anilist_staff_name_language)
        AniListSettingsEntry.DisplayAdultContent -> stringResource(R.string.label_settings_anilist_display_adult_content)
        AniListSettingsEntry.TimeZone -> stringResource(R.string.label_settings_anilist_timezone)
        AniListSettingsEntry.AiringNotifications -> stringResource(R.string.label_settings_anilist_airing_notifications)
        AniListSettingsEntry.NotificationOptions -> stringResource(R.string.label_settings_anilist_notification_options)
        AniListSettingsEntry.AnimeScoreFormat -> stringResource(R.string.label_settings_anilist_anime_score_format)
        AniListSettingsEntry.AnimeRowOrder -> stringResource(R.string.label_settings_anilist_anime_row_order)
        AniListSettingsEntry.AnimeSectionOrder -> stringResource(R.string.label_settings_anilist_anime_section_order)
        AniListSettingsEntry.AnimeSplitCompletedSection ->
            stringResource(R.string.label_settings_anilist_anime_split_completed_section)
        AniListSettingsEntry.AnimeCustomLists -> stringResource(R.string.label_settings_anilist_anime_custom_lists)
        AniListSettingsEntry.AnimeAdvancedScoring -> stringResource(R.string.label_settings_anilist_anime_advanced_scoring)
        AniListSettingsEntry.AnimeAdvancedScoringEnabled ->
            stringResource(R.string.label_settings_anilist_anime_advanced_scoring_enabled)
        AniListSettingsEntry.MangaScoreFormat -> stringResource(R.string.label_settings_anilist_manga_score_format)
        AniListSettingsEntry.MangaRowOrder -> stringResource(R.string.label_settings_anilist_manga_row_order)
        AniListSettingsEntry.MangaSectionOrder -> stringResource(R.string.label_settings_anilist_manga_section_order)
        AniListSettingsEntry.MangaSplitCompletedSection ->
            stringResource(R.string.label_settings_anilist_manga_split_completed_section)
        AniListSettingsEntry.MangaCustomLists -> stringResource(R.string.label_settings_anilist_manga_custom_lists)
        AniListSettingsEntry.MangaAdvancedScoring -> stringResource(R.string.label_settings_anilist_manga_advanced_scoring)
        AniListSettingsEntry.MangaAdvancedScoringEnabled ->
            stringResource(R.string.label_settings_anilist_manga_advanced_scoring_enabled)
    }

private fun previewAniListSettingsUiState(): AniListSettingsUiState =
    AniListSettingsUiState(
        groups =
            listOf(
                AniListSettingsUiState.Group(
                    group = AniListSettingsGroup.Profile,
                    entries =
                        listOf(
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.ProfileAbout,
                                value = "Collector, seasonal watcher, and soundtrack-first scorekeeper.",
                            ),
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.ProfileColor,
                                value = "Blue",
                            ),
                        ),
                ),
                AniListSettingsUiState.Group(
                    group = AniListSettingsGroup.General,
                    entries =
                        listOf(
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.TitleLanguage,
                                value = "English",
                            ),
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.StaffNameLanguage,
                                value = "Native",
                            ),
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.TimeZone,
                                value = "+02:00",
                            ),
                        ),
                ),
                AniListSettingsUiState.Group(
                    group = AniListSettingsGroup.Notifications,
                    entries =
                        listOf(
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.AiringNotifications,
                                value = "Enabled",
                            ),
                            AniListSettingsUiState.Entry(
                                entry = AniListSettingsEntry.NotificationOptions,
                                value = "Airing, Activity reply",
                            ),
                        ),
                ),
            ),
    )

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun AniListSettingsContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniListSettingsContent(
            uiState = previewAniListSettingsUiState(),
            loadState = null,
            onOpenAniListSettings = {},
            onSync = {},
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun AniListSettingsSyncIssuePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniListSettingsContent(
            uiState = previewAniListSettingsUiState(),
            loadState = LoadState.Error(details = IllegalStateException("AniList sync timed out")),
            onOpenAniListSettings = {},
            onSync = {},
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun AniListSettingsSignedOutPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniListSettingsSignedOutContent(onSignIn = {})
    }
}

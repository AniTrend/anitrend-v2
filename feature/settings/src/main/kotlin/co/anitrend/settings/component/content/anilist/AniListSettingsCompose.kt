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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.core.extensions.handleViewIntent
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsHeroCard
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsToggleRow
import co.anitrend.settings.component.compose.SettingsValueRow
import co.anitrend.settings.component.content.anilist.model.AniListSettingsEditorMapper
import co.anitrend.settings.component.content.anilist.model.AniListSettingsEditorState
import co.anitrend.settings.component.content.anilist.model.AniListSettingsEntry
import co.anitrend.settings.component.content.anilist.model.AniListSettingsGroup
import co.anitrend.settings.component.content.anilist.model.AniListSettingsMapper
import co.anitrend.settings.component.content.anilist.model.AniListSettingsUiState
import co.anitrend.settings.component.content.anilist.viewmodel.AniListSettingsViewModel
import co.anitrend.domain.user.model.UserParam
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
    val authenticatedUser = user as? User.Authenticated
    var editingGroup by remember { mutableStateOf<AniListSettingsGroup?>(null) }
    var draft by remember(authenticatedUser) { mutableStateOf(authenticatedUser?.let(AniListSettingsEditorMapper::from)) }

    LaunchedEffect(authenticatedUser, editingGroup) {
        if (authenticatedUser != null && editingGroup == null) {
            draft = AniListSettingsEditorMapper.from(authenticatedUser)
        }
    }

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
        uiState = authenticatedUser?.let(AniListSettingsMapper::from),
        loadState = loadState,
        editingGroup = editingGroup,
        draft = draft,
        onEditGroup = {
            editingGroup = it
        },
        onDismissEditor = {
            editingGroup = null
        },
        onDraftChange = {
            draft = it
        },
        onSaveDraft = {
            viewModel.save(AniListSettingsEditorMapper.toParam(it))
            editingGroup = null
        },
        onOpenAniListSettings = { context.handleViewIntent(ANILIST_SETTINGS_URL) },
        onSync = viewModel::sync,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AniListSettingsContent(
    modifier: Modifier = Modifier,
    uiState: AniListSettingsUiState?,
    loadState: LoadState?,
    editingGroup: AniListSettingsGroup?,
    draft: AniListSettingsEditorState?,
    onEditGroup: (AniListSettingsGroup) -> Unit,
    onDismissEditor: () -> Unit,
    onDraftChange: (AniListSettingsEditorState) -> Unit,
    onSaveDraft: (AniListSettingsEditorState) -> Unit,
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
                    SettingsValueRow(
                        title = stringResource(R.string.action_settings_anilist_edit_section),
                        summary = stringResource(R.string.summary_settings_anilist_edit_section),
                        icon = Icons.Outlined.Edit,
                        currentValue = stringResource(R.string.label_settings_anilist_edit_ready),
                        onClick = {
                            onEditGroup(group.group)
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f),
                    )
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

    if (editingGroup != null && draft != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissEditor,
        ) {
            AniListSectionEditor(
                group = editingGroup,
                draft = draft,
                onDraftChange = onDraftChange,
                onDismiss = onDismissEditor,
                onSave = {
                    onSaveDraft(draft)
                },
            )
        }
    }
}

@Composable
private fun AniListSectionEditor(
    group: AniListSettingsGroup,
    draft: AniListSettingsEditorState,
    onDraftChange: (AniListSettingsEditorState) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = group.title(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = group.description(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        when (group) {
            AniListSettingsGroup.Profile -> {
                OutlinedTextField(
                    value = draft.about,
                    onValueChange = { onDraftChange(draft.copy(about = it)) },
                    label = { Text(stringResource(R.string.label_settings_anilist_profile_about)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = draft.profileColor,
                    onValueChange = { onDraftChange(draft.copy(profileColor = it)) },
                    label = { Text(stringResource(R.string.label_settings_anilist_profile_color)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                )
            }

            AniListSettingsGroup.General -> {
                SettingsValueRow(
                    title = stringResource(R.string.label_settings_anilist_title_language),
                    summary = stringResource(R.string.summary_settings_anilist_cycle_option),
                    icon = Icons.Outlined.Edit,
                    currentValue = draft.titleLanguage.alias.toString(),
                    onClick = {
                        onDraftChange(
                            draft.copy(
                                titleLanguage = draft.titleLanguage.nextEnum(),
                            ),
                        )
                    },
                )
                SettingsValueRow(
                    title = stringResource(R.string.label_settings_anilist_staff_name_language),
                    summary = stringResource(R.string.summary_settings_anilist_cycle_option),
                    icon = Icons.Outlined.Edit,
                    currentValue = draft.staffNameLanguage.alias.toString(),
                    onClick = {
                        onDraftChange(
                            draft.copy(
                                staffNameLanguage = draft.staffNameLanguage.nextEnum(),
                            ),
                        )
                    },
                )
                SettingsToggleRow(
                    title = stringResource(R.string.label_settings_anilist_display_adult_content),
                    summary = null,
                    icon = Icons.Outlined.Edit,
                    checked = draft.displayAdultContent,
                    onCheckedChange = { onDraftChange(draft.copy(displayAdultContent = it)) },
                )
                OutlinedTextField(
                    value = draft.timeZone,
                    onValueChange = { updatedTimeZone ->
                        onDraftChange(draft.copy(timeZone = updatedTimeZone.take(5).filter { it.isDigit() || it == ':' }))
                    },
                    label = { Text(stringResource(R.string.label_settings_anilist_timezone)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = timeZoneValidationError(draft.timeZone) != null,
                    supportingText = {
                        Text(
                            text =
                                timeZoneValidationError(draft.timeZone)
                                    ?: stringResource(R.string.summary_settings_anilist_timezone_format),
                        )
                    },
                )
            }

            AniListSettingsGroup.Notifications -> {
                SettingsToggleRow(
                    title = stringResource(R.string.label_settings_anilist_airing_notifications),
                    summary = null,
                    icon = Icons.Outlined.Edit,
                    checked = draft.airingNotifications,
                    onCheckedChange = { onDraftChange(draft.copy(airingNotifications = it)) },
                )
                NotificationType.values().forEach { type ->
                    val current = draft.notificationOptions.firstOrNull { it.type == type }?.enabled ?: false
                    SettingsToggleRow(
                        title = type.label(),
                        summary = null,
                        icon = Icons.Outlined.Edit,
                        checked = current,
                        onCheckedChange = { enabled ->
                            val updated =
                                draft.notificationOptions
                                    .filterNot { it.type == type }
                                    .plus(
                                        UserParam.Update.NotificationOption(
                                            enabled = enabled,
                                            type = type,
                                        ),
                                    ).sortedBy { option -> option.type.name }
                            onDraftChange(draft.copy(notificationOptions = updated))
                        },
                    )
                }
            }

            AniListSettingsGroup.AnimeList,
            AniListSettingsGroup.MangaList,
            -> {
                val currentOptions =
                    if (group == AniListSettingsGroup.AnimeList) {
                        draft.animeListOptions
                    } else {
                        draft.mangaListOptions
                    }

                val onOptionsChanged: (AniListSettingsEditorState.MediaListOptions) -> Unit = { updatedOptions ->
                    if (group == AniListSettingsGroup.AnimeList) {
                        onDraftChange(draft.copy(animeListOptions = updatedOptions))
                    } else {
                        onDraftChange(draft.copy(mangaListOptions = updatedOptions))
                    }
                }

                val scoreLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_score_format
                    } else {
                        R.string.label_settings_anilist_manga_score_format
                    }
                val rowOrderLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_row_order
                    } else {
                        R.string.label_settings_anilist_manga_row_order
                    }
                val sectionOrderLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_section_order
                    } else {
                        R.string.label_settings_anilist_manga_section_order
                    }
                val customListsLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_custom_lists
                    } else {
                        R.string.label_settings_anilist_manga_custom_lists
                    }
                val advancedScoringLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_advanced_scoring
                    } else {
                        R.string.label_settings_anilist_manga_advanced_scoring
                    }
                val splitCompletedLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_split_completed_section
                    } else {
                        R.string.label_settings_anilist_manga_split_completed_section
                    }
                val advancedEnabledLabel =
                    if (group == AniListSettingsGroup.AnimeList) {
                        R.string.label_settings_anilist_anime_advanced_scoring_enabled
                    } else {
                        R.string.label_settings_anilist_manga_advanced_scoring_enabled
                    }

                val sectionOrderError = tokenValidationError(rawValue = currentOptions.sectionOrder)
                val customListsError = tokenValidationError(rawValue = currentOptions.customLists)
                val advancedScoringError = tokenValidationError(rawValue = currentOptions.advancedScoring)
                val hasListValidationError =
                    sectionOrderError != null || customListsError != null || advancedScoringError != null

                SettingsValueRow(
                    title = stringResource(scoreLabel),
                    summary = stringResource(R.string.summary_settings_anilist_cycle_option),
                    icon = Icons.Outlined.Edit,
                    currentValue = draft.scoreFormat.alias.toString(),
                    onClick = {
                        onDraftChange(draft.copy(scoreFormat = draft.scoreFormat.nextEnum()))
                    },
                )
                OutlinedTextField(
                    value = draft.rowOrder,
                    onValueChange = { onDraftChange(draft.copy(rowOrder = it)) },
                    label = { Text(stringResource(rowOrderLabel)) },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                )
                SettingsTokenListEditorRow(
                    title = stringResource(sectionOrderLabel),
                    rawValue = currentOptions.sectionOrder,
                    errorMessage = sectionOrderError,
                    onRawValueChange = {
                        onOptionsChanged(currentOptions.copy(sectionOrder = it))
                    },
                    modifier = Modifier.padding(top = 12.dp),
                )
                SettingsTokenListEditorRow(
                    title = stringResource(customListsLabel),
                    rawValue = currentOptions.customLists,
                    errorMessage = customListsError,
                    onRawValueChange = {
                        onOptionsChanged(currentOptions.copy(customLists = it))
                    },
                    modifier = Modifier.padding(top = 12.dp),
                )
                SettingsTokenListEditorRow(
                    title = stringResource(advancedScoringLabel),
                    rawValue = currentOptions.advancedScoring,
                    errorMessage = advancedScoringError,
                    onRawValueChange = {
                        onOptionsChanged(currentOptions.copy(advancedScoring = it))
                    },
                    modifier = Modifier.padding(top = 12.dp),
                )
                SettingsToggleRow(
                    title = stringResource(splitCompletedLabel),
                    summary = null,
                    icon = Icons.Outlined.Edit,
                    checked = currentOptions.splitCompletedSectionByFormat,
                    onCheckedChange = {
                        onOptionsChanged(currentOptions.copy(splitCompletedSectionByFormat = it))
                    },
                )
                SettingsToggleRow(
                    title = stringResource(advancedEnabledLabel),
                    summary = null,
                    icon = Icons.Outlined.Edit,
                    checked = currentOptions.advancedScoringEnabled,
                    onCheckedChange = {
                        onOptionsChanged(currentOptions.copy(advancedScoringEnabled = it))
                    },
                )

                if (hasListValidationError) {
                    Text(
                        text = stringResource(R.string.error_settings_anilist_fix_list_items),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                val canSave = !hasListValidationError

                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                    TextButton(
                        onClick = onSave,
                        enabled = canSave,
                    ) {
                        Text(text = stringResource(R.string.action_settings_anilist_save_changes))
                    }
                }

                return
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.End,
        ) {
            val canSave = group != AniListSettingsGroup.General || timeZoneValidationError(draft.timeZone) == null
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(android.R.string.cancel))
            }
            TextButton(onClick = onSave, enabled = canSave) {
                Text(text = stringResource(R.string.action_settings_anilist_save_changes))
            }
        }
    }
}

@Composable
private fun SettingsTokenListEditorRow(
    title: String,
    rawValue: String,
    errorMessage: String?,
    onRawValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = rawValue.toTokenList()
    var pendingToken by rememberSaveable(title) { mutableStateOf("") }
    val normalizedPending = pendingToken.trim()
    val canAdd = normalizedPending.isNotEmpty() && normalizedPending !in tokens

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (tokens.isEmpty()) {
            Text(
                text = stringResource(R.string.summary_settings_anilist_no_items),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tokens.forEach { token ->
                    FilterChip(
                        selected = true,
                        onClick = {
                            val updated = tokens.toMutableList().apply { remove(token) }
                            onRawValueChange(updated.toTokenCsv())
                        },
                        label = {
                            Text(text = "$token  ×")
                        },
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = pendingToken,
                onValueChange = { pendingToken = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text(text = stringResource(R.string.label_settings_anilist_list_item_input)) },
            )
            TextButton(
                onClick = {
                    if (!canAdd) {
                        return@TextButton
                    }
                    onRawValueChange((tokens + normalizedPending).toTokenCsv())
                    pendingToken = ""
                },
                enabled = canAdd,
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(text = stringResource(R.string.action_settings_anilist_add_item))
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
            )
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
        when {
            entry.entry == AniListSettingsEntry.ProfileAbout -> {
                MarkdownText(
                    content = entry.value,
                    maxLines = 4,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            entry.entry.isTokenizedPreviewEntry() -> {
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    entry.value.toTokenList().forEach { token ->
                        AssistChip(
                            onClick = {},
                            enabled = false,
                            label = { Text(text = token) },
                        )
                    }
                }
            }

            else -> {
                Text(
                    text = entry.value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

private fun NotificationType.label(): String = alias.toString()

private inline fun <reified T : Enum<T>> T.nextEnum(): T {
    val values = enumValues<T>()
    val currentIndex = values.indexOf(this)
    val nextIndex = (currentIndex + 1) % values.size
    return values[nextIndex]
}

private fun UserTitleLanguage.nextEnum(): UserTitleLanguage = nextEnum<UserTitleLanguage>()

private fun UserStaffNameLanguage.nextEnum(): UserStaffNameLanguage = nextEnum<UserStaffNameLanguage>()

private fun String.toTokenList(): List<String> =
    split(',')
        .map(String::trim)
        .filter(String::isNotBlank)

private fun List<String>.toTokenCsv(): String = joinToString(separator = ", ")

private fun AniListSettingsEntry.isTokenizedPreviewEntry(): Boolean =
    this == AniListSettingsEntry.NotificationOptions ||
        this == AniListSettingsEntry.AnimeSectionOrder ||
        this == AniListSettingsEntry.AnimeCustomLists ||
        this == AniListSettingsEntry.AnimeAdvancedScoring ||
        this == AniListSettingsEntry.MangaSectionOrder ||
        this == AniListSettingsEntry.MangaCustomLists ||
        this == AniListSettingsEntry.MangaAdvancedScoring

@Composable
private fun timeZoneValidationError(rawValue: String): String? {
    if (rawValue.isBlank()) {
        return null
    }
    val isValid = rawValue.matches(Regex("^([01]\\d|2[0-3]):[0-5]\\d$"))
    return if (isValid) {
        null
    } else {
        stringResource(R.string.error_settings_anilist_invalid_timezone)
    }
}

@Composable
private fun tokenValidationError(rawValue: String): String? {
    val hasBlankSegments = rawValue.split(',').any { it.isBlank() }
    if (hasBlankSegments) {
        return stringResource(R.string.error_settings_anilist_empty_item)
    }

    val duplicateExists =
        rawValue
            .toTokenList()
            .groupingBy { it }
            .eachCount()
            .any { it.value > 1 }

    return if (duplicateExists) {
        stringResource(R.string.error_settings_anilist_duplicate_item)
    } else {
        null
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
            editingGroup = null,
            draft = null,
            onEditGroup = {},
            onDismissEditor = {},
            onDraftChange = {},
            onSaveDraft = {},
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
            editingGroup = null,
            draft = null,
            onEditGroup = {},
            onDismissEditor = {},
            onDraftChange = {},
            onSaveDraft = {},
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

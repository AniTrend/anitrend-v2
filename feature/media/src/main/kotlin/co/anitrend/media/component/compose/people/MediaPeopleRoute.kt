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
package co.anitrend.media.component.compose.people

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.PagedList
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.media.R
import co.anitrend.media.component.viewmodel.MediaCharactersViewModel
import co.anitrend.media.component.viewmodel.MediaStaffViewModel
import co.anitrend.navigation.MediaPeopleRouter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaPeopleRoute(
    mediaId: Long,
    initialSection: MediaPeopleRouter.Section,
    onBackPress: () -> Unit,
    charactersViewModel: MediaCharactersViewModel = koinViewModel(),
    staffViewModel: MediaStaffViewModel = koinViewModel(),
) {
    val characters by charactersViewModel.model.observeAsState()
    val charactersLoadState by charactersViewModel.loadState.observeAsState()
    val staff by staffViewModel.model.observeAsState()
    val staffLoadState by staffViewModel.loadState.observeAsState()

    var selectedSection by rememberSaveable(mediaId) { mutableStateOf(initialSection) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(mediaId) {
        charactersViewModel(mediaId)
        staffViewModel(mediaId)
    }

    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.title_media_people_screen),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.subtitle_media_people_screen),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            MediaPeopleSegmentedControl(
                selected = selectedSection,
                onSelect = { selectedSection = it },
            )

            Box(modifier = Modifier.weight(1f)) {
                when (selectedSection) {
                    MediaPeopleRouter.Section.CHARACTERS ->
                        CharactersPane(
                            characters = characters,
                            loadState = charactersLoadState,
                            onRetry = { coroutineScope.launch { charactersViewModel.retry() } },
                        )

                    MediaPeopleRouter.Section.STAFF ->
                        StaffPane(
                            staff = staff,
                            loadState = staffLoadState,
                            onRetry = { coroutineScope.launch { staffViewModel.retry() } },
                        )
                }
            }
        }
    }
}

@Composable
private fun MediaPeopleSegmentedControl(
    selected: MediaPeopleRouter.Section,
    onSelect: (MediaPeopleRouter.Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape =
            androidx.compose.foundation.shape
                .RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                MediaPeopleRouter.Section.CHARACTERS to stringResource(R.string.label_media_people_characters_heading),
                MediaPeopleRouter.Section.STAFF to stringResource(R.string.label_media_people_staff_heading),
            ).forEach { (section, label) ->
                val isSelected = section == selected
                Surface(
                    modifier = Modifier.weight(1f),
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                        },
                    contentColor =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    shape =
                        androidx.compose.foundation.shape
                            .RoundedCornerShape(20.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    onClick = { onSelect(section) },
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharactersPane(
    characters: PagedList<MediaPerson.Character>?,
    loadState: LoadState?,
    onRetry: () -> Unit,
) {
    when {
        characters != null && characters.size > 0 -> {
            CharacterGrid(characters = characters)
        }
        loadState is LoadState.Loading || (characters == null && loadState !is LoadState.Error) -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_characters_loading),
                subtitle = stringResource(R.string.message_media_people_characters_loading),
            )
        }
        loadState is LoadState.Error -> {
            RetryPeopleState(
                title = stringResource(R.string.label_media_people_characters_error_title),
                onRetry = onRetry,
            )
        }
        else -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_characters_empty_title),
                subtitle = stringResource(R.string.message_media_people_characters_empty),
            )
        }
    }
}

@Composable
private fun StaffPane(
    staff: PagedList<MediaPerson.Staff>?,
    loadState: LoadState?,
    onRetry: () -> Unit,
) {
    when {
        staff != null && staff.size > 0 -> {
            StaffList(staff = staff)
        }
        loadState is LoadState.Loading || (staff == null && loadState !is LoadState.Error) -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_staff_loading),
                subtitle = stringResource(R.string.message_media_people_staff_loading),
            )
        }
        loadState is LoadState.Error -> {
            RetryPeopleState(
                title = stringResource(R.string.label_media_people_staff_error_title),
                onRetry = onRetry,
            )
        }
        else -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_staff_empty_title),
                subtitle = stringResource(R.string.message_media_people_staff_empty),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterGrid(
    characters: PagedList<MediaPerson.Character>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 148.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(characters.size) { index ->
            val item = characters[index] ?: return@items
            CharacterPreviewCard(
                item = item,
                showVoiceActor = true,
            )
        }
    }
}

@Composable
private fun StaffList(
    staff: PagedList<MediaPerson.Staff>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(staff.size) { index ->
            val item = staff[index] ?: return@items
            StaffPreviewListItem(
                item = item,
                showLanguage = true,
            )
        }
    }
}

@Composable
private fun CenteredPeopleState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RetryPeopleState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            androidx.compose.material3.OutlinedButton(
                onClick = onRetry,
                shape =
                    androidx.compose.foundation.shape
                        .RoundedCornerShape(20.dp),
            ) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}

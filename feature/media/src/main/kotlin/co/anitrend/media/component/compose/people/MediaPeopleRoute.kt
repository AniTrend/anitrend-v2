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

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState as PagingLoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.MediaSectionSegmentedControl
import co.anitrend.media.component.compose.section.groupStaffByRoleBucket
import co.anitrend.media.component.viewmodel.MediaCharactersViewModel
import co.anitrend.media.component.viewmodel.MediaStaffViewModel
import co.anitrend.navigation.MediaPeopleRouter
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaPeopleRoute(
    mediaId: Long,
    mediaTitle: String?,
    selectedSection: MediaPeopleRouter.Section,
    onBackPress: () -> Unit,
    onSelectSection: ((MediaPeopleRouter.Section) -> Unit)? = null,
    showSegmentedControl: Boolean = onSelectSection != null,
    charactersViewModel: MediaCharactersViewModel = koinViewModel(),
    staffViewModel: MediaStaffViewModel = koinViewModel(),
) {
    val characters = remember(mediaId) { charactersViewModel.characters(mediaId) }.collectAsLazyPagingItems()
    val staff = remember(mediaId) { staffViewModel.staff(mediaId) }.collectAsLazyPagingItems()

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
                    text =
                        when (selectedSection) {
                            MediaPeopleRouter.Section.CHARACTERS -> stringResource(R.string.label_media_people_characters_heading)
                            MediaPeopleRouter.Section.STAFF -> stringResource(R.string.label_media_people_staff_heading)
                        },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                if (!mediaTitle.isNullOrBlank()) {
                    Text(
                        text = mediaTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Text(
                    text = stringResource(R.string.subtitle_media_people_screen),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (showSegmentedControl && onSelectSection != null) {
                MediaSectionSegmentedControl(
                    selected = selectedSection,
                    options =
                        listOf(
                            MediaPeopleRouter.Section.CHARACTERS to stringResource(R.string.label_media_people_characters_heading),
                            MediaPeopleRouter.Section.STAFF to stringResource(R.string.label_media_people_staff_heading),
                        ),
                    onSelect = { section ->
                        if (section != selectedSection) {
                            onSelectSection(section)
                        }
                    },
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when (selectedSection) {
                    MediaPeopleRouter.Section.CHARACTERS ->
                        CharactersPane(
                            characters = characters,
                            onRetry = characters::retry,
                        )

                    MediaPeopleRouter.Section.STAFF ->
                        StaffPane(
                            staff = staff,
                            onRetry = staff::retry,
                        )
                }
            }
        }
    }
}

@Composable
private fun CharactersPane(
    characters: LazyPagingItems<MediaPerson.Character>,
    onRetry: () -> Unit,
) {
    val refreshState = characters.loadState.refresh

    when {
        characters.itemCount > 0 -> {
            CharacterGrid(characters = characters)
        }
        refreshState is PagingLoadState.Loading -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_characters_loading),
                subtitle = stringResource(R.string.message_media_people_characters_loading),
            )
        }
        refreshState is PagingLoadState.Error -> {
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
    staff: LazyPagingItems<MediaPerson.Staff>,
    onRetry: () -> Unit,
) {
    val refreshState = staff.loadState.refresh

    when {
        staff.itemCount > 0 -> {
            StaffList(staff = staff)
        }
        refreshState is PagingLoadState.Loading -> {
            CenteredPeopleState(
                title = stringResource(R.string.label_media_people_staff_loading),
                subtitle = stringResource(R.string.message_media_people_staff_loading),
            )
        }
        refreshState is PagingLoadState.Error -> {
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
    characters: LazyPagingItems<MediaPerson.Character>,
    modifier: Modifier = Modifier,
) {
    val sections =
        characters
            .itemSnapshotList
            .items
            .mapIndexed { index, item ->
                IndexedCharacterEntry(
                    pagingIndex = index,
                    item = item,
                )
            }.groupedCharacterSections()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 148.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        sections.forEach { section ->
            item(
                key = section.titleRes,
                span = { GridItemSpan(maxLineSpan) },
            ) {
                PeopleRoleSectionHeader(
                    title = stringResource(section.titleRes),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            items(
                count = section.characters.size,
                key = { index -> section.characters[index].item.id },
            ) { index ->
                val entry = section.characters[index]
                val item = characters[entry.pagingIndex] ?: entry.item

                CharacterPreviewCard(
                    item = item,
                    showVoiceActor = true,
                )
            }
        }

        when (characters.loadState.append) {
            is PagingLoadState.Loading -> {
                item(
                    key = "media_people_characters_append_loading",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "media_people_characters_append_loading",
                ) {
                    PeopleAppendLoadingText(
                        text = stringResource(R.string.message_media_people_characters_loading),
                    )
                }
            }

            is PagingLoadState.Error -> {
                item(
                    key = "media_people_characters_append_error",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "media_people_characters_append_error",
                ) {
                    AppendRetryPeopleState(
                        title = stringResource(R.string.label_media_people_characters_error_title),
                        onRetry = characters::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun StaffList(
    staff: LazyPagingItems<MediaPerson.Staff>,
    modifier: Modifier = Modifier,
) {
    val sections =
        staff
            .itemSnapshotList
            .items
            .mapIndexed { index, item ->
                IndexedStaffEntry(
                    pagingIndex = index,
                    item = item,
                )
            }.groupStaffByRoleBucket()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        sections.forEach { section ->
            item(key = section.group.name) {
                PeopleRoleSectionHeader(
                    title = stringResource(section.group.titleRes),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            items(
                count = section.staff.size,
                key = { index -> section.staff[index].item.id },
            ) { index ->
                val entry = section.staff[index]
                val item = staff[entry.pagingIndex] ?: entry.item

                StaffPreviewListItem(
                    item = item,
                    showLanguage = true,
                )
            }
        }

        when (staff.loadState.append) {
            is PagingLoadState.Loading -> {
                item(
                    key = "media_people_staff_append_loading",
                    contentType = "media_people_staff_append_loading",
                ) {
                    PeopleAppendLoadingText(
                        text = stringResource(R.string.message_media_people_staff_loading),
                    )
                }
            }

            is PagingLoadState.Error -> {
                item(
                    key = "media_people_staff_append_error",
                    contentType = "media_people_staff_append_error",
                ) {
                    AppendRetryPeopleState(
                        title = stringResource(R.string.label_media_people_staff_error_title),
                        onRetry = staff::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun PeopleAppendLoadingText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(vertical = 12.dp),
    )
}

@Composable
private fun AppendRetryPeopleState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
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

@AniTrendPreview.Default
@Composable
private fun RetryPeopleStatePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        RetryPeopleState(
            title = stringResource(R.string.label_media_people_characters_error_title),
            onRetry = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun CenteredPeopleStatePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        CenteredPeopleState(
            title = stringResource(R.string.label_media_people_staff_empty_title),
            subtitle = stringResource(R.string.message_media_people_staff_empty),
        )
    }
}

private data class IndexedCharacterEntry(
    val pagingIndex: Int,
    val item: MediaPerson.Character,
)

private data class IndexedCharacterSection(
    @param:StringRes val titleRes: Int,
    val characters: List<IndexedCharacterEntry>,
)

private data class IndexedStaffEntry(
    val pagingIndex: Int,
    val item: MediaPerson.Staff,
)

private data class IndexedStaffSection(
    val group: co.anitrend.media.component.compose.section.MediaStaffRoleGroup,
    val staff: List<IndexedStaffEntry>,
)

private fun List<IndexedCharacterEntry>.groupedCharacterSections(): List<IndexedCharacterSection> {
    if (isEmpty()) {
        return emptyList()
    }

    return buildList {
        listOf(
            co.anitrend.domain.character.enums.CharacterRole.MAIN to R.string.label_media_people_characters_group_main,
            co.anitrend.domain.character.enums.CharacterRole.SUPPORTING to R.string.label_media_people_characters_group_supporting,
            co.anitrend.domain.character.enums.CharacterRole.BACKGROUND to R.string.label_media_people_characters_group_background,
        ).forEach { (role, titleRes) ->
            val items = this@groupedCharacterSections.filter { character -> character.item.role == role }
            if (items.isNotEmpty()) {
                add(IndexedCharacterSection(titleRes = titleRes, characters = items))
            }
        }

        val unclassified = this@groupedCharacterSections.filter { character -> character.item.role == null }
        if (unclassified.isNotEmpty()) {
            add(
                IndexedCharacterSection(
                    titleRes = R.string.label_media_people_characters_group_background,
                    characters = unclassified,
                ),
            )
        }
    }
}

private fun List<IndexedStaffEntry>.groupStaffByRoleBucket(): List<IndexedStaffSection> =
    groupStaffByRoleBucket(
        map(IndexedStaffEntry::item),
    ).map { section ->
        val sectionIds = section.staff.mapTo(linkedSetOf(), MediaPerson.Staff::id)

        IndexedStaffSection(
            group = section.group,
            staff =
                this.filter { entry ->
                    entry.item.id in sectionIds
                },
        )
    }

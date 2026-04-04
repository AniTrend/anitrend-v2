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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagedList
import coil.compose.AsyncImage
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.MediaHubSection
import co.anitrend.media.component.compose.section.MediaHubSectionEmptyState
import co.anitrend.media.component.compose.section.MediaHubSectionErrorState
import co.anitrend.media.component.compose.section.MediaHubSectionLoadingState
import kotlin.math.min

private const val CHARACTER_PREVIEW_COUNT = 6
private const val STAFF_PREVIEW_COUNT = 4

internal fun <T : Any> PagedList<T>.asItems(limit: Int = size): List<T> {
    val end = min(size, limit)
    return (0 until end).mapNotNull(::get)
}

private fun LoadState?.isLoading() = this is LoadState.Loading

private fun LoadState?.isError() = this is LoadState.Error

@Composable
fun MediaPeopleSection(
    characters: PagedList<MediaPerson.Character>?,
    charactersLoadState: LoadState?,
    staff: PagedList<MediaPerson.Staff>?,
    staffLoadState: LoadState?,
    onSeeAllClick: () -> Unit,
    onCharacterClick: (MediaPerson.Character) -> Unit,
    onStaffClick: (MediaPerson.Staff) -> Unit,
    onRetryCharacters: () -> Unit,
    onRetryStaff: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewCharacters = characters?.asItems(CHARACTER_PREVIEW_COUNT).orEmpty()
    val previewStaff = staff?.asItems(STAFF_PREVIEW_COUNT).orEmpty()

    MediaHubSection(
        title = stringResource(R.string.title_media_people_section),
        subtitle = stringResource(R.string.subtitle_media_people_section),
        trailingActionLabel = stringResource(R.string.action_media_people_section_see_all),
        onTrailingAction = onSeeAllClick,
        modifier = modifier,
    ) {
        PeopleSubsectionTitle(
            title = stringResource(R.string.label_media_people_characters_heading),
        )

        when {
            previewCharacters.isNotEmpty() -> {
                CharacterPreviewRail(
                    items = previewCharacters,
                    onItemClick = onCharacterClick,
                )
            }
            charactersLoadState.isLoading() || (characters == null && !charactersLoadState.isError()) -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_people_characters_loading),
                )
            }
            charactersLoadState.isError() -> {
                PeopleSubsectionErrorState(
                    title = stringResource(R.string.label_media_people_characters_error_title),
                    onRetry = onRetryCharacters,
                )
            }
            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_people_characters_empty_title),
                    message = stringResource(R.string.message_media_people_characters_empty),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        PeopleSubsectionTitle(
            title = stringResource(R.string.label_media_people_staff_heading),
        )

        when {
            previewStaff.isNotEmpty() -> {
                StaffPreviewList(
                    items = previewStaff,
                    onItemClick = onStaffClick,
                )
            }
            staffLoadState.isLoading() || (staff == null && !staffLoadState.isError()) -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_people_staff_loading),
                )
            }
            staffLoadState.isError() -> {
                PeopleSubsectionErrorState(
                    title = stringResource(R.string.label_media_people_staff_error_title),
                    onRetry = onRetryStaff,
                )
            }
            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_people_staff_empty_title),
                    message = stringResource(R.string.message_media_people_staff_empty),
                )
            }
        }
    }
}

@Composable
private fun PeopleSubsectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun PeopleSubsectionErrorState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MediaHubSectionErrorState(title = title)
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
        }
    }
}

@Composable
fun CharacterPreviewRail(
    items: List<MediaPerson.Character>,
    onItemClick: (MediaPerson.Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(end = 4.dp),
    ) {
        items(items.size) { index ->
            CharacterPreviewCard(
                item = items[index],
                onClick = { onItemClick(items[index]) },
            )
        }
    }
}

@Composable
fun StaffPreviewList(
    items: List<MediaPerson.Staff>,
    onItemClick: (MediaPerson.Staff) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { item ->
            StaffPreviewListItem(
                item = item,
                onClick = { onItemClick(item) },
            )
        }
    }
}

@Composable
internal fun CharacterPreviewCard(
    item: MediaPerson.Character,
    modifier: Modifier = Modifier,
    showVoiceActor: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val primaryName = item.name?.userPreferred ?: item.name?.full ?: item.mediaRoleName.orEmpty()
    val roleLabel = item.role?.alias?.toString()
    val voiceActor = item.voiceActors.firstOrNull()?.name?.userPreferred ?: item.voiceActors.firstOrNull()?.name?.full

    Surface(
        modifier =
            modifier
                .width(138.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AsyncImage(
                model = item.image?.large ?: item.image?.medium,
                contentDescription = primaryName,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.84f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = primaryName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!roleLabel.isNullOrBlank()) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Text(
                            text = roleLabel,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }
                if (showVoiceActor && !voiceActor.isNullOrBlank()) {
                    Text(
                        text = voiceActor,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
internal fun StaffPreviewListItem(
    item: MediaPerson.Staff,
    modifier: Modifier = Modifier,
    showLanguage: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val displayName = item.name?.userPreferred ?: item.name?.full.orEmpty()
    val roleLabel = item.role ?: stringResource(R.string.label_media_people_staff_role_unknown)
    val language = item.language

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.image?.medium != null || item.image?.large != null) {
                AsyncImage(
                    model = item.image?.medium ?: item.image?.large,
                    contentDescription = displayName,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = roleLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (showLanguage && language != null) {
                    Text(
                        text = language.alias.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

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
package co.anitrend.media.component.compose.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.media.R

private data class MetadataEntry(
    val label: String,
    val value: String,
)

private fun MediaTheme.toLabel(): String {
    val themeMeta =
        meta?.let {
            buildString {
                append(it.type.uppercase())
                append(' ')
                append(it.number)
                if (it.version > 1) {
                    append(" v")
                    append(it.version)
                }
            }
        }

    return listOf(name, themeMeta).filter { it?.isNotBlank() == true }.joinToString(" • ")
}

private fun IMediaSourceId.toLabels(): List<String> =
    buildList {
        aniList?.let { add("AniList $it") }
        myAnimeList?.let { add("MyAnimeList $it") }
        aniDb?.let { add("AniDB $it") }
        aniSearch?.let { add("AniSearch $it") }
        animePlanet?.takeIf(String::isNotBlank)?.let { add("Anime-Planet $it") }
        imdb?.takeIf(String::isNotBlank)?.let { add("IMDb $it") }
        kitsu?.let { add("Kitsu $it") }
        liveChart?.let { add("LiveChart $it") }
        notify?.takeIf(String::isNotBlank)?.let { add("Notify $it") }
        shoboi?.let { add("Shoboi $it") }
        slug?.takeIf(String::isNotBlank)?.let { add("Slug $it") }
        tmdb?.let { add("TMDb $it") }
        trakt?.let { add("Trakt $it") }
        tvDb?.let { add("TVDb $it") }
        tvMaze?.let { add("TVMaze $it") }
        tvRage?.takeIf(String::isNotBlank)?.let { add("TVRage $it") }
    }

@Composable
private fun MetadataRow(
    entry: MetadataEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = entry.label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.36f),
        )
        Text(
            text = entry.value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.64f),
        )
    }
}

@Composable
private fun MetadataChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = CardDefaults.shape,
        modifier = modifier,
    ) {
        Text(
            text = label,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun MetadataGroup(
    title: String,
    values: List<String>,
    collapsedCount: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable(title, values.size) {
        mutableStateOf(false)
    }
    val canExpand = values.size > collapsedCount
    val visibleValues = if (canExpand && !isExpanded) values.take(collapsedCount) else values

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (canExpand) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                ) {
                    Text(
                        text =
                            stringResource(
                                if (isExpanded) {
                                    R.string.action_media_extended_details_show_less
                                } else {
                                    R.string.action_media_extended_details_show_more
                                },
                            ),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            visibleValues.forEach { value ->
                MetadataChip(label = value)
            }
        }
    }
}

@Composable
fun MediaExtendedMetadataSection(
    media: Media.Extended,
    showExternalIdentifiers: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val detailRows =
        buildList {
            media.ageRating
                ?.takeIf(String::isNotBlank)
                ?.let { add(MetadataEntry(stringResource(R.string.label_media_extended_details_age_rating), it)) }
            media.source
                ?.alias
                ?.toString()
                ?.takeIf(String::isNotBlank)
                ?.let { add(MetadataEntry(stringResource(R.string.label_media_extended_details_source), it)) }
            media.twitterTag
                ?.toString()
                ?.trim()
                ?.takeIf(String::isNotBlank)
                ?.let {
                    val hashtag = if (it.startsWith("#")) it else "#$it"
                    add(MetadataEntry(stringResource(R.string.label_media_extended_details_twitter_tag), hashtag))
                }
        }

    val synonyms =
        media.synonyms
            .map(CharSequence::toString)
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()

    val sourceIds = if (showExternalIdentifiers) media.sourceId.toLabels() else emptyList()
    val themes =
        media.themes
            .map(MediaTheme::toLabel)
            .filter(String::isNotBlank)
            .distinct()

    if (detailRows.isEmpty() && synonyms.isEmpty() && sourceIds.isEmpty() && themes.isEmpty()) {
        return
    }

    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            Text(
                text = stringResource(R.string.label_media_extended_details_section_title),
                style = MaterialTheme.typography.titleSmall,
            )

            detailRows.forEach { entry ->
                MetadataRow(entry = entry)
            }

            if (synonyms.isNotEmpty()) {
                MetadataGroup(
                    title = stringResource(R.string.label_media_extended_details_synonyms),
                    values = synonyms,
                    collapsedCount = 2,
                )
            }

            if (themes.isNotEmpty()) {
                MetadataGroup(
                    title = stringResource(R.string.label_media_extended_details_themes),
                    values = themes,
                    collapsedCount = 3,
                )
            }

            if (sourceIds.isNotEmpty()) {
                MetadataGroup(
                    title = stringResource(R.string.label_media_extended_details_external_ids),
                    values = sourceIds,
                    collapsedCount = 4,
                )
            }
        }
    }
}

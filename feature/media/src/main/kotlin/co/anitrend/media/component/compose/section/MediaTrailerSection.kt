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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.domain.media.entity.attribute.trailer.IMediaTrailer
import co.anitrend.media.R
import coil.compose.AsyncImage

private val TrailerCardWidth = 264.dp

@Composable
internal fun MediaTrailerSection(
    trailers: List<IMediaTrailer>,
    onTrailerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val playableTrailers = remember(trailers) { trailers.mapNotNull(::toPlayableTrailer).distinctBy(PlayableTrailer::key) }
    if (playableTrailers.isEmpty()) {
        return
    }

    MediaHubSection(
        title = stringResource(R.string.title_media_trailer_section),
        subtitle = stringResource(R.string.subtitle_media_trailer_section),
        modifier = modifier,
    ) {
        MediaSectionRail(contentPadding = PaddingValues(vertical = 2.dp)) {
            items(playableTrailers.size, key = { index -> playableTrailers[index].key }) { index ->
                val trailer = playableTrailers[index]
                TrailerCard(
                    trailer = trailer,
                    onClick = { onTrailerClick(trailer.url) },
                )
            }
        }
    }
}

@Composable
private fun TrailerCard(
    trailer: PlayableTrailer,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.width(TrailerCardWidth),
        shape = RoundedCornerShape(22.dp),
        color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        border =
            BorderStroke(
                1.dp,
                androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant
                    .copy(alpha = 0.28f),
            ),
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onClick).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color =
                    androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
                        .copy(alpha = 0.24f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    AsyncImage(
                        model = trailer.thumbnail,
                        contentDescription = trailer.source,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(148.dp),
                    )
                    Surface(
                        modifier = Modifier.padding(10.dp),
                        shape = RoundedCornerShape(999.dp),
                        color =
                            androidx.compose.material3.MaterialTheme.colorScheme.surface
                                .copy(alpha = 0.84f),
                        border =
                            BorderStroke(
                                1.dp,
                                androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant
                                    .copy(alpha = 0.28f),
                            ),
                    ) {
                        Text(
                            text = trailer.source,
                            style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = trailer.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = trailer.url,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                )
                androidx.compose.foundation.layout
                    .Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.action_media_trailer_watch))
            }
        }
    }
}

internal fun resolveTrailerUrl(trailer: IMediaTrailer): String? {
    val id =
        trailer.id
            ?.toString()
            ?.trim()
            ?.takeIf(String::isNotBlank) ?: return null
    val site =
        trailer.site
            ?.toString()
            ?.trim()
            ?.lowercase()

    return when (site) {
        "youtube", "youtube music", "youtu.be" -> "https://youtube.com/watch?v=$id"
        "dailymotion" -> "https://www.dailymotion.com/video/$id"
        "vimeo" -> "https://vimeo.com/$id"
        else -> id.takeIf { it.startsWith("https://") || it.startsWith("http://") }
    }
}

private fun toPlayableTrailer(trailer: IMediaTrailer): PlayableTrailer? {
    val url = resolveTrailerUrl(trailer) ?: return null
    val source =
        trailer.site
            ?.toString()
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?.replaceFirstChar(Char::titlecase) ?: "Trailer"
    return PlayableTrailer(
        key = "${source.lowercase()}:${trailer.id}",
        title = "$source trailer",
        source = source,
        thumbnail =
            trailer.thumbnail
                ?.toString()
                ?.trim()
                ?.takeIf(String::isNotBlank),
        url = url,
    )
}

private data class PlayableTrailer(
    val key: String,
    val title: String,
    val source: String,
    val thumbnail: String?,
    val url: String,
)

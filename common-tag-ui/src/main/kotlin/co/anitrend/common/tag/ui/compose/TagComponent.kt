package co.anitrend.common.tag.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
private fun TagItem(
    tag: Tag,
    accentColor: Color,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedSuggestionChip(
        onClick = {
            onMediaDiscoverableItemClick(
                MediaDiscoverRouter.MediaDiscoverParam(tag = tag.name),
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = accentColor,
        ),
        label = {
            Text(
                text = tag.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                style = AniTrendTheme.typography.caption,
            )
        },
        icon = {
            val icon = when (tag) {
                is Tag.Extended -> {
                    if (tag.isMediaSpoiler)
                        Icons.Rounded.Cancel
                    else null
                }
                else -> {
                    if (tag.isGeneralSpoiler)
                        Icons.Rounded.Warning
                    else null
                }
            } ?: Icons.Rounded.Tag
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        },
        modifier = modifier,
    )
}

@Composable
fun TagListItems(
    accentColor: Color,
    modifier: Modifier = Modifier,
    tags: List<Tag> = emptyList(),
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
) {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = tags.size,
            key = { tags[it].id },
            contentType = { "Tag" }
        ) { index ->
            TagItem(
                tag = tags[index],
                accentColor = accentColor,
                onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun TagComponentPreview() {
    PreviewTheme(wrapInSurface = true) {
        TagItem(
            tag = Tag.Extended(
                rank = 1,
                isMediaSpoiler = false,
                background = MaterialTheme.colorScheme.onSurface.toArgb().toHexString(),
                name = "Isekai",
                description = "",
                category = "",
                isGeneralSpoiler = true,
                isAdult = false,
                id = 0,
            ),
            accentColor = MaterialTheme.colorScheme.onSurface,
            onMediaDiscoverableItemClick = {},
        )
    }
}

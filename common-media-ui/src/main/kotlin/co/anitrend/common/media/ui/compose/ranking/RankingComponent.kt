package co.anitrend.common.media.ui.compose.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

@Composable
private fun RankingItem(
    accentColor: Color,
    ranking: IMediaRank,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedSuggestionChip(
        onClick = {
            val sorting = when (ranking.type) {
                MediaRankType.RATED -> Sorting(
                    sortable = MediaSort.SCORE,
                    order = SortOrder.DESC
                )
                else -> Sorting(
                    sortable = MediaSort.POPULARITY,
                    order = SortOrder.DESC
                )
            }
            onClick(ranking, listOf(sorting))
        },
        shape = SuggestionChipDefaults.shape,
        colors = SuggestionChipDefaults.elevatedSuggestionChipColors().copy(
            containerColor = accentColor,
        ),
        label = {
            val content = StringBuilder("#${ranking.rank} ${ranking.context}".capitalizeWords())
            if (ranking.season != null) {
                content.append(" $CHARACTER_SEPARATOR ${ranking.season?.alias}")
            }
            if (ranking.year != null) {
                content.append(" $CHARACTER_SEPARATOR ${ranking.year}")
            }
            content.append(" $CHARACTER_SEPARATOR ${ranking.format.alias}")

            Text(
                text = content.toString(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                style = AniTrendTheme.typography.caption,
            )
        },
        icon = {
            val resource = when (ranking.type) {
                MediaRankType.POPULAR -> Icons.AutoMirrored.Rounded.TrendingUp
                MediaRankType.RATED -> Icons.Rounded.StarRate
            }
            Icon(imageVector = resource, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
        },
        modifier = modifier,
    )
}


@Composable
fun RankingItems(
    accentColor: Color,
    modifier: Modifier = Modifier,
    rankings: List<IMediaRank> = emptyList(),
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(all = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = rankings.size,
            key = { rankings[it].id },
            contentType = { rankings[it].type }
        ) { index ->
            val ranking = rankings[index]
            RankingItem(
                accentColor = accentColor,
                ranking = ranking,
                onClick = onClick,
            )
        }
    }
}

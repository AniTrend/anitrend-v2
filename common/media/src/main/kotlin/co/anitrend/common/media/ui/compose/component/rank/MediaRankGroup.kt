package co.anitrend.common.media.ui.compose.component.rank

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

@Composable
private fun MediaRankGroupHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(co.anitrend.common.media.ui.R.drawable.ic_trophy),
            contentDescription = null,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            tint = colorResource(co.anitrend.android.core.R.color.orange_700)
        )
        Spacer(modifier = Modifier.padding(end = 16.dp))
        Text(
            text = "Rankings",
            modifier = Modifier.weight(1f)
                .align(alignment = Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun MediaRankGroup(
    ranks: List<IMediaRank>,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            MediaRankGroupHeader()
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            ranks.take(2).forEachIndexed { index, rank ->
                MediaRankItem(
                    rank = ranks[index],
                    onClick = onClick,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (ranks.size > 2) {
                TextButton(
                    onClick = {
                        // TODO: Create a bottom sheet for this? We'll probably have many similar use-case
                    },
                    content = {
                        Text(
                            text = "View all rankings",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaRankGroupPreview(
    @PreviewParameter(DarkThemeProvider ::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme,wrapInSurface = true) {
        MediaRankGroup(
            ranks = MediaRankPreviewProvider().values.toList(),
            onClick = { _, _ -> },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

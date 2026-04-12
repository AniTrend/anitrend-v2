package co.anitrend.common.episode.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.episode.entity.Episode

@Composable
fun EpisodeBrowseCard(
    episode: Episode,
    onClick: (Episode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val summary = remember(episode) { episode.summaryLabel() }
    val title = episode.series.seriesTitle.takeIf(String::isNotBlank) ?: episode.title
    val duration = episode.about.episodeDuration.takeIf(String::isNotBlank)

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(episode) },
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.78f),
        ) {
            AniTrendImage(
                image = episode.thumbnail,
                imageType = RequestImage.Media.ImageType.BANNER,
                modifier = Modifier.fillMaxSize(),
                onClick = { onClick(episode) },
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .padding(14.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                            shape = MaterialTheme.shapes.large,
                        ).padding(horizontal = 10.dp, vertical = 6.dp)
                        .align(Alignment.TopStart),
            )

            duration?.also {
                Row(
                    modifier =
                        Modifier
                            .padding(14.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                                shape = CircleShape,
                            ).padding(horizontal = 10.dp, vertical = 6.dp)
                            .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.scrim.copy(alpha = 0.82f),
                                    ),
                            ),
                        ).padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private fun Episode.summaryLabel(): String {
    val season = series.seriesSeason.orEmpty().padNumericToken(defaultValue = "--")
    val episodeNumber = about.episodeNumber.orEmpty().padNumericToken(defaultValue = "00")
    val episodeTitle = about.episodeTitle?.takeIf(String::isNotBlank) ?: title
    return "S${season}E${episodeNumber} • $episodeTitle"
}

private fun String.padNumericToken(defaultValue: String): String {
    if (isBlank()) {
        return defaultValue
    }

    return if (length < 2) {
        "0$this"
    } else {
        this
    }
}

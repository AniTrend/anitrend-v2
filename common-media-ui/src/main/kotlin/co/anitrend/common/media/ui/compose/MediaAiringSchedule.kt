package co.anitrend.common.media.ui.compose

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.widget.airing.controller.MediaAiringScheduleController
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media

@Composable
fun MediaAiringSchedule(
    category: Media.Category,
    modifier: Modifier = Modifier,
) {
    val controller = remember(category) { MediaAiringScheduleController(Media.Core.empty()) }
    Text(
        text = stringResource(R.string.label_episode_airing_in_time, category.total, "2 days from now"),
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier,
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaAiringSchedulePreview() {
    PreviewTheme {
        Surface {
            MediaAiringSchedule(
                category = Media.Category.Anime(
                    episodes = 12,
                    duration = 24,
                    broadcast = null,
                    premiered = null,
                    schedule = null
                )
            )
        }
    }
}

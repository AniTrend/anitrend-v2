package co.anitrend.common.media.ui.compose.component.score

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.media.entity.attribute.score.IMediaScore
import co.anitrend.domain.media.entity.attribute.score.MediaScore

internal data class MediaScoreSectionPreviewProvider(
    override val values: Sequence<IMediaScore> = sequenceOf(
        MediaScore(mean = 85, average = 86, personal = 9f, popularity = 4_700_000, trending = 9_000),
        MediaScore(mean = 39, average = 46, personal = 2.5f, popularity = 300_000, trending = 2_300),
        MediaScore(mean = 85, average = 86, personal = null, popularity = 20_000, trending = 2_300),
        MediaScore(mean = 20, average = 40, personal = null, popularity = null, trending = 2_300),
        MediaScore(mean = 5, average = 6, personal = null, popularity = null, trending = null),
    )
) : PreviewParameterProvider<IMediaScore>

package co.anitrend.common.media.ui.compose.component.rank

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason

internal class MediaRankPreviewProvider(
    override val values: Sequence<IMediaRank> = sequenceOf(
        MediaRank(
            id = 1L,
            allTime = true,
            context = "highest rated",
            format = MediaFormat.TV,
            rank = 16,
            season = null,
            type = MediaRankType.RATED,
            year = 2025,
        ),
        MediaRank(
            id = 2L,
            allTime = null,
            context = "most popular",
            format = MediaFormat.TV,
            rank = 36,
            season = MediaSeason.SUMMER,
            type = MediaRankType.POPULAR,
            year = 2025,
        ),
        MediaRank(
            id = 3L,
            allTime = null,
            context = "highest rated",
            format = MediaFormat.MUSIC,
            rank = 48,
            season = null,
            type = MediaRankType.RATED,
            year = null,
        ),
    ),
): PreviewParameterProvider<IMediaRank>

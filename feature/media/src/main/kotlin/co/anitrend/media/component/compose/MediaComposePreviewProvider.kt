package co.anitrend.media.component.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus

internal data class MediaComposePreviewProvider(
    override val values: Sequence<IMedia> = sequenceOf(
        Media.Extended.empty().copy(
            title =
                MediaTitle(
                    userPreferred = "Boku no Hero Academia 3",
                    english = "My Hero Academia Season 3",
                    romaji = "Boku no Hero Academia 3",
                    native = "僕のヒーローアカデミア 3",
                ),
            status = MediaStatus.FINISHED,
            image = MediaImage.empty().copy(color = "#e4a15d"),
            startDate = FuzzyDate.empty().copy(2018),
            format = MediaFormat.TV,
            category =
                Media.Category.Anime
                    .empty()
                    .copy(25),
            isFavourite = true,
            score =
                MediaScore(
                    average = 69,
                    mean = 70,
                    personal = null,
                    popularity = 4_000,
                    trending = 800,
                ),
            mediaList =
                MediaList.Core.empty().copy(
                    id = 100,
                    status = MediaListStatus.COMPLETED,
                    score = 8.3f,
                    privacy =
                        MediaListPrivacy(
                            isHidden = false,
                            isPrivate = false,
                            notes = "Good..",
                        ),
                ),
        )
    )
) : PreviewParameterProvider<IMedia>

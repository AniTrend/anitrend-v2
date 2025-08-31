package co.anitrend.data.edge.media.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle

/**
 * Converts Edge media entities to a minimal domain Media projection.
 * This stays within data layer boundaries and is injected where needed.
 */
internal class EdgeMediaEntityConverter : SupportConverter<EdgeMediaEntity, Media>() {
    override val fromType: (EdgeMediaEntity) -> Media = { entity ->
        Media.Core.empty().copy(
            id = entity.id.toLong(),
            title = MediaTitle(
                romaji = entity.title.romaji,
                english = entity.title.english,
                native = entity.title.native,
                userPreferred = null,
            ),
            image = MediaImage(
                color = entity.cover.color,
                extraLarge = entity.cover.extraLarge,
                large = entity.cover.large,
                medium = entity.cover.medium,
                banner = entity.bannerImage,
            ),
        )
    }

    override val toType: (Media) -> EdgeMediaEntity = { _ ->
        throw NotImplementedError()
    }
}

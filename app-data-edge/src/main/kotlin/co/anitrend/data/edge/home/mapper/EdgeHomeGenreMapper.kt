package co.anitrend.data.edge.home.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.home.entity.EdgeHomeEntity

internal class EdgeHomeGenreMapper(
    override val localSource: AbstractLocalSource<EdgeHomeEntity>
) : EmbedMapper<EdgeHomeGenreMapper.Item, EdgeHomeEntity>() {

    override val converter = object : SupportConverter<Item, EdgeHomeEntity>() {
        /**
         * Function reference from converting from [M] to [E] which will
         * be called by [convertFrom]
         */
        override val fromType: (Item) -> EdgeHomeEntity = {
            EdgeHomeEntity(
                name = it.name,
                id = it.id
            )
        }

        /**
         * Function reference from converting from [E] to [M] which will
         * be called by [convertTo]
         */
        override val toType: (EdgeHomeEntity) -> Item
            get() = throw NotImplementedError()
    }

    data class Item(
        val name: String,
        val configId: Long,
        val id: Long,
    )
}

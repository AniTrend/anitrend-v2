package co.anitrend.data.edge.news.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource
import co.anitrend.data.edge.news.entity.EdgeNewsEntity
import co.anitrend.data.edge.news.model.remote.EdgeNewsConnectionModel
import co.anitrend.domain.news.entity.News

internal class EdgeNewsMapper(
    private val localSource: EdgeNewsLocalSource,
    private val converter: EdgeNewsModelConverter,
) : DefaultMapper<EdgeNewsConnectionModel, List<EdgeNewsEntity>>() {
    override suspend fun onResponseMapFrom(source: EdgeNewsConnectionModel): List<EdgeNewsEntity> =
        converter.convertFrom(source.connection.data)

    override suspend fun persist(data: List<EdgeNewsEntity>) {
        localSource.upsert(data)
    }
}

internal class EdgeNewsModelConverter(
    override val fromType: (List<EdgeNewsConnectionModel.News>) -> List<EdgeNewsEntity> = { items ->
        items.map { item ->
            EdgeNewsEntity(
                cursor = item.id,
                newsId = item.id,
                title = item.title,
                url = item.link,
                image = item.image,
                source = item.author,
                publishedAt = item.publishedOn,
                description = item.description ?: item.content,
            )
        }
    },
    override val toType: (List<EdgeNewsEntity>) -> List<EdgeNewsConnectionModel.News> = { _ ->
        throw NotImplementedError()
    },
) : SupportConverter<List<EdgeNewsConnectionModel.News>, List<EdgeNewsEntity>>()

internal class EdgeNewsEntityConverter(
    override val fromType: (EdgeNewsEntity) -> News = { source ->
        News(
            id = source.newsId.hashCode().toLong(),
            guid = source.newsId,
            link = source.url,
            title = source.title,
            image = source.image,
            author = source.source ?: "",
            subTitle = source.description?.take(140) ?: "",
            description = source.description,
            content = source.description ?: "",
            publishedOn = source.publishedAt,
        )
    },
    override val toType: (News) -> EdgeNewsEntity = { _ -> throw NotImplementedError() },
) : SupportConverter<EdgeNewsEntity, News>()

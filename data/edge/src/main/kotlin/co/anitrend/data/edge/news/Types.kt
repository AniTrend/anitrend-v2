package co.anitrend.data.edge.news

import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.edge.news.entity.EdgeNewsEntity
import co.anitrend.data.edge.news.model.remote.EdgeNewsConnectionModel

internal typealias EdgeNewsController = GraphQLController<EdgeNewsConnectionModel, List<EdgeNewsEntity>>

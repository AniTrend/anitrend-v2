package co.anitrend.data.edge.media

import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel

internal typealias EdgeMediaController = GraphQLController<EdgeMediaModel, EdgeMediaEntity>

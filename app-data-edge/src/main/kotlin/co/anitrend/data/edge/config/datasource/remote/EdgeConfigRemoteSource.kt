package co.anitrend.data.edge.config.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.edge.config.model.remote.EdgeConfigModel
import co.anitrend.data.edge.core.api.factory.EdgeApiFactory
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface EdgeConfigRemoteSource {

    @GRAPHQL
    @GraphQuery("GetConfig")
    @POST(EdgeApiFactory.BASE_ENDPOINT_PATH)
    suspend fun getConfig(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<EdgeConfigModel>>
}

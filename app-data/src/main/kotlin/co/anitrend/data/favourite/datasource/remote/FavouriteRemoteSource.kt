package co.anitrend.data.favourite.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.model.GraphQLResponse
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface FavouriteRemoteSource {

    @GRAPHQL
    @POST
    @GraphQuery("ToggleAnimeFavorite")
    suspend fun toggleAnimeFavorite(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<Boolean>>
}

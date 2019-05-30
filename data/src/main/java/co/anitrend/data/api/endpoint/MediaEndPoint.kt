package co.anitrend.data.api.endpoint

import co.anitrend.data.model.response.collection.GenreCollection
import co.anitrend.data.model.response.collection.MediaTagCollection
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MediaEndPoint {

    @POST("/")
    @GraphQuery("genre-collection")
    fun getMediaGenres(@Body request: QueryContainerBuilder): Call<GraphContainer<GenreCollection>>

    @POST("/")
    @GraphQuery("media-tag-collection")
    fun getMediaTags(@Body request: QueryContainerBuilder): Call<GraphContainer<MediaTagCollection>>

}
package co.anitrend.core.api.endpoint

import co.anitrend.core.model.response.collection.GenreCollection
import co.anitrend.core.model.response.collection.MediaTagCollection
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MediaEndPoint {

    @POST("/")
    @GraphQuery("GenreCollection")
    fun getMediaGenres(@Body request: QueryContainerBuilder): Call<GraphContainer<GenreCollection>>

    @POST("/")
    @GraphQuery("MediaTagCollection")
    fun getMediaTags(@Body request: QueryContainerBuilder): Call<GraphContainer<MediaTagCollection>>

}
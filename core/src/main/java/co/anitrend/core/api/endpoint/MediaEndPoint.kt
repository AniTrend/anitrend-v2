package co.anitrend.core.api.endpoint

import co.anitrend.core.model.DataContainer
import co.anitrend.core.model.response.general.media.MediaTag
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MediaEndPoint {

    @POST("/")
    @GraphQuery("Genres")
    fun getMediaGenres(@Body request: QueryContainerBuilder): Call<GraphContainer<DataContainer<List<String>>>>

    @POST("/")
    @GraphQuery("Tags")
    fun getMediaTags(@Body request: QueryContainerBuilder): Call<GraphContainer<DataContainer<List<MediaTag>>>>

}
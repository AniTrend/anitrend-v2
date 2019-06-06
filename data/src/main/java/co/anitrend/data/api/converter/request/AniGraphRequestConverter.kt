package co.anitrend.data.api.converter.request

import com.google.gson.Gson
import io.github.wax911.library.annotation.processor.GraphProcessor
import io.github.wax911.library.converter.request.GraphRequestConverter
import io.github.wax911.library.model.request.QueryContainerBuilder
import okhttp3.RequestBody

class AniRequestConverter(
    methodAnnotations: Array<Annotation>,
    graphProcessor: GraphProcessor,
    gson: Gson
) : GraphRequestConverter(methodAnnotations, graphProcessor, gson) {

    /**
     * Converter for the request body, gets the GraphQL query from the method annotation
     * and constructs a GraphQL request body to send over the network.
     * <br></br>
     *
     * @param containerBuilder The constructed builder method of your query with variables
     * @return Request body
     */
    override fun convert(containerBuilder: QueryContainerBuilder): RequestBody {
        val rawPayload = graphProcessor.getQuery(methodAnnotations)

        /*val requestPayload = if (BuildConfig.DEBUG) rawPayload
         else GraphUtil.minify(rawPayload)*/

        val queryContainer = containerBuilder
            .setQuery(rawPayload)
            .build()

        val queryJson = gson.toJson(queryContainer)

        // Because anilist won't recognize application/graphql as a valid content type!
        // return RequestBody.create(MediaType.parse(GraphConverter.MimeType), queryJson)
        return RequestBody.create(null, queryJson)
    }
}
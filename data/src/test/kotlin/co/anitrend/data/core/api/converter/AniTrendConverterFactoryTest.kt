package co.anitrend.data.core.api.converter

import co.anitrend.data.auth.datasource.remote.AuthRemoteSource
import co.anitrend.data.edge.graphql.GeneratedGraphQLRegistry as EdgeRegistry
import co.anitrend.data.edge.graphql.GetMediaById
import co.anitrend.data.edge.graphql.GetMediaByIdVariables
import co.anitrend.data.edge.graphql.NewsConnection
import co.anitrend.data.edge.graphql.NewsConnectionVariables
import co.anitrend.data.graphql.anilist.GeneratedGraphQLRegistry as AniListRegistry
import co.anitrend.data.graphql.anilist.GetMediaDetail
import co.anitrend.data.graphql.anilist.GetMediaDetailVariables
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.retrofit.graphql.converter.GraphQLConverterFactory
import co.anitrend.retrofit.graphql.converter.request.GraphQLRequestConverter
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import co.anitrend.retrofit.graphql.serialization.kotlinx.KotlinxGraphQLTransportCodec
import com.google.gson.Gson
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import kotlin.coroutines.Continuation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Body

private const val OPERATION_NAME_KEY = "operationName"
private const val QUERY_KEY = "query"
private const val VARIABLES_KEY = "variables"

class AniTrendConverterFactoryTest {
    private val edgeMediaRemoteSourceClass: Class<*> =
        Class.forName("co.anitrend.data.edge.media.datasource.remote.EdgeMediaRemoteSource")
    private val newsRemoteSourceClass: Class<*> =
        Class.forName("co.anitrend.data.feed.news.datasource.remote.NewsRemoteSource")
    private val jsonResponseConverter = NamedResponseConverter("json")
    private val graphResponseConverter = NamedResponseConverter("graph")
    private val graphRequestConverter = NamedRequestConverter("graph-request")
    private val xmlResponseConverter = NamedResponseConverter("xml")
    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://example.com/")
            .build()

    private val factory =
        AniTrendConverterFactory(
            jsonFactory = StubFactory(responseConverter = jsonResponseConverter),
            graphFactory =
                StubFactory(
                    requestConverter = graphRequestConverter,
                    responseConverter = graphResponseConverter,
                ),
            xmlFactory = StubFactory(responseConverter = xmlResponseConverter),
            gson = Gson(),
        )

    private val compositeGeneratedRegistry =
        CompositeGraphQLDocumentRegistry(
            primary = AniListRegistry,
            fallback = EdgeRegistry,
        )

    private val realGraphFactory =
        GraphQLConverterFactory.create(
            codec =
                KotlinxGraphQLTransportCodec(
                    json =
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                            explicitNulls = false
                        },
                ),
            registry = compositeGeneratedRegistry,
        )

    @Test
    fun `AniList GraphQL request delegates to graph request converter without GRAPHQL annotation`() {
        val method = method(MediaRemoteSource::class.java, "getMediaDetail")
        val (bodyType, parameterAnnotations) = bodyParameter(method)

        assertFalse(method.annotations.any { it.annotationClass.simpleName == "GRAPHQL" })

        val converter =
            factory.requestBodyConverter(
                bodyType,
                parameterAnnotations,
                method.annotations,
                retrofit,
            )

        assertSame(graphRequestConverter, converter)
    }

    @Test
    fun `Edge GraphQL request delegates to graph request converter without GRAPHQL annotation`() {
        val method = method(edgeMediaRemoteSourceClass, "getMediaById")
        val (bodyType, parameterAnnotations) = bodyParameter(method)

        assertFalse(method.annotations.any { it.annotationClass.simpleName == "GRAPHQL" })

        val converter =
            factory.requestBodyConverter(
                bodyType,
                parameterAnnotations,
                method.annotations,
                retrofit,
            )

        assertSame(graphRequestConverter, converter)
    }

    @Test
    fun `AniList GraphQL response uses graph response converter without GRAPHQL annotation`() {
        val method = method(MediaRemoteSource::class.java, "getMediaDetail")
        val converter =
            factory.responseBodyConverter(
                responseType(method),
                method.annotations,
                retrofit,
            )

        assertSame(graphResponseConverter, converter)
    }

    @Test
    fun `Edge GraphQL response uses graph response converter without GRAPHQL annotation`() {
        val method = method(edgeMediaRemoteSourceClass, "getMediaById")
        val converter =
            factory.responseBodyConverter(
                responseType(method),
                method.annotations,
                retrofit,
            )

        assertSame(graphResponseConverter, converter)
    }

    @Test
    fun `JSON response keeps JSON converter control`() {
        val method = method(AuthRemoteSource::class.java, "getAuthenticationToken")

        val converter =
            factory.responseBodyConverter(
                responseType(method),
                method.annotations,
                retrofit,
            )

        assertSame(jsonResponseConverter, converter)
    }

    @Test
    fun `XML response keeps XML converter control`() {
        val method = method(newsRemoteSourceClass, "getNews")
        val converter =
            factory.responseBodyConverter(
                responseType(method),
                method.annotations,
                retrofit,
            )

        assertSame(xmlResponseConverter, converter)
    }

    @Test
    fun `AniList generated operation delegates to real GraphQL request converter`() {
        val converter =
            realGraphFactory.requestBodyConverter(
                GraphQLRequestType(GetMediaDetailVariables::class.java),
                emptyArray(),
                emptyArray(),
                retrofit,
            )

        assertNotNull(converter)
        assertTrue(converter is GraphQLRequestConverter)
    }

    @Test
    fun `Edge generated operation delegates to real GraphQL request converter`() {
        val converter =
            realGraphFactory.requestBodyConverter(
                GraphQLRequestType(GetMediaByIdVariables::class.java),
                emptyArray(),
                emptyArray(),
                retrofit,
            )

        assertNotNull(converter)
        assertTrue(converter is GraphQLRequestConverter)
    }

    /**
     * Verifies end-to-end request encoding through the real retrofit-graphql
     * factory and codec. Variables are intentionally absent here, mirroring
     * the no-variable request pattern used in production sources; the codec
     * encodes absent variables as an explicit JSON null.
     */
    @Test
    fun `real GraphQL converter encodes AniList generated operation envelope as application json`() {
        val request =
            GraphQLOperationRequest<GetMediaDetailVariables>(
                query = GetMediaDetail.document,
                operationName = GetMediaDetail.name,
            )

        val envelope = encodeRequestEnvelopeJson(GetMediaDetailVariables::class.java, request)

        assertEquals(GetMediaDetail.name, envelope.getValue(OPERATION_NAME_KEY).jsonPrimitive.content)
        assertEquals(GetMediaDetail.document, envelope.getValue(QUERY_KEY).jsonPrimitive.content)
        assertFalse(envelope.getValue(QUERY_KEY).jsonPrimitive.content.isBlank())
        assertTrue(envelope.getValue(VARIABLES_KEY) is JsonNull)
    }

    @Test
    fun `real GraphQL converter encodes Edge generated operation envelope as application json`() {
        val request =
            GraphQLOperationRequest<GetMediaByIdVariables>(
                query = GetMediaById.document,
                operationName = GetMediaById.name,
            )

        val envelope = encodeRequestEnvelopeJson(GetMediaByIdVariables::class.java, request)

        assertEquals(GetMediaById.name, envelope.getValue(OPERATION_NAME_KEY).jsonPrimitive.content)
        assertEquals(GetMediaById.document, envelope.getValue(QUERY_KEY).jsonPrimitive.content)
        assertFalse(envelope.getValue(QUERY_KEY).jsonPrimitive.content.isBlank())
        assertTrue(envelope.getValue(VARIABLES_KEY) is JsonNull)
    }

    @Test
    fun `real GraphQL converter encodes AniList generated operation with variables as application json`() {
        val request = GetMediaDetail.request(id = 1337)

        val envelope = encodeRequestEnvelopeJson(GetMediaDetailVariables::class.java, request)

        assertEquals(GetMediaDetail.name, envelope.getValue(OPERATION_NAME_KEY).jsonPrimitive.content)
        assertEquals(GetMediaDetail.document, envelope.getValue(QUERY_KEY).jsonPrimitive.content)
        val variables = envelope.getValue(VARIABLES_KEY).jsonObject
        assertEquals(1337, variables.getValue("id").jsonPrimitive.content.toInt())
        assertFalse(variables.containsKey("type"))
        assertFalse(variables.containsKey("scoreFormat"))
    }

    @Test
    fun `real GraphQL converter encodes Edge generated operation with variables as application json`() {
        val request = GetMediaById.request(id = 7331)

        val envelope = encodeRequestEnvelopeJson(GetMediaByIdVariables::class.java, request)

        assertEquals(GetMediaById.name, envelope.getValue(OPERATION_NAME_KEY).jsonPrimitive.content)
        assertEquals(GetMediaById.document, envelope.getValue(QUERY_KEY).jsonPrimitive.content)
        val variables = envelope.getValue(VARIABLES_KEY).jsonObject
        assertEquals(7331, variables.getValue("id").jsonPrimitive.content.toInt())
    }

    @Test
    fun `real GraphQL converter encodes Edge NewsConnection with supplied and omitted nullable variables as application json`() {
        val request = NewsConnection.request(after = "cursor-1")

        val envelope = encodeRequestEnvelopeJson(NewsConnectionVariables::class.java, request)

        assertEquals(NewsConnection.name, envelope.getValue(OPERATION_NAME_KEY).jsonPrimitive.content)
        assertEquals(NewsConnection.document, envelope.getValue(QUERY_KEY).jsonPrimitive.content)
        val variables = envelope.getValue(VARIABLES_KEY).jsonObject
        assertEquals("cursor-1", variables.getValue("after").jsonPrimitive.content)
        assertFalse(variables.containsKey("before"))
        assertFalse(variables.containsKey("limit"))
    }

    @Test
    fun `composite registry resolves AniList and Edge generated operation documents and hashes`() {
        assertEquals(GetMediaDetail.document, compositeGeneratedRegistry.document(GetMediaDetail.name))
        assertEquals(GetMediaDetail.sha256Hash, compositeGeneratedRegistry.hash(GetMediaDetail.name))
        assertEquals(GetMediaById.document, compositeGeneratedRegistry.document(GetMediaById.name))
        assertEquals(GetMediaById.sha256Hash, compositeGeneratedRegistry.hash(GetMediaById.name))
    }

    private fun bodyParameter(method: Method): Pair<Type, Array<out Annotation>> {
        val index =
            method.parameterAnnotations.indexOfFirst { annotations ->
                annotations.any { it is Body }
            }
        check(index >= 0) { "No @Body parameter found for ${method.name}" }
        return method.genericParameterTypes[index] to method.parameterAnnotations[index]
    }

    private fun method(
        owner: Class<*>,
        name: String,
    ): Method = owner.declaredMethods.first { it.name == name }

    private fun responseType(method: Method): Type {
        val returnType = method.genericReturnType
        if (returnType is ParameterizedType) {
            return unwrapResponseType(returnType.actualTypeArguments.first())
        }

        val continuationType =
            method.genericParameterTypes
                .filterIsInstance<ParameterizedType>()
                .firstOrNull { it.rawType == Continuation::class.java }
                ?: error("No Continuation parameter found for ${method.name}")

        return unwrapResponseType(continuationType.actualTypeArguments.first())
    }

    private fun unwrapResponseType(type: Type): Type {
        val candidate =
            when (type) {
                is WildcardType -> type.lowerBounds.firstOrNull() ?: type.upperBounds.first()
                else -> type
            }

        if (candidate is ParameterizedType && candidate.rawType == retrofit2.Response::class.java) {
            return candidate.actualTypeArguments.first()
        }

        return candidate
    }

    private fun RequestBody.asString(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }

    private fun encodeRequestEnvelopeJson(
        variablesType: Type,
        request: GraphQLOperationRequest<*>,
    ): JsonObject {
        val converter =
            realGraphFactory.requestBodyConverter(
                GraphQLRequestType(variablesType),
                emptyArray(),
                emptyArray(),
                retrofit,
            )
        assertNotNull(converter)
        val graphConverter = converter as GraphQLRequestConverter

        val body = graphConverter.convert(request)
        assertEquals("application/json", "${body.contentType()?.type}/${body.contentType()?.subtype}")
        return Json.parseToJsonElement(body.asString()).jsonObject
    }

    private class GraphQLRequestType(
        private val variablesType: Type,
    ) : ParameterizedType {
        override fun getRawType(): Type = GraphQLOperationRequest::class.java

        override fun getOwnerType(): Type? = null

        override fun getActualTypeArguments(): Array<Type> = arrayOf(variablesType)
    }

    private class NamedResponseConverter(
        private val name: String,
    ) : Converter<ResponseBody, Any> {
        override fun convert(value: ResponseBody): Any = name
    }

    private class NamedRequestConverter(
        private val name: String,
    ) : Converter<Any, RequestBody> {
        override fun convert(value: Any): RequestBody = name.toRequestBody()
    }

    private class StubFactory(
        private val requestConverter: Converter<Any, RequestBody>? = null,
        private val responseConverter: Converter<ResponseBody, Any>? = null,
    ) : Converter.Factory() {
        @Suppress("UNCHECKED_CAST")
        override fun requestBodyConverter(
            type: Type,
            parameterAnnotations: Array<out Annotation>,
            methodAnnotations: Array<out Annotation>,
            retrofit: Retrofit,
        ): Converter<*, RequestBody>? = requestConverter

        @Suppress("UNCHECKED_CAST")
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit,
        ): Converter<ResponseBody, *>? = responseConverter
    }
}

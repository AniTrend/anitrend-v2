package co.anitrend.data.core.api.converter

import co.anitrend.data.auth.datasource.remote.AuthRemoteSource
import co.anitrend.data.core.api.converter.request.AniRequestConverter
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import com.google.gson.Gson
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import kotlin.coroutines.Continuation
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Body

class AniTrendConverterFactoryTest {
    private val edgeMediaRemoteSourceClass: Class<*> =
        Class.forName("co.anitrend.data.edge.media.datasource.remote.EdgeMediaRemoteSource")
    private val newsRemoteSourceClass: Class<*> =
        Class.forName("co.anitrend.data.feed.news.datasource.remote.NewsRemoteSource")
    private val jsonResponseConverter = NamedResponseConverter("json")
    private val graphResponseConverter = NamedResponseConverter("graph")
    private val xmlResponseConverter = NamedResponseConverter("xml")
    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://example.com/")
            .build()

    private val factory =
        AniTrendConverterFactory(
            jsonFactory = StubFactory(responseConverter = jsonResponseConverter),
            graphFactory = StubFactory(responseConverter = graphResponseConverter),
            xmlFactory = StubFactory(responseConverter = xmlResponseConverter),
            gson = Gson(),
        )

    @Test
    fun `AniList GraphQL request uses graph request converter without GRAPHQL annotation`() {
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

        assertTrue(converter is AniRequestConverter)
    }

    @Test
    fun `Edge GraphQL request uses graph request converter without GRAPHQL annotation`() {
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

        assertTrue(converter is AniRequestConverter)
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

    private class NamedResponseConverter(
        private val name: String,
    ) : Converter<ResponseBody, Any> {
        override fun convert(value: ResponseBody): Any = name
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

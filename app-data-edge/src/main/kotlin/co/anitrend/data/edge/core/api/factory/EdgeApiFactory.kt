package co.anitrend.data.edge.core.api.factory

import co.anitrend.data.android.network.cache.CacheHelper
import co.anitrend.data.android.network.interceptor.app.AppInterceptor
import co.anitrend.data.android.network.interceptor.shared.SharedInterceptor
import co.anitrend.data.core.api.factory.IEndpointFactory
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.core.app.IAppInfo
import co.anitrend.data.core.device.IDeviceInfo
import co.anitrend.data.core.extensions.defaultBuilder
import co.anitrend.data.edge.BuildConfig
import co.anitrend.data.edge.core.api.interceptor.EdgeClientInterceptor
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

internal class EdgeApiFactory(
    private val deviceInfo: IDeviceInfo,
    private val appInfo: IAppInfo,
) : IEndpointFactory {
    override val endpointType = object : IEndpointType {
        override val url: HttpUrl = BuildConfig.edgeHost.toHttpUrl()
    }

    override fun okHttpConfig(scope: Scope): OkHttpClient {
        val builder = scope.defaultBuilder().cache(
            CacheHelper.createCache(
                scope.androidContext(),
                "edge"
            )
        ).addInterceptor(
            SharedInterceptor(
                deviceInfo = deviceInfo
            )
        ).addInterceptor(
            AppInterceptor(
                appInfo = appInfo
            )
        ).addInterceptor(
            EdgeClientInterceptor()
        ).cookieJar(scope.get())
        return builder.build()
    }

    companion object {
        const val BASE_ENDPOINT_PATH = "/graphql"
    }
}

package co.anitrend.deeplink.provider

import android.content.Context
import android.content.Intent
import co.anitrend.deeplink.component.screen.DeepLinkScreen
import co.anitrend.navigation.DeepLinkRouter
import com.kingsleyadio.deeplink.DeepLinkParser
import com.kingsleyadio.deeplink.DeepLinkUri
import timber.log.Timber

internal class FeatureProvider(
    private val deepLinkParser: DeepLinkParser<Intent?>,
) : DeepLinkRouter.Provider {
    override fun activity(context: Context?) = Intent(context, DeepLinkScreen::class.java)

    override fun matchingIntent(uri: String): Intent? {
        return runCatching {
            val deepLink = DeepLinkUri.parse(uri)
            deepLinkParser.parse(deepLink)
        }.onFailure(Timber::e).getOrNull()
    }
}

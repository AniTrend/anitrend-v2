package co.anitrend.deeplink.provider

import android.content.Intent
import co.anitrend.navigation.DeepLinkRouter
import com.kingsleyadio.deeplink.DeepLinkParser
import com.kingsleyadio.deeplink.DeepLinkUri

internal class FeatureProvider(
    private val deepLinkParser: DeepLinkParser<Intent?>,
) : DeepLinkRouter.Provider {
    override fun matchingIntent(uri: String): Intent? {
        val deepLinkUri = DeepLinkUri.parse(uri)
        return deepLinkParser.parse(deepLinkUri)
    }
}

package co.anitrend.navigation.extensions

import android.net.Uri

enum class DeepLinkType(val scheme: String, val host: String) {
    APP(scheme = "app.anitrend://", host = "action"),
    WEB(scheme = "https://", host = "anilist.co"),
}

@Throws(IllegalArgumentException::class)
fun deepLinkOf(path: String, type: DeepLinkType): Uri {
    if (!path.startsWith("/"))
        throw IllegalArgumentException("'$path' is not a recognised deep link")
    return Uri.parse("${type.scheme}${type.host}$path")
}

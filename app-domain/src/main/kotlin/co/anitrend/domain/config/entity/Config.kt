package co.anitrend.domain.config.entity

data class Config(
    val settings: Settings,
    val defaultImage: DefaultImage,
) {
    data class Settings(
        val analyticsEnabled: Boolean,
        val platformSource: String? = null,
    )

    data class DefaultImage(
        val banner: String,
        val poster: String,
        val loading: String,
        val error: String,
        val info: String,
        val default: String,
    )
}

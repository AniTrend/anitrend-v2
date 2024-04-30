package co.anitrend.domain.config.entity

data class Config(
    val settings: Settings,
    val image: DefaultImage,
    val navigation: List<Navigation>,
    val genres: List<Genre>
) {
    data class Settings(
        val analyticsEnabled: Boolean,
        val platformSource: String?,
    )

    data class DefaultImage(
        val banner: String,
        val poster: String,
        val loading: String,
        val error: String,
        val info: String,
        val default: String,
    )

    data class Genre(
        val mediaId: Long,
        val name: String,
    )

    data class Navigation(
        val criteria: String,
        val destination: String,
        val group: Group,
        val i18n: String,
        val icon: String,
        val id: Long
    ) {
        data class Group(
            val authenticated: Boolean,
            val i18n: String,
        )
    }
}

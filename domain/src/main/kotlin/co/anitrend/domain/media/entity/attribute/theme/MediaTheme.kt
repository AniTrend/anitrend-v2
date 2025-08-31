package co.anitrend.domain.media.entity.attribute.theme

data class MediaTheme(
    val mediaId: String,
    val themeId: String,
    val name: String,
    val audio: String?,
    val video: String,
    val meta: Meta?,
) {
    data class Meta(
        val number: Int,
        val type: String,
        val version: Int
    )
}

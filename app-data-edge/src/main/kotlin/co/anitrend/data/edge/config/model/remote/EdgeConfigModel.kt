package co.anitrend.data.edge.config.model.remote

import co.anitrend.data.edge.genre.model.EdgeGenreModel
import co.anitrend.data.edge.navigation.model.EdgeNavigationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeConfigModel(
    @SerialName("settings") val settings: Settings,
    @SerialName("image") val image: DefaultImage,
    @SerialName("genres") val genres: List<EdgeGenreModel>,
    @SerialName("navigation") val navigation: List<EdgeNavigationModel>,
) {

    @Serializable
    data class Settings(
        @SerialName("analyticsEnabled") val analyticsEnabled: Boolean,
        @SerialName("platformSource") val platformSource: String?,
    )

    @Serializable
    data class DefaultImage(
        @SerialName("banner") val banner: String,
        @SerialName("poster") val poster: String,
        @SerialName("loading") val loading: String,
        @SerialName("error") val error: String,
        @SerialName("info") val info: String,
        @SerialName("default") val default: String,
    )
}

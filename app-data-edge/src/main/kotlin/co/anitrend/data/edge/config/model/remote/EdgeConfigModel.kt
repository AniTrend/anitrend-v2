package co.anitrend.data.edge.config.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeConfigModel(
    @SerialName("settings") val settings: Settings,
    @SerialName("image") val defaultImage: DefaultImage,
    @SerialName("id") val id: Long,
) {

    @Serializable
    data class Settings(
        @SerialName("analyticsEnabled") val analyticsEnabled: Boolean,
    )

    @Serializable
    data class DefaultImage(
        @SerialName("banner") val banner: String,
        @SerialName("poster") val poster: String = "",
        @SerialName("loading") val loading: String = "",
        @SerialName("error") val error: String = "",
        @SerialName("info") val info: String = "",
        @SerialName("default") val default: String = "",
    )
}

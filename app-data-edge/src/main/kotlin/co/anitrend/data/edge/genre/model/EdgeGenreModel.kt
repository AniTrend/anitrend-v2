package co.anitrend.data.edge.genre.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdgeGenreModel(
    @SerialName("mediaId") val mediaId: Long,
    @SerialName("name") val name: String,
)

package co.anitrend.data.jikan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class JikanWrapper<out T>(
    @SerialName("data") val data: T
)

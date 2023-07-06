package co.anitrend.data.edge.navigation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EdgeNavigationModel(
    @SerialName("id") val id: Long,
    @SerialName("destination") val destination: String,
    @SerialName("i18n") val label: String,
    @SerialName("icon") val icon: String,
    @SerialName("group") val group: Group,
) {
    @Serializable
    data class Group(
        @SerialName("authenticated") val authenticated: Boolean,
        @SerialName("i18n") val label: String,
        @SerialName("id") val id: Long,
    )
}

package co.anitrend.data.edge.navigation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EdgeNavigationModel(
    @SerialName("criteria") val criteria: String,
    @SerialName("destination") val destination: String,
    @SerialName("group") val group: NavigationGroup,
    @SerialName("i18n") val i18n: String,
    @SerialName("icon") val icon: String,
) {
    @Serializable
    data class NavigationGroup(
        @SerialName("authenticated") val authenticated: Boolean,
        @SerialName("i18n") val i18n: String,
    )
}

package co.anitrend.data.model.response.collection

import co.anitrend.data.model.response.general.media.MediaTag
import com.google.gson.annotations.SerializedName

data class MediaTagCollection(
    @SerializedName("MediaTagCollection")
    val mediaTagCollection: List<MediaTag>
)
package co.anitrend.core.model.response.collection

import com.google.gson.annotations.SerializedName

data class GenreCollection(
    @SerializedName("GenreCollection")
    val genreCollection: List<String>
)
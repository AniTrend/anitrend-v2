package co.anitrend.android.core.compose.design.header

import androidx.compose.runtime.Immutable

@Immutable
data class AniTrendHeaderState(
    val title: String,
    val description: String? = null,
    val lineSizeLimit: LineSizeLimit = LineSizeLimit(),
) {
    @Immutable
    data class LineSizeLimit(
        val title: Int = 1,
        val description: Int = 1,
    )
}

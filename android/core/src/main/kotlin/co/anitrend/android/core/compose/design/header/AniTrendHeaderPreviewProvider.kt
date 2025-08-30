package co.anitrend.android.core.compose.design.header

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal data class AniTrendHeaderPreviewProvider(
    override val values: Sequence<AniTrendHeaderState> = sequenceOf(
        AniTrendHeaderState(
            title = "AniTrend",
            description = "AniTrend is a modern anime and manga tracking app for Android.",
            lineSizeLimit = AniTrendHeaderState.LineSizeLimit(description = 2)
        ),
        AniTrendHeaderState(
            title = "AniTrend",
        ),
    )
) : PreviewParameterProvider<AniTrendHeaderState>

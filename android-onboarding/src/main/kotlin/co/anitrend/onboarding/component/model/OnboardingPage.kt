package co.anitrend.onboarding.component.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
@Immutable
data class OnboardingPage(
    val resource: Int,
    val background: List<Color>,
    val title: Int,
    val description: Int
)

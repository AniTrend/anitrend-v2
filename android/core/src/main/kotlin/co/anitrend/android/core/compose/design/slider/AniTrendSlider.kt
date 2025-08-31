package co.anitrend.android.core.compose.design.slider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@Composable
fun AniTrendSliderItem(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    valueLabel: (Float) -> String,
    extraInfo: (() -> String)? = null,
    progress: (() -> Float)? = null,
) {
    // Internal state for the slider's current visual position.
    // Initialized with the external value.
    var internalSliderValue by remember { mutableFloatStateOf(value()) }

    // If the external value() changes, update our internal state.
    // This makes the component controlled by the external value.
    LaunchedEffect(value()) {
        if (internalSliderValue != value()) {
            internalSliderValue = value()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = valueLabel(internalSliderValue), // Label reflects the interactive slider value
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
        )
        Slider(
            value = internalSliderValue, // Slider is driven by internal state
            onValueChange = { newValue ->
                internalSliderValue = newValue // Update internal state immediately for responsiveness
                onValueChange(newValue)  // Propagate the change to the external handler
            },
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
        )
        progress?.let { progressProvider ->
            LinearProgressIndicator(
                // Progress should reflect the committed state from the progressProvider lambda
                progress = { progressProvider() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )
        }
        extraInfo?.let { infoProvider ->
            Text(
                // Extra info should also reflect the committed state from the infoProvider lambda
                text = infoProvider(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun AniTrendSliderItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    var sliderValue by remember { mutableStateOf(0.5f) }
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSliderItem(
            value = { sliderValue },
            onValueChange = { sliderValue = it },
            valueRange = 0f..1f,
            steps = 10,
            valueLabel = { "Value: ${"%.2f".format(it)}" }, // Formatted for preview
            extraInfo = { "Extra info for slider. Current committed value: ${"%.2f".format(sliderValue)}" },
            progress = { sliderValue / 1f }
        )
    }
}

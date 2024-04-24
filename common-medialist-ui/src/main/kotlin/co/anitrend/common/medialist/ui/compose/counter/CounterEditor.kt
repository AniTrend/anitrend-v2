package co.anitrend.common.medialist.ui.compose.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandCircleDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@Composable
fun CounterEditor(modifier: Modifier = Modifier) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("0")) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {}) {
            Icon(
                imageVector = Icons.Rounded.ExpandCircleDown,
                contentDescription = null,
                modifier = Modifier,
            )
        }
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            modifier = Modifier.width(48.dp),
            singleLine = true,
        )
        TextButton(onClick = {}) {
            Icon(
                imageVector = Icons.Rounded.ExpandCircleDown,
                contentDescription = null,
                modifier = Modifier.rotate(180f),
            )
        }
    }
}

@AniTrendPreview.Dark
@AniTrendPreview.Light
@Composable
private fun CounterEditorPreview() {
    PreviewTheme(wrapInSurface = true) {
        CounterEditor(
            modifier = Modifier.padding(8.dp)
        )
    }
}

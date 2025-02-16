package co.anitrend.common.shared.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun DefaultBottomAppBar(onBackPress: () -> Unit) {
    BottomAppBar(
        actions = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
fun DefaultScaffold(
    onBackPress: () -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        bottomBar = {
            DefaultBottomAppBar(onBackPress)
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

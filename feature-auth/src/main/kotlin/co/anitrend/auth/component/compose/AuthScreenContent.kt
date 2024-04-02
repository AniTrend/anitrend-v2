package co.anitrend.auth.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.NoAccounts
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.anitrend.auth.component.viewmodel.state.AuthState
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.android.ui.typography.AniTrendTypography
import kotlinx.coroutines.launch

@Composable
private fun AuthBrandNameComponent(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_segment_first),
            style = AniTrendTypography.displayMedium,
        )
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_segment_second),
            style = AniTrendTypography.displayMedium.copy(
                color = colorResource(co.anitrend.arch.theme.R.color.colorStateBlue)
            ),

        )
    }
}

@Composable
private fun AuthHeaderSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthBrandNameComponent()
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(text = stringResource(co.anitrend.auth.R.string.label_allow_authorization))
    }
}

@Composable
private fun AuthAuthorizationSection(
    onAuthorizeClick: () -> Unit,
    onAuthorizationHelpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = onAuthorizeClick,
        ) {
            Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null)
            Spacer(modifier = Modifier.padding(start = 6.dp))
            Text(text = stringResource(co.anitrend.auth.R.string.auth_label_authorize))
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        SuggestionChip(
            onClick = onAuthorizationHelpClick,
            label = {
                Text(text = stringResource(co.anitrend.auth.R.string.auth_label_having_trouble_logging_in))
            },
            icon = { Icon(imageVector = Icons.TwoTone.Info, contentDescription = null) }
        )
    }
}

@Composable
private fun AuthAnonymousSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_alternative_account),
            textAlign = TextAlign.Center,
            style = AniTrendTypography.labelMedium,
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        SuggestionChip(
            onClick = {},
            label = {
                Text(text = stringResource(co.anitrend.auth.R.string.auth_label_action_start_anonymous_account))
            },
            icon = { Icon(imageVector = Icons.TwoTone.NoAccounts, contentDescription = null) }
        )
    }
}

@Composable
private fun AuthContent(
    onAuthorizeClick: () -> Unit,
    onAuthorizationHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.then(Modifier.padding(start = 48.dp, end = 48.dp)),
    ) {
        AuthHeaderSection()
        Spacer(modifier = Modifier.padding(top = 24.dp))
        AuthAuthorizationSection(onAuthorizeClick, onAuthorizationHelpClick)
        Spacer(modifier = Modifier.padding(top = 24.dp))
        AuthAnonymousSection()
    }
}

@Composable
private fun AuthBottomAppBar(onBackPress: () -> Unit) {
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
fun AuthScreenContent(
    authState: AuthState,
    onAuthorizeClick: () -> Unit,
    onAuthorizationHelpClick: () -> Unit,
    onBackPress: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = authState.model.observeAsState()

    Scaffold(
        bottomBar = {
            AuthBottomAppBar(onBackPress = onBackPress)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
    ) { innerPadding ->
        AuthContent(
            onAuthorizeClick,
            onAuthorizationHelpClick,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}


@AniTrendPreview.Mobile
@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaDetailComponentPreview() {
    AniTrendTheme3 {
        AuthContent(
            onAuthorizeClick = {},
            onAuthorizationHelpClick = {}
        )
    }
}

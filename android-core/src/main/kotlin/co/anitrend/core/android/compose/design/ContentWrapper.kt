package co.anitrend.core.android.compose.design

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.navigation.model.common.IParam
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
private fun ContentImage(
    @DrawableRes drawableResource: Int?,
    modifier: Modifier = Modifier
) {
    if (drawableResource == null)
        return

    Image(
        painter = rememberAsyncImagePainter(drawableResource),
        contentDescription = null,
        modifier = modifier.size(96.dp),
    )
}

@Composable
private fun ContentText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        modifier = modifier,
    )
}

@Composable
private fun LoadingContent(
    config: IStateLayoutConfig,
    modifier: Modifier = Modifier,
) {
    Surface {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContentImage(drawableResource = config.loadingDrawable)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp)
                        .align(alignment = Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 2.dp,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
                config.loadingMessage?.also {
                    ContentText(text = stringResource(it))
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    config: IStateLayoutConfig,
    state: LoadState.Error,
    modifier: Modifier = Modifier,
    onClick: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Surface {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContentImage(drawableResource = config.errorDrawable)
            Spacer(modifier = Modifier.height(16.dp))
            state.details.message?.also {
                ContentText(text = it)
            }
            config.retryAction?.also {
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = { scope.launch { onClick() } },
                ) {
                    Text(text = stringResource(it))
                }
            }
        }
    }
}

@Composable
fun <P: IParam> ContentWrapper(
    stateFlow: LiveData<LoadState>,
    config: IStateLayoutConfig = koinInject(),
    param: P? = null,
    onLoad: (P) -> Unit = {},
    onClick: suspend () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val modifier: Modifier = Modifier.fillMaxSize().padding(32.dp)
    val loadState by stateFlow.observeAsState(LoadState.Loading())

    if (param == null) {
        ErrorContent(
            config = config,
            state = LoadState.Error(
                RequestError(
                    topic = stringResource(co.anitrend.core.android.R.string.app_controller_heading_missing_param),
                    description = stringResource(co.anitrend.core.android.R.string.app_controller_message_missing_param),
                )
            ),
            modifier = modifier,
            onClick = onClick
        )
        return
    }

    when (val state = loadState) {
        is LoadState.Error -> ErrorContent(
            config = config,
            state = state,
            modifier = modifier,
            onClick = onClick,
        )
        is LoadState.Loading -> LoadingContent(
            config = config,
            modifier = modifier,
        )
        else -> content()
    }

    LaunchedEffect(param) {
        onLoad(param)
    }
}


@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun ContentWrapperPreview(
    @PreviewParameter(provider = ContentWrapperPreviewParameter::class) loadState: LoadState
) {
    val modifier: Modifier = Modifier.fillMaxSize().padding(16.dp)
    val config: IStateLayoutConfig = StateLayoutConfig(
        loadingDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        errorDrawable = co.anitrend.arch.ui.R.drawable.ic_support_empty_state,
        loadingMessage = co.anitrend.arch.ui.R.string.supportTextLoading,
        defaultMessage = co.anitrend.core.android.R.string.app_controller_message_missing_param,
        retryAction = co.anitrend.core.android.R.string.action_share
    )
    PreviewTheme {
        when (loadState) {
            is LoadState.Error -> ErrorContent(
                config = config,
                state = loadState,
                modifier = modifier,
                onClick = {},
            )
            is LoadState.Loading -> LoadingContent(
                config = config,
                modifier = modifier,
            )
            is LoadState.Idle,
            is LoadState.Success -> {}
        }
    }
}

private class ContentWrapperPreviewParameter(
    override val values: Sequence<LoadState> = sequenceOf(
        LoadState.Loading(),
        LoadState.Error(details = UnsupportedOperationException("Looks like no arguments were passed to this screen")),
        LoadState.Idle(),
    )
) : PreviewParameterProvider<LoadState>

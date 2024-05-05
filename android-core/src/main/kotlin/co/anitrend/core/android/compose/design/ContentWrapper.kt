/*
 * Copyright (C) 2024 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.core.android.compose.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.core.android.R
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.navigation.model.common.IParam
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
private fun LoadingContent(
    config: IStateLayoutConfig,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = rememberAsyncImagePainter(config.loadingDrawable),
            contentDescription = "",
            modifier =
                Modifier.size(64.dp)
                    .align(alignment = Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp).align(alignment = Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 2.dp,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
            config.loadingMessage?.also {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(it),
                    style = AniTrendTheme.typography.body2,
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    config: IStateLayoutConfig,
    state: LoadState.Error,
    modifier: Modifier,
    onClick: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = rememberAsyncImagePainter(config.errorDrawable),
            contentDescription = "",
            modifier =
                Modifier.size(64.dp)
                    .align(alignment = Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(8.dp))
        state.details.message?.also {
            Text(
                text = it,
                style = AniTrendTheme.typography.body2,
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        config.retryAction?.also {
            FilledTonalButton(
                onClick = { scope.launch { onClick() } },
                modifier =
                    Modifier
                        .align(alignment = Alignment.CenterHorizontally),
            ) {
                Text(
                    text = stringResource(it),
                    style = AniTrendTheme.typography.body2,
                )
            }
        }
    }
}

@Composable
fun <P : IParam> ContentWrapper(
    stateFlow: LiveData<LoadState>,
    config: IStateLayoutConfig = koinInject(),
    param: P? = null,
    onLoad: (P) -> Unit = {},
    onClick: suspend () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val modifier: Modifier = Modifier.fillMaxSize().padding(16.dp)
    val loadState by stateFlow.observeAsState(LoadState.Loading())

    if (param == null) {
        ErrorContent(
            config = config,
            state =
                LoadState.Error(
                    RequestError(
                        topic = stringResource(R.string.app_controller_heading_missing_param),
                        description = stringResource(R.string.app_controller_message_missing_param),
                    ),
                ),
            modifier = modifier,
            onClick = onClick,
        )
        return
    }

    when (val state = loadState) {
        is LoadState.Error -> ErrorContent(config = config, state = state, modifier = modifier, onClick = onClick)
        is LoadState.Loading -> LoadingContent(config = config, modifier = modifier)
        else -> content()
    }

    LaunchedEffect(param) {
        onLoad(param)
    }
}

/*
 * Copyright (C) 2025 AniTrend
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
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.NoAccounts
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.core.android.ui.typography.AniTrendTypography

@Composable
private fun AuthBrandNameComponent(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_segment_first),
            style = AniTrendTypography.displayMedium,
        )
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_segment_second),
            style =
                AniTrendTypography.displayMedium.copy(
                    color = colorResource(co.anitrend.arch.theme.R.color.colorStateBlue),
                ),
        )
    }
}

@Composable
private fun AuthHeaderSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
            icon = { Icon(imageVector = Icons.TwoTone.Info, contentDescription = null) },
        )
    }
}

@Composable
private fun AuthAnonymousSection(
    onAuthorizationAnonymousClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(co.anitrend.auth.R.string.auth_label_alternative_account),
            textAlign = TextAlign.Center,
            style = AniTrendTypography.labelMedium,
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        SuggestionChip(
            onClick = onAuthorizationAnonymousClick,
            label = {
                Text(text = stringResource(co.anitrend.auth.R.string.auth_label_action_start_anonymous_account))
            },
            icon = { Icon(imageVector = Icons.TwoTone.NoAccounts, contentDescription = null) },
        )
    }
}

@Composable
private fun AuthContent(
    onAuthorizeClick: () -> Unit,
    onAuthorizationHelpClick: () -> Unit,
    onAuthorizationAnonymousClick: () -> Unit,
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
        AuthAnonymousSection(onAuthorizationAnonymousClick)
    }
}

@Composable
fun AuthScreenContent(
    onAuthorizeClick: () -> Unit,
    onAuthorizationHelpClick: () -> Unit,
    onAuthorizationAnonymousClick: () -> Unit,
    onBackPress: () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { modifier ->
        AuthContent(
            onAuthorizeClick,
            onAuthorizationHelpClick,
            onAuthorizationAnonymousClick,
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaDetailComponentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme) {
        AuthScreenContent(
            onAuthorizeClick = {},
            onAuthorizationHelpClick = {},
            onAuthorizationAnonymousClick = {},
            onBackPress = {},
        )
    }
}

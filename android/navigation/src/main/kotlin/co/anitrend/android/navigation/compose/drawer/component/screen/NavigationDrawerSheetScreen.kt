/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.android.navigation.compose.drawer.component.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.shape.DrawerCutoutShape
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.adaptiveIconPainterResource
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.account.Account
import co.anitrend.android.navigation.drawer.model.internal.DrawerEntry
import co.anitrend.android.navigation.drawer.model.internal.DrawerUiState

private val DrawerRowShape = RoundedCornerShape(20.dp)
private val DrawerSheetTopCornerRadius = 30.dp
private val DrawerForegroundTopOffset = 24.dp
private val DrawerAvatarSize = 48.dp
private val DrawerAvatarHaloSize = 56.dp
private val DrawerNavigationContentTopPadding = 42.dp

@Immutable
private enum class DrawerSheetContentState {
    Closed,
    Navigation,
    AccountSwitcher,
}

@Composable
internal fun NavigationDrawerSheetScreen(
    uiState: DrawerUiState,
    onHeaderClick: () -> Unit,
    onAccountClick: (Account) -> Unit,
    onNavigationClick: (DrawerEntry.Item) -> Unit,
) {
    val contentState =
        when {
            !uiState.isSheetVisible -> DrawerSheetContentState.Closed
            uiState.isAccountSwitcherExpanded -> DrawerSheetContentState.AccountSwitcher
            else -> DrawerSheetContentState.Navigation
        }

    val transition =
        androidx.compose.animation.core.updateTransition(
            targetState = contentState,
            label = "drawer_sheet_state",
        )
    val badgeScale by transition.animateFloat(
        label = "badge_scale",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) },
    ) { state ->
        when (state) {
            DrawerSheetContentState.Navigation -> 1f
            DrawerSheetContentState.AccountSwitcher -> 0.78f
            DrawerSheetContentState.Closed -> 0.86f
        }
    }
    val badgeAlpha by transition.animateFloat(
        label = "badge_alpha",
        transitionSpec = { tween(durationMillis = 260) },
    ) { state ->
        when (state) {
            DrawerSheetContentState.Navigation -> 1f
            else -> 0f
        }
    }
    val cutoutProgress by transition.animateFloat(
        label = "cutout_progress",
        transitionSpec = { tween(durationMillis = 320, easing = FastOutSlowInEasing) },
    ) { state ->
        when (state) {
            DrawerSheetContentState.Navigation -> 1f
            else -> 0f
        }
    }
    val foregroundAlpha by transition.animateFloat(
        label = "foreground_alpha",
        transitionSpec = { tween(durationMillis = 220, easing = FastOutSlowInEasing) },
    ) { state ->
        if (state == DrawerSheetContentState.Navigation) 1f else 0f
    }
    val foregroundOffset by transition.animateDp(
        label = "foreground_offset",
        transitionSpec = { tween(durationMillis = 260, easing = FastOutSlowInEasing) },
    ) { state ->
        if (state == DrawerSheetContentState.AccountSwitcher) 18.dp else 0.dp
    }
    val accountContentAlpha by transition.animateFloat(
        label = "account_content_alpha",
        transitionSpec = {
            if (targetState == DrawerSheetContentState.AccountSwitcher) {
                tween(durationMillis = 160, delayMillis = 180, easing = FastOutSlowInEasing)
            } else {
                tween(durationMillis = 120, easing = FastOutSlowInEasing)
            }
        },
    ) { state ->
        if (state == DrawerSheetContentState.AccountSwitcher) 1f else 0f
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        BackgroundDrawerSurface(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (accountContentAlpha > 0.01f) {
                Box(
                    modifier = Modifier.graphicsLayer { alpha = accountContentAlpha },
                ) {
                    AccountSwitcherContent(
                        accounts = uiState.accounts,
                        onAccountClick = onAccountClick,
                    )
                }
            }
        }

        if (badgeAlpha > 0.01f) {
            DrawerSeamBadge(
                account = uiState.activeAccount,
                isExpanded = contentState == DrawerSheetContentState.AccountSwitcher,
                modifier =
                    Modifier
                        .graphicsLayer { alpha = badgeAlpha }
                        .scale(badgeScale),
                onClick = onHeaderClick,
            )
        }

        if (foregroundAlpha > 0.01f) {
            ForegroundDrawerSurface(
                cutoutProgress = cutoutProgress,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = DrawerForegroundTopOffset)
                        .offset(y = foregroundOffset)
                        .graphicsLayer { alpha = foregroundAlpha },
            ) {
                NavigationDrawerContent(
                    entries = uiState.entries,
                    enabled = contentState == DrawerSheetContentState.Navigation,
                    onNavigationClick = onNavigationClick,
                )
            }
        }
    }
}

@Composable
private fun BackgroundDrawerSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = DrawerSheetTopCornerRadius, topEnd = DrawerSheetTopCornerRadius),
        color = drawerBackgroundColor(),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
    ) {
        content()
    }
}

@Composable
private fun ForegroundDrawerSurface(
    cutoutProgress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape =
            DrawerCutoutShape(
                topCornerRadius = DrawerSheetTopCornerRadius,
                cutoutProgress = cutoutProgress,
            ),
        color = drawerForegroundColor(),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = DrawerNavigationContentTopPadding),
        ) {
            content()
        }
    }
}

@Composable
private fun DrawerSeamBadge(
    account: Account?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val stateDescription =
        stringResource(
            id =
                if (isExpanded) {
                    R.string.description_navigation_drawer_accounts_expanded
                } else {
                    R.string.description_navigation_drawer_navigation_expanded
                },
        )
    val actionDescription =
        stringResource(
            id =
                if (isExpanded) {
                    R.string.action_navigation_drawer_close_accounts
                } else {
                    R.string.action_navigation_drawer_switch_accounts
                },
        )

    Surface(
        modifier =
            modifier
                .semantics {
                    role = Role.Button
                    contentDescription = actionDescription
                    this.stateDescription = stateDescription
                }.clickable(onClick = onClick),
        shape = CircleShape,
        color = drawerAvatarHaloColor(),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier =
                Modifier
                    .size(DrawerAvatarHaloSize),
            contentAlignment = Alignment.Center,
        ) {
            DrawerAccountAvatar(
                account = account,
                modifier = Modifier.size(DrawerAvatarSize),
            )
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    entries: List<DrawerEntry>,
    enabled: Boolean,
    onNavigationClick: (DrawerEntry.Item) -> Unit,
) {
    val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = topPadding + 6.dp,
                    bottom = bottomPadding + 20.dp,
                ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        entries.forEach { entry ->
            when (entry) {
                is DrawerEntry.Header ->
                    DrawerSection(
                        title = stringResource(id = entry.titleRes),
                        modifier = Modifier.padding(top = 12.dp, bottom = 2.dp, start = 4.dp),
                    )
                is DrawerEntry.Item ->
                    DrawerMenuRow(
                        item = entry,
                        enabled = enabled,
                        onClick = { onNavigationClick(entry) },
                    )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AccountSwitcherContent(
    accounts: List<Account>,
    onAccountClick: (Account) -> Unit,
) {
    val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = topPadding + 6.dp,
                    bottom = bottomPadding + 20.dp,
                ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        accounts.forEach { account ->
            when (account) {
                is Account.Group ->
                    DrawerSection(
                        title = stringResource(id = account.titleRes),
                        modifier = Modifier.padding(top = 12.dp, bottom = 2.dp, start = 4.dp),
                    )
                else ->
                    AccountSwitcherRow(
                        account = account,
                        onClick = { onAccountClick(account) },
                    )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DrawerSection(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.semantics { heading() },
    )
}

@Composable
private fun DrawerMenuRow(
    item: DrawerEntry.Item,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val itemLabel = stringResource(id = item.titleRes)
    val selectedContainerColor = selectedDrawerRowContainerColor()
    val iconTint =
        if (item.isChecked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    val textColor =
        if (item.isChecked) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(DrawerRowShape)
                .background(if (item.isChecked) selectedContainerColor else Color.Transparent)
                .defaultMinSize(minHeight = 52.dp)
                .semantics {
                    role = Role.Button
                    selected = item.isChecked
                    contentDescription = itemLabel
                }.clickable(enabled = enabled, onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = null,
                tint = iconTint,
            )
        }
        Text(
            text = itemLabel,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (item.isChecked) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor,
        )
    }
}

@Composable
private fun AccountSwitcherRow(
    account: Account,
    onClick: () -> Unit,
) {
    val label = accountTitle(account)
    val isActive = account.isActiveUser
    val selectedContainerColor = selectedDrawerRowContainerColor()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(DrawerRowShape)
                .background(if (isActive) selectedContainerColor else Color.Transparent)
                .defaultMinSize(minHeight = 52.dp)
                .semantics {
                    role = Role.Button
                    selected = isActive
                    contentDescription = label
                }.clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (account) {
            is Account.Authenticated,
            is Account.Anonymous,
            -> DrawerAccountAvatar(account = account, modifier = Modifier.size(24.dp))
            is Account.Authorize ->
                Box(
                    modifier = Modifier.width(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_add_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            is Account.Group -> Unit
        }
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DrawerAccountAvatar(
    account: Account?,
    modifier: Modifier = Modifier,
) {
    val avatarContent = resolveDrawerAvatarContent(account)

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        when (avatarContent) {
            is DrawerAvatarContent.RemoteImage ->
                AniTrendImage(
                    image = avatarContent.image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.fillMaxSize(),
                )
            is DrawerAvatarContent.AdaptiveLocalImage ->
                Image(
                    painter = adaptiveIconPainterResource(id = avatarContent.imageRes),
                    contentDescription = stringResource(id = avatarContent.contentDescriptionRes),
                    modifier = Modifier.padding(4.dp),
                )
            is DrawerAvatarContent.TintedIcon ->
                Icon(
                    painter = painterResource(id = avatarContent.iconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
        }
    }
}

@Composable
private fun accountTitle(account: Account): String =
    when (account) {
        is Account.Authenticated -> account.userName.toString()
        is Account.Anonymous -> stringResource(id = account.titleRes)
        is Account.Authorize -> stringResource(id = account.titleRes)
        is Account.Group -> stringResource(id = account.titleRes)
    }

@Composable
private fun selectedDrawerRowContainerColor(): Color =
    if (isLightTheme()) {
        MaterialTheme.colorScheme.surfaceContainerHigh
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }

@Composable
private fun drawerBackgroundColor(): Color =
    if (isLightTheme()) {
        MaterialTheme.colorScheme.surfaceContainerLow
    } else {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }

@Composable
private fun drawerForegroundColor(): Color =
    if (isLightTheme()) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

@Composable
private fun drawerAvatarHaloColor(): Color =
    if (isLightTheme()) {
        MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

@Composable
private fun isLightTheme(): Boolean = MaterialTheme.colorScheme.background.luminance() > 0.5f

package co.anitrend.android.navigation.compose.drawer.component.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.adaptiveIconPainterResource
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.model.account.Account
import co.anitrend.android.navigation.drawer.model.internal.DrawerEntry
import co.anitrend.android.navigation.drawer.model.internal.DrawerUiState

@Composable
internal fun NavigationDrawerSheetScreen(
    uiState: DrawerUiState,
    onHeaderClick: () -> Unit,
    onAccountClick: (Account) -> Unit,
    onNavigationClick: (DrawerEntry.Item) -> Unit,
) {
    val transition =
        updateTransition(
            targetState = uiState.isAccountSwitcherExpanded,
            label = "drawer_switcher",
        )
    val avatarScale by transition.animateFloat(
        label = "avatar_scale",
        transitionSpec = { tween(durationMillis = 320, easing = FastOutSlowInEasing) },
    ) { expanded ->
        if (expanded) 0.88f else 1f
    }
    val avatarAlpha by transition.animateFloat(
        label = "avatar_alpha",
        transitionSpec = { tween(durationMillis = 240) },
    ) { expanded ->
        if (expanded) 0.78f else 1f
    }
    val contentOffset by transition.animateDp(
        label = "content_offset",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) },
    ) { expanded ->
        if (expanded) 6.dp else 0.dp
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 10.dp,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DrawerProfileHeader(
                activeAccount = uiState.activeAccount,
                isExpanded = uiState.isAccountSwitcherExpanded,
                avatarScale = avatarScale,
                avatarAlpha = avatarAlpha,
                onClick = onHeaderClick,
            )

            AnimatedContent(
                targetState = uiState.isAccountSwitcherExpanded,
                label = "drawer_content",
                transitionSpec = {
                    (
                        fadeIn(animationSpec = tween(220)) +
                            slideInVertically(
                                animationSpec = tween(280, easing = FastOutSlowInEasing),
                                initialOffsetY = { height -> height / 10 },
                            )
                    ) togetherWith
                        (
                            fadeOut(animationSpec = tween(160)) +
                                slideOutVertically(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                                    targetOffsetY = { height -> -height / 12 },
                                )
                        )
                },
            ) { expanded ->
                if (expanded) {
                    DrawerAccountSwitcherSection(
                        accounts = uiState.accounts,
                        modifier =
                            Modifier.graphicsLayer {
                                alpha = avatarAlpha
                                translationY = contentOffset.toPx()
                            },
                        onAccountClick = onAccountClick,
                    )
                } else {
                    DrawerNavigationSection(
                        entries = uiState.entries,
                        modifier =
                            Modifier.graphicsLayer {
                                alpha = avatarAlpha
                                translationY = contentOffset.toPx()
                            },
                        onNavigationClick = onNavigationClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerProfileHeader(
    activeAccount: Account?,
    isExpanded: Boolean,
    avatarScale: Float,
    avatarAlpha: Float,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DrawerAccountAvatar(
                account = activeAccount,
                modifier =
                    Modifier
                        .size(56.dp)
                        .scale(avatarScale)
                        .alpha(avatarAlpha),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = activeAccountTitle(activeAccount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text =
                        stringResource(
                            if (isExpanded) {
                                R.string.account_header_other
                            } else {
                                R.string.account_header_active
                            },
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = if (isExpanded) "Close" else "Accounts",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
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
                    modifier = Modifier.fillMaxWidth(),
                )
            is DrawerAvatarContent.AdaptiveLocalImage ->
                Image(
                    painter = adaptiveIconPainterResource(id = avatarContent.imageRes),
                    contentDescription = stringResource(id = avatarContent.contentDescriptionRes),
                    modifier = Modifier.padding(12.dp),
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
private fun DrawerAccountSwitcherSection(
    accounts: List<Account>,
    modifier: Modifier = Modifier,
    onAccountClick: (Account) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        accounts.forEach { account ->
            when (account) {
                is Account.Group ->
                    Text(
                        text = stringResource(id = account.titleRes),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                    )
                else ->
                    DrawerAccountRow(
                        account = account,
                        onClick = { onAccountClick(account) },
                    )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun DrawerAccountRow(
    account: Account,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(22.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (account) {
                is Account.Authenticated ->
                    DrawerAccountAvatar(
                        account = account,
                        modifier = Modifier.size(42.dp),
                    )
                is Account.Anonymous ->
                    DrawerAccountAvatar(
                        account = account,
                        modifier = Modifier.size(42.dp),
                    )
                is Account.Authorize ->
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_add_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                is Account.Group -> Unit
            }
            Text(
                text = accountTitle(account),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight =
                    if (account.isActiveUser) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
            )
        }
    }
}

@Composable
private fun DrawerNavigationSection(
    entries: List<DrawerEntry>,
    modifier: Modifier = Modifier,
    onNavigationClick: (DrawerEntry.Item) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        entries.forEach { entry ->
            when (entry) {
                is DrawerEntry.Header ->
                    Text(
                        text = stringResource(id = entry.titleRes),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                    )
                is DrawerEntry.Item ->
                    DrawerNavigationRowItem(
                        item = entry,
                        onClick = { onNavigationClick(entry) },
                    )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun DrawerNavigationRowItem(
    item: DrawerEntry.Item,
    onClick: () -> Unit,
) {
    val containerColor =
        if (item.isChecked) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        }
    val contentColor =
        if (item.isChecked) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = containerColor,
        shape = RoundedCornerShape(22.dp),
        tonalElevation = if (item.isChecked) 4.dp else 0.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = stringResource(id = item.titleRes),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (item.isChecked) FontWeight.SemiBold else FontWeight.Normal,
                color = contentColor,
            )
        }
    }
}

@Composable
private fun activeAccountTitle(account: Account?): String =
    when (account) {
        is Account.Authenticated -> account.userName.toString()
        is Account.Anonymous -> stringResource(id = account.titleRes)
        is Account.Authorize -> stringResource(id = account.titleRes)
        is Account.Group,
        null,
        -> stringResource(id = R.string.label_account_anonymous)
    }

@Composable
private fun accountTitle(account: Account): String =
    when (account) {
        is Account.Authenticated -> account.userName.toString()
        is Account.Anonymous -> stringResource(id = account.titleRes)
        is Account.Authorize -> stringResource(id = account.titleRes)
        is Account.Group -> stringResource(id = account.titleRes)
    }

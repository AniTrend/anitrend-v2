package co.anitrend.profile.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.compose.design.image.AniTrendImageDefaults
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toCoverImage
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.profile.component.viewmodel.state.ProfileState
import coil.transform.CircleCropTransformation

@Composable
private fun ProfileSections(
    user: User,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(8.dp)
                )
            },
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = CircleShape
                )
        ) {
            AniTrendImage(
                image = user.avatar,
                imageType = RequestImage.Media.ImageType.POSTER,
                onClick = onImageClick,
                transformations = listOf(CircleCropTransformation()),
                modifier = Modifier.size(48.dp)
            )
        }
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {

        }
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {

        }
        MarkdownText(
            content = user.status.about,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )
    }
}

@Composable
private fun ProfileContent(
    user: User,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AniTrendImage(
            image = user.avatar.banner.toCoverImage(),
            imageType = RequestImage.Media.ImageType.BANNER,
            onClick = onImageClick,
            modifier = AniTrendImageDefaults.BANNER_SIZE
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .absoluteOffset(y = (-16).dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            ProfileSections(
                user = user,
                onImageClick = onImageClick,
                modifier = Modifier.padding(16.dp).offset(y = (-38).dp)
            )
        }
    }
}

@Composable
fun ProfileScreenContent(
    profileState: ProfileState,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onFloatingActionButtonClick: (CharSequence?) -> Unit,
    onInboxButtonClick: () -> Unit,
    onNotificationsButtonClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state = profileState.model.observeAsState()
    val user: User = state.value ?: return

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                    // TODO: Add check if the current view is the authenticated user possible on the entity directly
                    IconButton(onClick = onNotificationsButtonClick) {
                        BadgedBox(
                            badge = {
                                // TODO: If we have unread notification we'll display this
                                Badge(modifier = Modifier.size(8.dp))
                            },
                        ) {
                            Icon(imageVector = Icons.Rounded.Notifications, contentDescription = null)
                        }
                    }
                    IconButton(onClick = onInboxButtonClick) {
                        Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { user.status.pageUrl?.run(onFloatingActionButtonClick) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Rounded.Share, "Share")
                    }
                },
            )
        }
    ) { innerPadding ->
        ProfileContent(
            user = user,
            onImageClick = onImageClick,
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun ProfileScreenPreview() {
    PreviewTheme {
        ProfileContent(
            user = User.empty(),
            onImageClick = {},
        )
    }
}

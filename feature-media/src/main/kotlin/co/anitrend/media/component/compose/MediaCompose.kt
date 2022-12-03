/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.media.component.compose

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ShareCompat
import co.anitrend.common.genre.ui.compose.GenresListComponent
import co.anitrend.common.markdown.ui.widget.MarkdownTextWidget
import co.anitrend.common.media.ui.compose.BannerSection
import co.anitrend.common.media.ui.compose.SummarySection
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.extensions.runIfAuthenticated
import co.anitrend.core.extensions.stackTrace
import co.anitrend.core.extensions.startViewIntent
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.media.R
import co.anitrend.media.component.viewmodel.state.MediaState
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import co.anitrend.navigation.extensions.startActivity
import org.koin.compose.koinInject

@Composable
private fun TagListItems(
    accentColor: Color,
    modifier: Modifier = Modifier,
    tags: List<Tag> = emptyList(),
    onClick: (Tag) -> Unit,
) {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = tags.size,
            key = { tags[it].id },
            contentType = { tags[it].category }
        ) { index ->
            val item = tags[index]
            Chip(
                onClick = { onClick(item) },
                colors = ChipDefaults.chipColors(
                    backgroundColor = accentColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = AniTrendTheme.typography.caption,
                )
            }
        }
    }
}


@Composable
private fun MediaDetailContent(
    media: Media,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    Column(modifier = modifier) {
        BannerSection(
            cover = media.image,
            modifier = Modifier.height(180.dp)
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
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                    )
            ) {
                SummarySection(
                    media = media,
                    accentColor = accentColor,
                    modifier = Modifier
                        .absoluteOffset(y = (-16).dp)
                )
                Spacer(Modifier.height(8.dp))
                GenresListComponent(
                    genres = media.genres.map { it },
                )
                Spacer(Modifier.height(8.dp))
                AndroidView(
                    factory = {
                        MarkdownTextWidget(context = it)
                    },
                    update = {
                        it.setText(media)
                    }
                )
                Spacer(Modifier.height(8.dp))
                TagListItems(
                    accentColor = accentColor,
                    tags = media.tags as List<Tag>,
                    onClick = { tag ->
                        MediaDiscoverRouter.startActivity(
                            view = view,
                            navPayload = MediaDiscoverRouter.Param(
                                tag = tag.name
                            ).asNavPayload()
                        )
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MediaScreenContent(
    mediaState: MediaState,
    settings: IUserSettings = koinInject(),
) {
    val state = mediaState.model.observeAsState()
    val media: Media = state.value ?: return

    val accentColor = media.rememberAccentColor()

    val context = LocalContext.current
    val view = LocalView.current

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { context.startViewIntent(Uri.parse(media.siteUrl.myAnimeList)) }) {
                        Icon(painter = painterResource(R.drawable.ic_my_anime_list), contentDescription = "Open my anime list")
                    }
                    IconButton(onClick = { view.openMediaListSheetFor(media, settings) }) {
                        val isOnMyList = media.mediaList != null
                        Icon(
                            imageVector = if (isOnMyList) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                            contentDescription = if (isOnMyList) "Manage media listing" else "Add to list",
                        )
                    }
                    IconButton(onClick = {
                        view.runIfAuthenticated(settings) {
                            // TODO: Add FavouriteTaskRouter to handle favourites of multiple types of media
                            FavouriteTaskRouter.forWorker()
                                .createOneTimeUniqueWorker(view.context, params = null)
                                .enqueue()
                        }
                    }) {
                        Icon(
                            imageVector = if (media.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (media.isFavourite) "Remove from favourites" else "Add to favourites",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            val intent = ShareCompat.IntentBuilder(context)
                                .setType("text/plain")
                                .setText(media.siteUrl.aniList)
                                .setSubject(media.title.userPreferred?.toString())
                                .createChooserIntent()
                            runCatching {
                                intent.run(context::startActivity)
                            }.stackTrace()
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Rounded.Share, "Share")
                    }
                },
            )
        },
    ) { innerPadding ->
        MediaDetailContent(
            media = media,
            accentColor = accentColor,
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun MediaDetailComponentPreview() {
    AniTrendTheme3 {
        MediaDetailContent(
            media = Media.Extended.empty(),
            accentColor = Color.DarkGray
        )
    }
}

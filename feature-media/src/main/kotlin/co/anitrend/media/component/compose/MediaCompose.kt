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

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.common.genre.ui.compose.GenresListComponent
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.common.media.ui.compose.SummarySection
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.compose.design.image.AniTrendImageDefaults
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.media.R
import co.anitrend.media.component.viewmodel.state.MediaState
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
private fun TagListItems(
    accentColor: Color,
    modifier: Modifier = Modifier,
    tags: List<Tag> = emptyList(),
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.Param) -> Unit,
) {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = tags.size,
            key = { tags[it].id },
            contentType = { "Tag" }
        ) { index ->
            tags[index].also { tag ->
                ElevatedSuggestionChip(
                    onClick = {
                        onMediaDiscoverableItemClick(
                            MediaDiscoverRouter.Param(tag = tag.name),
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = accentColor,
                    ),
                    label = {
                        Text(
                            text = tag.name,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = AniTrendTheme.typography.caption,
                        )
                    }
                )
            }
        }
    }
}


@Composable
private fun MediaDetailContent(
    media: Media,
    accentColor: Color,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.Param) -> Unit,
    onBannerClick: (ImageViewerRouter.Param) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.BANNER,
            onClick = onBannerClick,
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
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummarySection(
                    media = media,
                    accentColor = accentColor,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                    modifier = Modifier
                        .absoluteOffset(y = (-16).dp)
                )
                GenresListComponent(
                    genres = media.genres as List<Genre>,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                )
                MarkdownText(
                    synopsis = media,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
                TagListItems(
                    accentColor = accentColor,
                    tags = media.tags as List<Tag>,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick
                )
            }
        }
    }
}

@Composable
fun MediaScreenContent(
    mediaState: MediaState,
    onMyAnimeListButtonClick: (String) -> Unit,
    onBookmarkButtonClick: (View, Media) -> Unit,
    onFavouriteButtonClick: (View, FavouriteTaskRouter.Param) -> Unit,
    onFloatingActionButtonClick: (Media) -> Unit,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.Param) -> Unit,
    onBannerClick: (ImageViewerRouter.Param) -> Unit,
    onBackClick: () -> Unit,
) {
    val state = mediaState.model.observeAsState()
    val media: Media = state.value ?: return

    val accentColor = media.image.rememberAccentColor()

    val view = LocalView.current

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                    media.siteUrl.myAnimeList?.also { url ->
                        IconButton(onClick = { onMyAnimeListButtonClick(url) }) {
                            Icon(painter = painterResource(R.drawable.ic_my_anime_list), contentDescription = "Open my anime list")
                        }
                    }
                    IconButton(onClick = { onBookmarkButtonClick(view, media) }) {
                        val isOnMyList = media.mediaList != null
                        Icon(
                            imageVector = if (isOnMyList) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                            contentDescription = if (isOnMyList) "Manage media listing" else "Add to list",
                        )
                    }
                    IconButton(onClick = {
                        val param = FavouriteTaskRouter.Param.ToggleFavouriteState(
                            id = media.id,
                            mediaType = media.category.type
                        )
                        onFavouriteButtonClick(view, param)
                    }) {
                        Icon(
                            imageVector = if (media.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (media.isFavourite) "Remove from favourites" else "Add to favourites",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onFloatingActionButtonClick(media) },
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
            onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            onBannerClick = onBannerClick,
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun MediaDetailComponentPreview() {
    PreviewTheme {
        MediaDetailContent(
            media = Media.Extended.empty(),
            accentColor = Color.DarkGray,
            onMediaDiscoverableItemClick = {},
            onBannerClick = {}
        )
    }
}

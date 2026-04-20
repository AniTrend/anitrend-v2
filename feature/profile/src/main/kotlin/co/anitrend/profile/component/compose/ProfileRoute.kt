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
package co.anitrend.profile.component.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.profile.component.model.ProfileSectionState
import co.anitrend.profile.component.viewmodel.ProfileFeedViewModel
import co.anitrend.profile.component.viewmodel.ProfileOverviewViewModel
import co.anitrend.profile.component.viewmodel.ProfileStatsViewModel
import co.anitrend.profile.component.viewmodel.ProfileViewModel

@Composable
fun ProfileScreenContent(
    viewModel: ProfileViewModel,
    statsViewModel: ProfileStatsViewModel,
    overviewViewModel: ProfileOverviewViewModel,
    feedViewModel: ProfileFeedViewModel,
    authenticatedUserId: Long,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onMediaClick: (Long, MediaType?) -> Unit,
    onReviewClick: (Long, ScoreFormat?) -> Unit,
    onShareClick: (CharSequence?) -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val user by viewModel.model.observeAsState()
    val statsUser by statsViewModel.model.observeAsState()
    val statsLoadState by statsViewModel.loadState.observeAsState()
    val overview by overviewViewModel.model.observeAsState()
    val overviewLoadState by overviewViewModel.loadState.observeAsState()
    val feed by feedViewModel.model.observeAsState()
    val feedLoadState by feedViewModel.loadState.observeAsState()
    val currentUser = user ?: return
    val currentStatsUser = statsUser?.takeIf { it.id == currentUser.id }
    val currentStatsLoadState =
        if (statsUser != null && statsUser?.id != currentUser.id) {
            LoadState.Loading()
        } else {
            statsLoadState
        }

    ProfileRoute(
        user = currentUser,
        statsUser = currentStatsUser,
        statsLoadState = currentStatsLoadState,
        authenticatedUserId = authenticatedUserId,
        onImageClick = onImageClick,
        onMediaClick = onMediaClick,
        onReviewClick = onReviewClick,
        onShareClick = onShareClick,
        onNotificationsClick = onNotificationsClick,
        onSettingsClick = onSettingsClick,
        overview = overview,
        overviewLoadState = overviewLoadState,
        feed = feed,
        feedLoadState = feedLoadState,
        onStatsLoad = statsViewModel::load,
        onOverviewLoad = overviewViewModel::load,
        onFeedLoad = feedViewModel::load,
        onOverviewRetry = overviewViewModel::retryCurrent,
        onStatsRetry = statsViewModel::retryCurrent,
        onFeedRetry = feedViewModel::retryCurrent,
        onBackClick = onBackClick,
    )
}

@Composable
private fun ProfileRoute(
    user: User,
    statsUser: User.WithStats?,
    statsLoadState: LoadState?,
    authenticatedUserId: Long,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onMediaClick: (Long, MediaType?) -> Unit,
    onReviewClick: (Long, ScoreFormat?) -> Unit,
    onShareClick: (CharSequence?) -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    overview: ProfileOverview?,
    overviewLoadState: LoadState?,
    feed: ProfileFeed?,
    feedLoadState: LoadState?,
    onStatsLoad: (Long) -> Unit,
    onOverviewLoad: (Long) -> Unit,
    onFeedLoad: (Long) -> Unit,
    onOverviewRetry: () -> Unit,
    onStatsRetry: () -> Unit,
    onFeedRetry: () -> Unit,
    onBackClick: () -> Unit,
) {
    var selectedSurfaceTab by rememberSaveable { mutableStateOf(ProfileSurfaceTab.Overview) }
    var selectedMediaTab by rememberSaveable { mutableStateOf(ProfileMediaTab.Anime) }
    var selectedActivityFilter by rememberSaveable { mutableStateOf(ProfileActivityFilter.All) }
    val displayUser = statsUser ?: user
    val detailsState = profileDetailsSectionState(user = user, statsUser = statsUser)
    val statsState = profileStatsSectionState(statsUser = statsUser, loadState = statsLoadState, selectedTab = selectedMediaTab)
    val overviewState = profileOverviewSectionState(overview = overview, loadState = overviewLoadState)
    val feedState = profileFeedSectionState(feed = feed, loadState = feedLoadState)
    val isViewer =
        authenticatedUserId != IAuthenticationSettings.INVALID_USER_ID &&
            authenticatedUserId == user.id

    LaunchedEffect(user.id) {
        onStatsLoad(user.id)
        onOverviewLoad(user.id)
        onFeedLoad(user.id)
    }

    ProfileContent(
        user = user,
        displayUser = displayUser,
        detailsState = detailsState,
        overviewState = overviewState,
        statsState = statsState,
        feedState = feedState,
        selectedSurfaceTab = selectedSurfaceTab,
        selectedMediaTab = selectedMediaTab,
        selectedActivityFilter = selectedActivityFilter,
        isViewer = isViewer,
        onSurfaceTabSelected = { selectedSurfaceTab = it },
        onMediaTabSelected = { selectedMediaTab = it },
        onActivityFilterSelected = { selectedActivityFilter = it },
        onImageClick = onImageClick,
        onMediaSelected = onMediaClick,
        onReviewSelected = onReviewClick,
        onShareClick = onShareClick,
        onNotificationsClick = onNotificationsClick,
        onSettingsClick = onSettingsClick,
        onOverviewRetry = onOverviewRetry,
        onStatsRetry = onStatsRetry,
        onFeedRetry = onFeedRetry,
        onBackClick = onBackClick,
    )
}

private fun profileDetailsSectionState(
    user: User,
    statsUser: User.WithStats?,
): ProfileSectionState<ProfileDetails> =
    profileSectionStateOf(
        value = statsUser?.profileDetailsOrNull() ?: user.profileDetailsOrNull(),
        loadState = LoadState.Idle(),
        isEmpty = { details ->
            details.mediaListStats.isEmpty() && details.previousNames.isEmpty()
        },
    )

private fun profileStatsSectionState(
    statsUser: User.WithStats?,
    loadState: LoadState?,
    selectedTab: ProfileMediaTab,
): ProfileSectionState<Statistic> =
    profileSectionStateOf(
        value = statsUser?.statisticFor(selectedTab),
        loadState = loadState,
    )

private fun profileOverviewSectionState(
    overview: ProfileOverview?,
    loadState: LoadState?,
): ProfileSectionState<ProfileOverview> =
    profileSectionStateOf(
        value = overview,
        loadState = loadState,
        isEmpty = { value ->
            value.animeFavourites.isEmpty() && value.mangaFavourites.isEmpty() && value.recentActivity.isEmpty()
        },
    )

private fun profileFeedSectionState(
    feed: ProfileFeed?,
    loadState: LoadState?,
): ProfileSectionState<ProfileFeed> =
    profileSectionStateOf(
        value = feed,
        loadState = loadState,
        isEmpty = { value ->
            value.reviews.isEmpty() && value.listActivity.isEmpty()
        },
    )

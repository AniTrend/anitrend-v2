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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ProfileContent(
    user: User,
    displayUser: User,
    detailsState: ProfileSectionState<ProfileDetails>,
    overviewState: ProfileSectionState<ProfileOverview>,
    statsState: ProfileSectionState<Statistic>,
    feedState: ProfileSectionState<ProfileFeed>,
    selectedSurfaceTab: ProfileSurfaceTab,
    selectedMediaTab: ProfileMediaTab,
    selectedActivityFilter: ProfileActivityFilter,
    isViewer: Boolean,
    onSurfaceTabSelected: (ProfileSurfaceTab) -> Unit,
    onMediaTabSelected: (ProfileMediaTab) -> Unit,
    onActivityFilterSelected: (ProfileActivityFilter) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onMediaSelected: (Long, MediaType?) -> Unit,
    onReviewSelected: (Long, ScoreFormat?) -> Unit,
    onShareClick: (CharSequence?) -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onOverviewRetry: () -> Unit,
    onStatsRetry: () -> Unit,
    onFeedRetry: () -> Unit,
    onBackClick: () -> Unit,
) {
    val details = detailsState.state
    val scoreFormat = details?.listOption?.scoreFormat

    DefaultScaffold(
        onBackPress = onBackClick,
        showBottomBar = true,
        bottomBarActions = {
            if (isViewer) {
                IconButton(onClick = onNotificationsClick) {
                    Icon(
                        imageVector = Icons.TwoTone.Notifications,
                        contentDescription = stringResource(R.string.action_profile_open_notifications),
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.TwoTone.Settings,
                        contentDescription = stringResource(R.string.action_profile_open_settings),
                    )
                }
            }
            IconButton(
                onClick = { onShareClick(user.status.pageUrl) },
                enabled = !user.status.pageUrl.isNullOrBlank(),
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Share,
                    contentDescription = stringResource(R.string.action_profile_share),
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ProfileHeroSection(
                    user = displayUser,
                    details = details,
                    isViewer = isViewer,
                    onImageClick = onImageClick,
                )
            }

            stickyHeader {
                ProfileSurfaceTabSelector(
                    selectedTab = selectedSurfaceTab,
                    onTabSelected = onSurfaceTabSelected,
                )
            }

            when (selectedSurfaceTab) {
                ProfileSurfaceTab.Overview ->
                    item {
                        ProfileOverviewTab(
                            user = user,
                            displayUser = displayUser,
                            details = details,
                            overviewState = overviewState,
                            statsState = statsState,
                            onOpenStats = { onSurfaceTabSelected(ProfileSurfaceTab.Stats) },
                            onMediaSelected = onMediaSelected,
                        )
                    }

                ProfileSurfaceTab.Library -> {
                    item {
                        ProfileLibraryTab(
                            details = details,
                            overviewState = overviewState,
                            statsState = statsState,
                            selectedTab = selectedMediaTab,
                            onTabSelected = onMediaTabSelected,
                            onMediaSelected = onMediaSelected,
                            onOverviewRetry = onOverviewRetry,
                            onStatsRetry = onStatsRetry,
                        )
                    }
                }

                ProfileSurfaceTab.Stats -> {
                    item {
                        ProfileStatsTab(
                            state = statsState,
                            selectedTab = selectedMediaTab,
                            onTabSelected = onMediaTabSelected,
                            onRetry = onStatsRetry,
                        )
                    }
                }

                ProfileSurfaceTab.Activity ->
                    item {
                        ProfileFeedTab(
                            state = feedState,
                            selectedFilter = selectedActivityFilter,
                            scoreFormat = scoreFormat,
                            onFilterSelected = onActivityFilterSelected,
                            onMediaSelected = onMediaSelected,
                            onReviewSelected = onReviewSelected,
                            onRetry = onFeedRetry,
                        )
                    }
            }
        }
    }
}

@Composable
private fun ProfileSurfaceTabSelector(
    selectedTab: ProfileSurfaceTab,
    onTabSelected: (ProfileSurfaceTab) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SectionHorizontalPadding, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileSurfaceTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text =
                            when (tab) {
                                ProfileSurfaceTab.Overview -> stringResource(R.string.label_profile_surface_tab_overview)
                                ProfileSurfaceTab.Library -> stringResource(R.string.label_profile_surface_tab_library)
                                ProfileSurfaceTab.Stats -> stringResource(R.string.label_profile_surface_tab_stats)
                                ProfileSurfaceTab.Activity -> stringResource(R.string.label_profile_surface_tab_activity)
                            },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Box(
                        modifier =
                            Modifier
                                .width(28.dp)
                                .height(3.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.Transparent
                                    },
                                ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    val previewUser = previewProfileUser()

    PreviewTheme(darkTheme = true, wrapInSurface = true) {
        ProfileContent(
            user = previewUser,
            displayUser = previewUser,
            detailsState = ProfileSectionState.Content(requireNotNull(previewUser.profileDetailsOrNull())),
            overviewState = ProfileSectionState.Content(previewProfileOverview()),
            statsState = ProfileSectionState.Content(requireNotNull(previewUser.statisticFor(ProfileMediaTab.Anime))),
            feedState = ProfileSectionState.Content(previewProfileFeed()),
            selectedSurfaceTab = ProfileSurfaceTab.Overview,
            selectedMediaTab = ProfileMediaTab.Anime,
            selectedActivityFilter = ProfileActivityFilter.All,
            isViewer = true,
            onSurfaceTabSelected = {},
            onMediaTabSelected = {},
            onActivityFilterSelected = {},
            onImageClick = {},
            onMediaSelected = { _, _ -> },
            onReviewSelected = { _, _ -> },
            onShareClick = { _ -> },
            onNotificationsClick = {},
            onSettingsClick = {},
            onOverviewRetry = {},
            onStatsRetry = {},
            onFeedRetry = {},
            onBackClick = {},
        )
    }
}

internal fun previewProfileOverview(): ProfileOverview =
    ProfileOverview(
        animeFavourites =
            listOf(
                previewMediaPreview(id = 101L, title = "Monster", type = MediaType.ANIME),
                previewMediaPreview(id = 102L, title = "Frieren", type = MediaType.ANIME),
                previewMediaPreview(id = 103L, title = "Vinland Saga", type = MediaType.ANIME),
            ),
        mangaFavourites =
            listOf(
                previewMediaPreview(id = 201L, title = "Oyasumi Punpun", type = MediaType.MANGA),
                previewMediaPreview(id = 202L, title = "Blue Period", type = MediaType.MANGA),
            ),
        recentActivity =
            listOf(
                ProfileOverview.ListActivityPreview(
                    id = 1L,
                    createdAt = 1_744_998_000L,
                    status = "Completed",
                    progress = "26 of 26",
                    siteUrl = null,
                    type = co.anitrend.domain.status.enums.StatusType.ANIME_LIST,
                    media = previewMediaPreview(id = 102L, title = "Frieren", type = MediaType.ANIME),
                    mediaListStatus = MediaListStatus.COMPLETED,
                    mediaListProgress = 26,
                    mediaListVolumeProgress = null,
                ),
                ProfileOverview.ListActivityPreview(
                    id = 2L,
                    createdAt = 1_744_912_000L,
                    status = "Read",
                    progress = "98 chapters",
                    siteUrl = null,
                    type = co.anitrend.domain.status.enums.StatusType.MANGA_LIST,
                    media = previewMediaPreview(id = 202L, title = "Blue Period", type = MediaType.MANGA),
                    mediaListStatus = MediaListStatus.CURRENT,
                    mediaListProgress = 98,
                    mediaListVolumeProgress = 15,
                ),
                ProfileOverview.ListActivityPreview(
                    id = 3L,
                    createdAt = 1_744_800_000L,
                    status = "Started",
                    progress = "1 of 12",
                    siteUrl = null,
                    type = co.anitrend.domain.status.enums.StatusType.ANIME_LIST,
                    media = previewMediaPreview(id = 103L, title = "Vinland Saga", type = MediaType.ANIME),
                    mediaListStatus = MediaListStatus.CURRENT,
                    mediaListProgress = 1,
                    mediaListVolumeProgress = null,
                ),
            ),
    )

internal fun previewProfileFeed(): ProfileFeed =
    ProfileFeed(
        reviews =
            listOf(
                ProfileFeed.ReviewPreview(
                    id = 81L,
                    summary = "A restrained review about grief, patience, and the emotional weight of quiet fantasy storytelling.",
                    score = 88,
                    rating = 124,
                    ratingAmount = 31,
                    siteUrl = "https://anilist.co/review/81",
                    createdAt = 1_744_980_000L,
                    updatedAt = 1_744_985_000L,
                    mediaId = 102L,
                    mediaType = MediaType.ANIME,
                    media = previewMediaPreview(id = 102L, title = "Frieren", type = MediaType.ANIME),
                ),
                ProfileFeed.ReviewPreview(
                    id = 82L,
                    summary = "The direction is exacting, the tension never breaks, and the ending lands without theatrics.",
                    score = 92,
                    rating = 92,
                    ratingAmount = 21,
                    siteUrl = "https://anilist.co/review/82",
                    createdAt = 1_744_700_000L,
                    updatedAt = 1_744_701_000L,
                    mediaId = 101L,
                    mediaType = MediaType.ANIME,
                    media = previewMediaPreview(id = 101L, title = "Monster", type = MediaType.ANIME),
                ),
            ),
        listActivity = previewProfileOverview().recentActivity,
    )

private fun previewMediaPreview(
    id: Long,
    title: String,
    type: MediaType,
): ProfileOverview.MediaPreview =
    ProfileOverview.MediaPreview(
        id = id,
        title = co.anitrend.domain.media.entity.attribute.title.MediaTitle(
            romaji = title,
            english = title,
            native = null,
            userPreferred = title,
        ),
        image =
            co.anitrend.domain.media.entity.attribute.image.MediaImage(
                color = null,
                extraLarge = null,
                large = null,
                medium = null,
                banner = null,
            ),
        type = type,
        format = if (type == MediaType.ANIME) co.anitrend.domain.media.enums.MediaFormat.TV else co.anitrend.domain.media.enums.MediaFormat.MANGA,
        status = co.anitrend.domain.media.enums.MediaStatus.FINISHED,
        episodes = if (type == MediaType.ANIME) 24 else 0,
        chapters = if (type == MediaType.MANGA) 98 else 0,
        volumes = if (type == MediaType.MANGA) 15 else 0,
        isFavourite = true,
        meanScore = 84,
        averageScore = 82,
        siteUrl = null,
    )

internal fun previewProfileUser(): User.WithStats =
    User.WithStats(
        previousNames =
            listOf(
                User.PreviousName(
                    createdAt = 1_640_995_200L,
                    name = "OldTrend",
                    updatedAt = 1_672_531_200L,
                ),
                User.PreviousName(
                    createdAt = 1_609_459_200L,
                    name = "TrendClassic",
                    updatedAt = 1_640_995_200L,
                ),
            ),
        listOption =
            co.anitrend.domain.user.entity.attribute.option.UserMediaListOption(
                scoreFormat = ScoreFormat.POINT_100,
                rowOrder = "Score",
                animeList =
                    co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions(
                        splitCompletedSectionByFormat = false,
                        customLists = listOf("Favorites"),
                        sectionOrder = listOf("Watching", "Completed"),
                        advancedScoring = emptyList(),
                        advancedScoringEnabled = false,
                    ),
                mangaList =
                    co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions(
                        splitCompletedSectionByFormat = false,
                        customLists = listOf("Collector"),
                        sectionOrder = listOf("Reading", "Completed"),
                        advancedScoring = emptyList(),
                        advancedScoringEnabled = false,
                    ),
            ),
        profileOption = UserProfileOption(profileColor = "blue"),
        statistics =
            co.anitrend.domain.user.entity.attribute.statistic.UserMediaStatisticType(
                anime =
                    Statistic.Anime(
                        minutesWatched = 24_500,
                        episodesWatched = 930,
                        count = 182,
                        meanScore = 81.6f,
                        standardDeviation = 12.4f,
                        countries = null,
                        formats = null,
                        genres =
                            listOf(
                                MediaStatistic.Anime.Genre("Drama", 44, 82f, listOf(1L), 8_400),
                                MediaStatistic.Anime.Genre("Mystery", 33, 85f, listOf(2L), 6_400),
                                MediaStatistic.Anime.Genre("Sci Fi", 26, 79f, listOf(3L), 4_500),
                            ),
                        lengths = null,
                        releaseYears = null,
                        scores =
                            listOf(
                                MediaStatistic.Anime.Score(60, 10, 60f, listOf(1L), 1_200),
                                MediaStatistic.Anime.Score(70, 30, 70f, listOf(2L), 4_300),
                                MediaStatistic.Anime.Score(80, 64, 80f, listOf(3L), 9_800),
                                MediaStatistic.Anime.Score(90, 52, 90f, listOf(4L), 7_500),
                            ),
                        staff = null,
                        startYears = null,
                        statuses =
                            listOf(
                                MediaStatistic.Anime.Status(MediaListStatus.COMPLETED, 98, 83f, listOf(1L), 14_000),
                                MediaStatistic.Anime.Status(MediaListStatus.CURRENT, 18, 79f, listOf(2L), 2_800),
                                MediaStatistic.Anime.Status(MediaListStatus.PLANNING, 52, 0f, listOf(3L), 0),
                                MediaStatistic.Anime.Status(MediaListStatus.DROPPED, 14, 55f, listOf(4L), 700),
                            ),
                        studios = null,
                        tags = null,
                        voiceActors = null,
                    ),
                manga =
                    Statistic.Manga(
                        chaptersRead = 1_480,
                        volumesRead = 132,
                        count = 76,
                        meanScore = 77.1f,
                        standardDeviation = 9.8f,
                        countries = null,
                        formats = null,
                        genres =
                            listOf(
                                MediaStatistic.Manga.Genre("Slice of Life", 21, 78f, listOf(5L), 420),
                                MediaStatistic.Manga.Genre("Romance", 18, 80f, listOf(6L), 390),
                            ),
                        lengths = null,
                        releaseYears = null,
                        scores =
                            listOf(
                                MediaStatistic.Manga.Score(70, 16, 70f, listOf(7L), 220),
                                MediaStatistic.Manga.Score(80, 33, 80f, listOf(8L), 610),
                                MediaStatistic.Manga.Score(90, 27, 90f, listOf(9L), 650),
                            ),
                        staff = null,
                        startYears = null,
                        statuses =
                            listOf(
                                MediaStatistic.Manga.Status(MediaListStatus.COMPLETED, 29, 81f, listOf(10L), 520),
                                MediaStatistic.Manga.Status(MediaListStatus.CURRENT, 12, 75f, listOf(11L), 180),
                                MediaStatistic.Manga.Status(MediaListStatus.PLANNING, 28, 0f, listOf(12L), 0),
                            ),
                        studios = null,
                        tags = null,
                        voiceActors = null,
                    ),
            ),
        mediaListStats =
            listOf(
                MediaListInfo(false, MediaType.ANIME, "Watching", 18),
                MediaListInfo(false, MediaType.ANIME, "Completed", 98),
                MediaListInfo(false, MediaType.ANIME, "Planning", 52),
                MediaListInfo(true, MediaType.ANIME, "Favorites", 12),
                MediaListInfo(false, MediaType.MANGA, "Reading", 12),
                MediaListInfo(false, MediaType.MANGA, "Completed", 29),
                MediaListInfo(false, MediaType.MANGA, "Planning", 28),
                MediaListInfo(true, MediaType.MANGA, "Collector", 6),
            ),
        name = "AniTrend",
        avatar =
            co.anitrend.domain.user.entity.contract.UserImage(
                large = null,
                medium = null,
                banner = null,
            ),
        status =
            co.anitrend.domain.user.entity.contract.UserStatus(
                about = "Markdown-ready bio with **bold takes**, longform notes, and profile details.",
                donationBadge = "Supporter",
                donationTier = 2,
                isFollowing = true,
                isFollower = true,
                isBlocked = false,
                pageUrl = "https://anilist.co/user/AniTrend",
                createdAt = 1_577_836_800L,
                updatedAt = 1_743_724_800L,
            ),
        id = 42L,
    )

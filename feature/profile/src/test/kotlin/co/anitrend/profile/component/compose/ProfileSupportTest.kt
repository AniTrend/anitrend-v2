package co.anitrend.profile.component.compose

import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.attribute.statistic.UserMediaStatisticType
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.profile.component.model.ProfileSectionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfileSupportTest {

    @Test
    fun `profileSectionStateOf keeps stale content visible while surfacing errors`() {
        val state =
            profileSectionStateOf(
                value = "AniTrend",
                loadState = LoadState.Error(details = IllegalStateException("boom")),
            )

        assertIs<ProfileSectionState.Partial<String>>(state)
        assertEquals("AniTrend", state.state)
    }

    @Test
    fun `profileSectionStateOf returns empty when settled data has no display content`() {
        val state =
            profileSectionStateOf(
                value = emptyList<String>(),
                loadState = LoadState.Idle(),
                isEmpty = List<String>::isEmpty,
            )

        assertEquals(ProfileSectionState.Empty, state)
    }

    @Test
    fun `profileSectionStateOf returns loading until the first payload is available`() {
        val state = profileSectionStateOf<String>(value = null, loadState = LoadState.Loading())

        assertEquals(ProfileSectionState.Loading, state)
    }

    @Test
    fun `profileSectionStateOf returns content for settled non-empty values`() {
        val state = profileSectionStateOf(value = "AniTrend", loadState = LoadState.Idle())

        assertEquals(ProfileSectionState.Content("AniTrend"), state)
    }

    @Test
    fun `profile details expose grouped media list sections for the selected media type`() {
        val user =
            createUserWithStats(
                mediaListStats =
                    listOf(
                        MediaListInfo(isCustomList = false, mediaType = MediaType.ANIME, name = "Watching", count = 12),
                        MediaListInfo(isCustomList = true, mediaType = MediaType.ANIME, name = "Favorites", count = 4),
                        MediaListInfo(isCustomList = false, mediaType = MediaType.MANGA, name = "Reading", count = 6),
                    ),
            )

        val sections = requireNotNull(user.profileDetailsOrNull()).mediaListSections(ProfileMediaTab.Anime)

        assertEquals(listOf("Watching"), sections.primary.map(MediaListInfo::name))
        assertEquals(listOf("Favorites"), sections.custom.map(MediaListInfo::name))
    }

    @Test
    fun `statisticFor returns the statistic matching the selected media tab`() {
        val animeStatistic = createAnimeStatistic(count = 120, meanScore = 82f)
        val mangaStatistic = createMangaStatistic(count = 48, meanScore = 74f)
        val user =
            createUserWithStats(
                animeStatistic = animeStatistic,
                mangaStatistic = mangaStatistic,
            )

        assertEquals(animeStatistic, user.statisticFor(ProfileMediaTab.Anime))
        assertEquals(mangaStatistic, user.statisticFor(ProfileMediaTab.Manga))
    }

    @Test
    fun `scoreEntries merge duplicate buckets and sort ascending`() {
        val statistic =
            createAnimeStatistic(
                scores =
                    listOf(
                        MediaStatistic.Anime.Score(score = 80, count = 12, meanScore = 81f, mediaIds = listOf(1L), minutesWatched = 10),
                        MediaStatistic.Anime.Score(score = 10, count = 3, meanScore = 20f, mediaIds = listOf(2L), minutesWatched = 4),
                        MediaStatistic.Anime.Score(score = 80, count = 9, meanScore = 79f, mediaIds = listOf(3L), minutesWatched = 6),
                        MediaStatistic.Anime.Score(score = 60, count = 0, meanScore = 0f, mediaIds = emptyList(), minutesWatched = 0),
                    ),
            )

        assertEquals(
            listOf(
                ProfileScoreEntry(score = 10, amount = 3),
                ProfileScoreEntry(score = 80, amount = 21),
            ),
            statistic.scoreEntries(),
        )
    }

    @Test
    fun `statusEntries sort by amount and place unknown last on ties`() {
        val statistic =
            createAnimeStatistic(
                statuses =
                    listOf(
                        MediaStatistic.Anime.Status(
                            status = MediaListStatus.COMPLETED,
                            count = 30,
                            meanScore = 82f,
                            mediaIds = listOf(1L),
                            minutesWatched = 20,
                        ),
                        MediaStatistic.Anime.Status(
                            status = MediaListStatus.CURRENT,
                            count = 12,
                            meanScore = 77f,
                            mediaIds = listOf(2L),
                            minutesWatched = 8,
                        ),
                        MediaStatistic.Anime.Status(
                            status = MediaListStatus.PLANNING,
                            count = 12,
                            meanScore = 0f,
                            mediaIds = listOf(3L),
                            minutesWatched = 0,
                        ),
                    ),
            )

        assertEquals(
            listOf(
                ProfileStatusEntry(status = MediaListStatus.COMPLETED, amount = 30, fraction = 30f / 54f),
                ProfileStatusEntry(status = MediaListStatus.CURRENT, amount = 12, fraction = 12f / 54f),
                ProfileStatusEntry(status = MediaListStatus.PLANNING, amount = 12, fraction = 12f / 54f),
            ),
            statistic.statusEntries(),
        )
    }

    @Test
    fun `axisValues returns a compact two point scale`() {
        val values =
            listOf(
                ProfileScoreEntry(score = 10, amount = 3),
                ProfileScoreEntry(score = 40, amount = 6),
                ProfileScoreEntry(score = 80, amount = 104),
            ).axisValues(labelCount = 2)

        assertEquals(listOf(120, 0), values)
    }

    @Test
    fun `profile details are absent for lightweight user models`() {
        val user =
            User.Core(
                name = "AniTrend",
                avatar = UserImage.empty(),
                status = UserStatus.empty(),
                id = 1L,
            )

        assertNull(user.profileDetailsOrNull())
    }

    @Test
    fun `hero meta items only keep joined and updated context`() {
        val user =
            createUserWithStats(
                status =
                    UserStatus(
                        about = null,
                        donationBadge = null,
                        donationTier = null,
                        isFollowing = false,
                        isFollower = false,
                        isBlocked = false,
                        pageUrl = null,
                        createdAt = 1_700_000_000L,
                        updatedAt = 1_700_086_400L,
                    ),
            )

        assertEquals(
            listOf(ProfileHeroMetaKind.Joined, ProfileHeroMetaKind.Updated),
            user.heroMetaItems().map(ProfileHeroMetaItem::kind),
        )
    }

    @Test
    fun `favourite groups keep anime first and omit empty categories`() {
        val overview =
            ProfileOverview(
                animeFavourites = listOf(createMediaPreview(1L, "Monster", MediaType.ANIME)),
                mangaFavourites = listOf(createMediaPreview(2L, "Punpun", MediaType.MANGA)),
                recentActivity = emptyList(),
            )

        assertEquals(
            listOf(ProfileMediaTab.Anime, ProfileMediaTab.Manga),
            overview.favouriteGroups().map(Pair<ProfileMediaTab, List<ProfileOverview.MediaPreview>>::first),
        )
        assertEquals(1L, overview.leadFavourite()?.id)
    }

    @Test
    fun `favourites rail keeps category order and respects the requested limit`() {
        val overview =
            ProfileOverview(
                animeFavourites =
                    listOf(
                        createMediaPreview(1L, "Monster", MediaType.ANIME),
                        createMediaPreview(2L, "Frieren", MediaType.ANIME),
                    ),
                mangaFavourites =
                    listOf(
                        createMediaPreview(3L, "Punpun", MediaType.MANGA),
                        createMediaPreview(4L, "Blue Period", MediaType.MANGA),
                    ),
                recentActivity = emptyList(),
            )

        assertEquals(listOf(1L, 2L, 3L), overview.favouritesRail(limit = 3).map(ProfileOverview.MediaPreview::id))
    }

    @Test
    fun `overview lead surface prefers favourites then recent activity then quiet`() {
        val favouriteOverview =
            ProfileOverview(
                animeFavourites = listOf(createMediaPreview(1L, "Monster", MediaType.ANIME)),
                mangaFavourites = emptyList(),
                recentActivity = listOf(createListActivity(id = 5L, createdAt = 10L)),
            )
        val recentOnlyOverview =
            ProfileOverview(
                animeFavourites = emptyList(),
                mangaFavourites = emptyList(),
                recentActivity = listOf(createListActivity(id = 7L, createdAt = 30L)),
            )
        val quietOverview =
            ProfileOverview(
                animeFavourites = emptyList(),
                mangaFavourites = emptyList(),
                recentActivity = emptyList(),
            )

        assertEquals(ProfileOverviewLeadSurface.Favourites, favouriteOverview.leadSurface())
        assertEquals(ProfileOverviewLeadSurface.RecentActivity, recentOnlyOverview.leadSurface())
        assertEquals(ProfileOverviewLeadSurface.Quiet, quietOverview.leadSurface())
    }

    @Test
    fun `recent activity preview sorts newest first and limits rows`() {
        val overview =
            ProfileOverview(
                animeFavourites = emptyList(),
                mangaFavourites = emptyList(),
                recentActivity =
                    listOf(
                        createListActivity(id = 1L, createdAt = 10L),
                        createListActivity(id = 2L, createdAt = 30L),
                        createListActivity(id = 3L, createdAt = 20L),
                        createListActivity(id = 4L, createdAt = 40L),
                    ),
            )

        assertEquals(listOf(4L, 2L, 3L), overview.recentActivityPreview().map(ProfileOverview.ListActivityPreview::id))
    }

    @Test
    fun `recent library activity filters by media tab and limits rows`() {
        val overview =
            ProfileOverview(
                animeFavourites = emptyList(),
                mangaFavourites = emptyList(),
                recentActivity =
                    listOf(
                        createListActivity(id = 1L, createdAt = 10L, mediaType = MediaType.ANIME),
                        createListActivity(id = 2L, createdAt = 50L, mediaType = MediaType.MANGA),
                        createListActivity(id = 3L, createdAt = 40L, mediaType = MediaType.ANIME),
                        createListActivity(id = 4L, createdAt = 30L, mediaType = MediaType.ANIME),
                        createListActivity(id = 5L, createdAt = 20L, mediaType = MediaType.ANIME),
                    ),
            )

        assertEquals(
            listOf(3L, 4L, 5L, 1L),
            overview.recentLibraryActivity(ProfileMediaTab.Anime).map(ProfileOverview.ListActivityPreview::id),
        )
        assertEquals(
            listOf(2L),
            overview.recentLibraryActivity(ProfileMediaTab.Manga).map(ProfileOverview.ListActivityPreview::id),
        )
    }

    @Test
    fun `library pulse summary ignores custom lists and prefers stat footprint when available`() {
        val user =
            createUserWithStats(
                mediaListStats =
                    listOf(
                        MediaListInfo(isCustomList = false, mediaType = MediaType.ANIME, name = "Watching", count = 18),
                        MediaListInfo(isCustomList = false, mediaType = MediaType.ANIME, name = "Completed", count = 12),
                        MediaListInfo(isCustomList = true, mediaType = MediaType.ANIME, name = "Favorites", count = 99),
                        MediaListInfo(isCustomList = false, mediaType = MediaType.MANGA, name = "Reading", count = 7),
                    ),
            )

        val summary = requireNotNull(user.profileDetailsOrNull()).libraryPulseSummary(displayUser = user)

        assertEquals(30, summary.animeTotal)
        assertEquals(7, summary.mangaTotal)
        assertEquals("Watching", summary.dominantStatus)
        assertTrue(summary.progressFootprint.contains("min"))
        assertTrue(summary.progressFootprint.contains("ch"))
    }

    @Test
    fun `feed helpers split reviews and list updates by filter`() {
        val feed =
            ProfileFeed(
                reviews =
                    listOf(
                        createReviewPreview(id = 9L, createdAt = 20L),
                        createReviewPreview(id = 10L, createdAt = 40L),
                    ),
                listActivity =
                    listOf(
                        createListActivity(id = 11L, createdAt = 15L),
                        createListActivity(id = 12L, createdAt = 35L),
                    ),
            )

        assertEquals(listOf(10L, 9L), feed.filteredReviews(ProfileActivityFilter.All).map(ProfileFeed.ReviewPreview::id))
        assertEquals(listOf(10L), feed.reviewSpotlight(ProfileActivityFilter.Reviews)?.let { listOf(it.id) })
        assertEquals(listOf(9L), feed.reviewArchive(ProfileActivityFilter.Reviews).map(ProfileFeed.ReviewPreview::id))
        assertEquals(emptyList(), feed.filteredReviews(ProfileActivityFilter.ListUpdates))
        assertEquals(listOf(12L, 11L), feed.filteredListUpdates(ProfileActivityFilter.ListUpdates).map(ProfileOverview.ListActivityPreview::id))
    }

    @Test
    fun `preferred hero chart uses score distribution first and falls back to status`() {
        val scoreAndStatusStatistic =
            createAnimeStatistic(
                scores = listOf(MediaStatistic.Anime.Score(score = 80, count = 12, meanScore = 80f, mediaIds = listOf(1L), minutesWatched = 1)),
                statuses =
                    listOf(
                        MediaStatistic.Anime.Status(
                            status = MediaListStatus.COMPLETED,
                            count = 12,
                            meanScore = 80f,
                            mediaIds = listOf(1L),
                            minutesWatched = 1,
                        ),
                    ),
            )
        val statusOnlyStatistic =
            createAnimeStatistic(
                scores = emptyList(),
                statuses =
                    listOf(
                        MediaStatistic.Anime.Status(
                            status = MediaListStatus.CURRENT,
                            count = 9,
                            meanScore = 70f,
                            mediaIds = listOf(2L),
                            minutesWatched = 1,
                        ),
                    ),
            )

        assertEquals(ProfileStatsChart.ScoreDistribution, scoreAndStatusStatistic.preferredHeroChart())
        assertEquals(ProfileStatsChart.StatusDistribution, scoreAndStatusStatistic.secondaryChart())
        assertEquals(ProfileStatsChart.StatusDistribution, statusOnlyStatistic.preferredHeroChart())
        assertNull(statusOnlyStatistic.secondaryChart())
    }

    private fun createUserWithStats(
        mediaListStats: List<MediaListInfo> = emptyList(),
        animeStatistic: Statistic.Anime? = createAnimeStatistic(),
        mangaStatistic: Statistic.Manga? = createMangaStatistic(),
        status: UserStatus = UserStatus.empty(),
    ): User.WithStats =
        User.WithStats(
            previousNames = emptyList(),
            listOption =
                UserMediaListOption(
                    scoreFormat = ScoreFormat.POINT_100,
                    rowOrder = "Score",
                    animeList =
                        UserMediaListTypeOptions(
                            splitCompletedSectionByFormat = false,
                            customLists = listOf("Favorites"),
                            sectionOrder = listOf("Watching", "Completed"),
                            advancedScoring = emptyList(),
                            advancedScoringEnabled = false,
                        ),
                    mangaList =
                        UserMediaListTypeOptions(
                            splitCompletedSectionByFormat = false,
                            customLists = listOf("To Buy"),
                            sectionOrder = listOf("Reading", "Completed"),
                            advancedScoring = emptyList(),
                            advancedScoringEnabled = false,
                        ),
                ),
            profileOption = UserProfileOption(profileColor = "blue"),
            statistics =
                UserMediaStatisticType(
                    anime = animeStatistic,
                    manga = mangaStatistic,
                ),
            mediaListStats = mediaListStats,
            name = "AniTrend",
            avatar = UserImage.empty(),
            status = status,
            id = 42L,
        )

    private fun createAnimeStatistic(
        count: Int = 120,
        meanScore: Float = 82f,
        standardDeviation: Float = 11f,
        scores: List<MediaStatistic.Anime.Score>? = null,
        statuses: List<MediaStatistic.Anime.Status>? = null,
    ): Statistic.Anime =
        Statistic.Anime(
            minutesWatched = 16_500,
            episodesWatched = 540,
            count = count,
            meanScore = meanScore,
            standardDeviation = standardDeviation,
            countries = null,
            formats = null,
            genres = null,
            lengths = null,
            releaseYears = null,
            scores = scores,
            staff = null,
            startYears = null,
            statuses = statuses,
            studios = null,
            tags = null,
            voiceActors = null,
        )

    private fun createMangaStatistic(
        count: Int = 48,
        meanScore: Float = 74f,
        standardDeviation: Float = 9f,
    ): Statistic.Manga =
        Statistic.Manga(
            chaptersRead = 1_280,
            volumesRead = 126,
            count = count,
            meanScore = meanScore,
            standardDeviation = standardDeviation,
            countries = null,
            formats = null,
            genres = null,
            lengths = null,
            releaseYears = null,
            scores = null,
            staff = null,
            startYears = null,
            statuses = null,
            studios = null,
            tags = null,
            voiceActors = null,
        )

    private fun createMediaPreview(
        id: Long,
        title: String,
        type: MediaType,
    ) =
        ProfileOverview.MediaPreview(
            id = id,
            title = MediaTitle(romaji = title, english = title, native = null, userPreferred = title),
            image = MediaImage(color = null, extraLarge = null, large = null, medium = null, banner = null),
            type = type,
            format = null,
            status = null,
            episodes = 0,
            chapters = 0,
            volumes = 0,
            isFavourite = true,
            meanScore = 80,
            averageScore = 81,
            siteUrl = null,
        )

    private fun createListActivity(
        id: Long,
        createdAt: Long,
        mediaType: MediaType = MediaType.ANIME,
    ) =
        ProfileOverview.ListActivityPreview(
            id = id,
            createdAt = createdAt,
            status = "Completed",
            progress = "12 of 12",
            siteUrl = null,
            type = null,
            media = createMediaPreview(id = id + 100, title = "Media $id", type = mediaType),
            mediaListStatus = MediaListStatus.COMPLETED,
            mediaListProgress = 12,
            mediaListVolumeProgress = null,
        )

    private fun createReviewPreview(
        id: Long,
        createdAt: Long,
    ) =
        ProfileFeed.ReviewPreview(
            id = id,
            summary = "Review $id",
            score = 80,
            rating = 10,
            ratingAmount = 4,
            siteUrl = "https://example.com/review/$id",
            createdAt = createdAt,
            updatedAt = createdAt,
            mediaId = id + 100,
            mediaType = MediaType.ANIME,
            media = createMediaPreview(id = id + 100, title = "Review Media $id", type = MediaType.ANIME),
        )
}

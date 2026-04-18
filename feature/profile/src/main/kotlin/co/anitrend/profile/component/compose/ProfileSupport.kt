package co.anitrend.profile.component.compose

import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.profile.component.model.ProfileSectionState
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

internal enum class ProfileMediaTab(
    val mediaType: MediaType,
) {
    Anime(MediaType.ANIME),
    Manga(MediaType.MANGA),
}

internal enum class ProfileSurfaceTab {
    Overview,
    Library,
    Stats,
    Activity,
}

internal enum class ProfileActivityFilter {
    All,
    Reviews,
    ListUpdates,
}

internal enum class ProfileStatsChart {
    ScoreDistribution,
    StatusDistribution,
}

internal enum class ProfileHeroMetaKind {
    Joined,
    Updated,
}

internal enum class ProfileOverviewLeadSurface {
    Favourites,
    RecentActivity,
    Quiet,
}

internal data class ProfileDetails(
    val previousNames: List<User.PreviousName>,
    val listOption: UserMediaListOption,
    val profileOption: UserProfileOption,
    val mediaListStats: List<MediaListInfo>,
)

internal data class ProfileMediaListSections(
    val primary: List<MediaListInfo>,
    val custom: List<MediaListInfo>,
)

internal data class ProfileScoreEntry(
    val score: Int,
    val amount: Int,
)

internal data class ProfileStatusEntry(
    val status: MediaListStatus?,
    val amount: Int,
    val fraction: Float,
)

internal data class ProfileHeroMetaItem(
    val kind: ProfileHeroMetaKind,
    val value: String,
)

internal data class ProfileLibraryPulseSummary(
    val animeTotal: Int,
    val mangaTotal: Int,
    val progressFootprint: String,
    val dominantStatus: String,
)

internal fun User.profileDetailsOrNull(): ProfileDetails? =
    when (this) {
        is User.Extended ->
            ProfileDetails(
                previousNames = previousNames,
                listOption = listOption,
                profileOption = profileOption,
                mediaListStats = mediaListInfo,
            )
        is User.WithStats ->
            ProfileDetails(
                previousNames = previousNames,
                listOption = listOption,
                profileOption = profileOption,
                mediaListStats = mediaListStats,
            )
        else -> null
    }

internal fun ProfileDetails.mediaListSections(tab: ProfileMediaTab): ProfileMediaListSections {
    val filtered =
        mediaListStats
            .asSequence()
            .filter { it.mediaType == tab.mediaType && it.count > 0 }
            .sortedWith(
                compareByDescending<MediaListInfo> { it.count }
                    .thenBy(MediaListInfo::name),
            ).toList()

    return ProfileMediaListSections(
        primary = filtered.filterNot(MediaListInfo::isCustomList),
        custom = filtered.filter(MediaListInfo::isCustomList),
    )
}

internal fun User.statisticFor(tab: ProfileMediaTab): Statistic? =
    when (this) {
        is User.WithStats ->
            when (tab) {
                ProfileMediaTab.Anime -> statistics.anime
                ProfileMediaTab.Manga -> statistics.manga
            }
        else -> null
    }

    internal fun User.heroMetaItems(): List<ProfileHeroMetaItem> =
        buildList {
            formatEpochDate(status.createdAt)?.let {
                add(ProfileHeroMetaItem(kind = ProfileHeroMetaKind.Joined, value = it))
            }
            formatEpochDate(status.updatedAt)?.let {
                add(ProfileHeroMetaItem(kind = ProfileHeroMetaKind.Updated, value = it))
            }
        }

internal fun <T> profileSectionStateOf(
    value: T?,
    loadState: LoadState?,
    isEmpty: (T) -> Boolean = { false },
): ProfileSectionState<T> =
    when {
        loadState is LoadState.Loading && value == null -> ProfileSectionState.Loading
        loadState is LoadState.Error && value != null && !isEmpty(value) -> ProfileSectionState.Partial(
            data = value,
            cause = loadState.details,
        )
        loadState is LoadState.Error -> ProfileSectionState.Error(loadState.details)
        value == null || isEmpty(value) -> ProfileSectionState.Empty
        else -> ProfileSectionState.Content(value)
    }

internal fun ProfileOverview.favouriteGroups(): List<Pair<ProfileMediaTab, List<ProfileOverview.MediaPreview>>> =
    buildList {
        if (animeFavourites.isNotEmpty()) {
            add(ProfileMediaTab.Anime to animeFavourites)
        }
        if (mangaFavourites.isNotEmpty()) {
            add(ProfileMediaTab.Manga to mangaFavourites)
        }
    }

internal fun ProfileOverview.leadFavourite(): ProfileOverview.MediaPreview? =
    animeFavourites.firstOrNull() ?: mangaFavourites.firstOrNull()

internal fun ProfileOverview.favouritesRail(limit: Int = 10): List<ProfileOverview.MediaPreview> =
    favouriteGroups()
        .flatMap { it.second }
        .take(limit)

internal fun ProfileOverview.leadSurface(): ProfileOverviewLeadSurface =
    when {
        animeFavourites.isNotEmpty() || mangaFavourites.isNotEmpty() -> ProfileOverviewLeadSurface.Favourites
        recentActivityPreview(limit = 1).isNotEmpty() -> ProfileOverviewLeadSurface.RecentActivity
        else -> ProfileOverviewLeadSurface.Quiet
    }

internal fun ProfileOverview.supportingFavourites(limitPerGroup: Int = 6): List<Pair<ProfileMediaTab, List<ProfileOverview.MediaPreview>>> {
    val leadId = leadFavourite()?.id

    return favouriteGroups()
        .map { (tab, items) ->
            tab to items.filterNot { it.id == leadId }.take(limitPerGroup)
        }.filter { it.second.isNotEmpty() }
}

internal fun ProfileOverview.recentActivityPreview(limit: Int = 3): List<ProfileOverview.ListActivityPreview> =
    recentActivity
        .sortedByDescending(ProfileOverview.ListActivityPreview::createdAt)
        .take(limit)

internal fun ProfileOverview.recentLibraryActivity(
    tab: ProfileMediaTab,
    limit: Int = 4,
): List<ProfileOverview.ListActivityPreview> =
    recentActivity
        .asSequence()
        .filter { it.media?.type == tab.mediaType }
        .sortedByDescending(ProfileOverview.ListActivityPreview::createdAt)
        .take(limit)
        .toList()

internal fun ProfileFeed.filteredReviews(filter: ProfileActivityFilter): List<ProfileFeed.ReviewPreview> =
    when (filter) {
        ProfileActivityFilter.All,
        ProfileActivityFilter.Reviews,
        -> reviews.sortedByDescending(ProfileFeed.ReviewPreview::createdAt)

        ProfileActivityFilter.ListUpdates -> emptyList()
    }

internal fun ProfileFeed.filteredListUpdates(filter: ProfileActivityFilter): List<ProfileOverview.ListActivityPreview> =
    when (filter) {
        ProfileActivityFilter.All,
        ProfileActivityFilter.ListUpdates,
        -> listActivity.sortedByDescending(ProfileOverview.ListActivityPreview::createdAt)

        ProfileActivityFilter.Reviews -> emptyList()
    }

internal fun ProfileFeed.reviewSpotlight(filter: ProfileActivityFilter): ProfileFeed.ReviewPreview? =
    filteredReviews(filter).firstOrNull()

internal fun ProfileFeed.reviewArchive(filter: ProfileActivityFilter): List<ProfileFeed.ReviewPreview> =
    filteredReviews(filter).drop(1)

internal fun ProfileDetails.libraryPulseSummary(
    displayUser: User,
): ProfileLibraryPulseSummary {
    val animeTotal =
        mediaListStats
            .filter { !it.isCustomList && it.mediaType == MediaType.ANIME }
            .sumOf(MediaListInfo::count)
    val mangaTotal =
        mediaListStats
            .filter { !it.isCustomList && it.mediaType == MediaType.MANGA }
            .sumOf(MediaListInfo::count)

    val progressFootprint =
        when (displayUser) {
            is User.WithStats ->
                listOfNotNull(
                    displayUser.statistics.anime?.minutesWatched
                        ?.takeIf { it > 0 }
                        ?.toHumanReadableQuantity(0)
                        ?.let { "$it min" },
                    displayUser.statistics.manga?.chaptersRead
                        ?.takeIf { it > 0 }
                        ?.toHumanReadableQuantity(0)
                        ?.let { "$it ch" },
                ).joinToString(separator = " • ").ifBlank {
                    (animeTotal + mangaTotal).toHumanReadableQuantity(0)
                }

            else -> (animeTotal + mangaTotal).toHumanReadableQuantity(0)
        }

    val dominantStatus =
        mediaListStats
            .filterNot(MediaListInfo::isCustomList)
            .maxByOrNull(MediaListInfo::count)
            ?.name
            ?.toString()
            .orEmpty()

    return ProfileLibraryPulseSummary(
        animeTotal = animeTotal,
        mangaTotal = mangaTotal,
        progressFootprint = progressFootprint,
        dominantStatus = dominantStatus,
    )
}

internal fun ProfileOverview.MediaPreview.displayTitleText(): String =
    title.userPreferred?.toString()
        ?: title.english?.toString()
        ?: title.romaji?.toString()
        ?: title.native?.toString()
        ?: ""

internal fun ProfileOverview.MediaPreview.secondaryContext(): String =
    listOfNotNull(
        type?.name?.asReadableLabel(),
        format?.name?.asReadableLabel(),
        status?.name?.asReadableLabel(),
    ).joinToString(separator = " • ")

internal fun ProfileOverview.ListActivityPreview.activitySummary(): String =
    listOfNotNull(
        status?.toString()?.takeIf { it.isNotBlank() },
        progress?.toString()?.takeIf { it.isNotBlank() },
        mediaListStatus?.name?.asReadableLabel()?.takeIf { it.isNotBlank() },
    ).distinct().joinToString(separator = " • ")

internal fun ProfileFeed.ReviewPreview.summaryText(): String = summary.toString().trim()

internal fun Statistic.preferredHeroChart(): ProfileStatsChart? =
    when {
        scoreEntries().isNotEmpty() -> ProfileStatsChart.ScoreDistribution
        statusEntries().isNotEmpty() -> ProfileStatsChart.StatusDistribution
        else -> null
    }

internal fun Statistic.secondaryChart(): ProfileStatsChart? =
    when (preferredHeroChart()) {
        ProfileStatsChart.ScoreDistribution ->
            if (statusEntries().isNotEmpty()) {
                ProfileStatsChart.StatusDistribution
            } else {
                null
            }

        ProfileStatsChart.StatusDistribution ->
            if (scoreEntries().isNotEmpty()) {
                ProfileStatsChart.ScoreDistribution
            } else {
                null
            }

        null -> null
    }

internal fun Statistic.scoreEntries(): List<ProfileScoreEntry> =
    scoreStatistics()
        .asSequence()
        .filter { it.count > 0 }
        .groupBy(StatisticScore::score)
        .map { (score, entries) ->
            ProfileScoreEntry(
                score = score,
                amount = entries.sumOf(StatisticScore::count),
            )
        }.sortedBy(ProfileScoreEntry::score)

internal fun Statistic.statusEntries(): List<ProfileStatusEntry> {
    val aggregated =
        statusStatistics()
            .asSequence()
            .filter { it.count > 0 }
            .groupBy(StatisticStatus::status)
            .map { (status, entries) -> status to entries.sumOf(StatisticStatus::count) }
            .sortedWith(
                compareByDescending<Pair<MediaListStatus?, Int>> { it.second }
                    .thenBy { it.first?.ordinal ?: Int.MAX_VALUE },
            )

    val total = aggregated.sumOf(Pair<MediaListStatus?, Int>::second)
    if (total <= 0) {
        return emptyList()
    }

    return aggregated.map { (status, amount) ->
        ProfileStatusEntry(
            status = status,
            amount = amount,
            fraction = amount / total.toFloat(),
        )
    }
}

internal fun List<ProfileScoreEntry>.axisValues(labelCount: Int): List<Int> {
    if (isEmpty() || labelCount < 2) {
        return emptyList()
    }

    val maxAmount = maxOf(ProfileScoreEntry::amount)
    if (maxAmount <= 0) {
        return emptyList()
    }

    val segmentCount = labelCount - 1
    val step = niceAxisStep(ceil(maxAmount / segmentCount.toDouble()).toInt())

    return (segmentCount downTo 0).map { index -> step * index }
}

private fun Statistic.scoreStatistics(): List<StatisticScore> =
    when (this) {
        is Statistic.Anime ->
            scores
                .orEmpty()
                .filterIsInstance<MediaStatistic.Anime.Score>()
                .map {
                    StatisticScore(
                        score = it.score,
                        count = it.count,
                    )
                }
        is Statistic.Manga ->
            scores
                .orEmpty()
                .filterIsInstance<MediaStatistic.Manga.Score>()
                .map {
                    StatisticScore(
                        score = it.score,
                        count = it.count,
                    )
                }
    }

private fun Statistic.statusStatistics(): List<StatisticStatus> =
    when (this) {
        is Statistic.Anime ->
            statuses
                .orEmpty()
                .filterIsInstance<MediaStatistic.Anime.Status>()
                .map {
                    StatisticStatus(
                        status = it.status,
                        count = it.count,
                    )
                }
        is Statistic.Manga ->
            statuses
                .orEmpty()
                .filterIsInstance<MediaStatistic.Manga.Status>()
                .map {
                    StatisticStatus(
                        status = it.status,
                        count = it.count,
                    )
                }
    }

private fun niceAxisStep(value: Int): Int {
    if (value <= 0) {
        return 0
    }

    if (value < 10) {
        return value
    }

    val magnitude = 10.0.pow(floor(log10(value.toDouble()))).toInt()
    val normalized = value.toDouble() / magnitude
    val multiplier =
        when {
            normalized <= 1.0 -> 1.0
            normalized <= 1.2 -> 1.2
            normalized <= 1.5 -> 1.5
            normalized <= 2.0 -> 2.0
            normalized <= 2.5 -> 2.5
            normalized <= 3.0 -> 3.0
            normalized <= 4.0 -> 4.0
            normalized <= 5.0 -> 5.0
            normalized <= 6.0 -> 6.0
            normalized <= 8.0 -> 8.0
            normalized <= 9.0 -> 9.0
            else -> 10.0
        }

    return (multiplier * magnitude).roundToInt()
}

private data class StatisticScore(
    val score: Int,
    val count: Int,
)

private data class StatisticStatus(
    val status: MediaListStatus?,
    val count: Int,
)

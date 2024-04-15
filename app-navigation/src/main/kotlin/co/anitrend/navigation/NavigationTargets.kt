/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.navigation

import android.text.SpannedString
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.work.ListenableWorker
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.common.DateInt
import co.anitrend.domain.common.DateLike
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaLicensor
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.recommendation.enums.RecommendationSort
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.navigation.model.common.IParam
import co.anitrend.navigation.model.sorting.Sorting
import co.anitrend.navigation.provider.INavigationProvider
import co.anitrend.navigation.router.NavigationRouter
import co.anitrend.navigation.work.WorkSchedulerController
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import org.koin.core.component.inject

object MainRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object NavigationDrawerRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    enum class Destination(val requiresAuth: Boolean) {
        HOME(false),
        DISCOVER(false),
        SOCIAL(false),
        REVIEWS(false),
        SUGGESTIONS(false),
        ANIME_LIST(true),
        MANGA_LIST(true),
        NEWS(false),
        FORUMS(false),
        EPISODES(false)
    }

    @Parcelize
    data class NavigationDrawerParam(
        val destination: Destination
    ) : IParam
}

object SplashRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object OnBoardingRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class OnboardingParam(
        @RawRes val resource: Int,
        @DrawableRes val background: Int,
        val title: @RawValue SpannedString,
        val subTitle: @RawValue SpannedString,
        val description: @RawValue SpannedString,
        @ColorRes val textColor: Int
    ) : IParam
}

object SearchRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class SearchParam(
        val query: String? = null,
        val genres: List<String>? = null,
        val year: Int? = null,
        val season: MediaSeason? = null,
        val format: MediaFormat? = null,
        val status: MediaStatus? = null,
    ) : IParam
}

object SettingsRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object AboutRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object AiringRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    /** [AiringSchedule query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * @param id Filter by the id of the airing schedule item
     * @param mediaId Filter by the id of associated media
     * @param episode Filter by the airing episode number
     * @param airingAt Filter by the time of airing
     * @param notYetAired Filter to episodes that haven't yet aired
     * @param id_not Filter by the id of the airing schedule item
     * @param id_in Filter by the id of the airing schedule item
     * @param id_not_in Filter by the id of the airing schedule item
     * @param mediaId_not Filter by the id of associated media
     * @param mediaId_in Filter by the id of associated media
     * @param mediaId_not_in Filter by the id of associated media
     * @param episode_not Filter by the airing episode number
     * @param episode_in Filter by the airing episode number
     * @param episode_not_in Filter by the airing episode number
     * @param episode_greater Filter by the airing episode number
     * @param episode_lesser Filter by the airing episode number
     * @param airingAt_greater Filter by the time of airing
     * @param airingAt_lesser Filter by the time of airing
     * @param sort The order the results will be returned in
     */
    @Parcelize
    data class AiringParam(
        var id: Long? = null,
        var mediaId: Long? = null,
        var episode: Int? = null,
        var airingAt: Int? = null,
        var notYetAired: Boolean? = null,
        var id_not: Long? = null,
        var id_in: List<Long>? = null,
        var id_not_in: List<Long>? = null,
        var mediaId_not: Long? = null,
        var mediaId_in: List<Long>? = null,
        var mediaId_not_in: List<Long>? = null,
        var episode_not: Int? = null,
        var episode_in: List<Int>? = null,
        var episode_not_in: List<Int>? = null,
        var episode_greater: Int? = null,
        var episode_lesser: Int? = null,
        var airingAt_greater: Int? = null,
        var airingAt_lesser: Int? = null,
        var sort: List<Sorting<AiringSort>>? = null
    ) : IParam
}

object AuthRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class AuthParam(
        val accessToken: String? = null,
        val tokenType: String? = null,
        val expiresIn: Long? = null,
        val errorTitle: String? = null,
        val errorDescription: String? = null
    ) : IParam
}

object MediaRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class MediaParam(
        val id: Long,
        val type: MediaType,
    ) : IParam
}

object MediaDiscoverRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
        fun sheet(): Class<out DialogFragment>
    }

    fun forFragment() = provider.fragment()
    fun forSheet() = provider.sheet()

    @Parcelize
    data class MediaDiscoverParam(
        var averageScore: Int? = null,
        var averageScore_greater: Int? = null,
        var averageScore_lesser: Int? = null,
        var averageScore_not: Int? = null,
        var chapters: Int? = null,
        var chapters_greater: Int? = null,
        var chapters_lesser: Int? = null,
        var countryOfOrigin: CharSequence? = null,
        var duration: Int? = null,
        var duration_greater: Int? = null,
        var duration_lesser: Int? = null,
        var endDate: DateInt? = null,
        var endDate_greater: DateInt? = null,
        var endDate_lesser: DateInt? = null,
        var endDate_like: String? = null,
        var episodes: Int? = null,
        var episodes_greater: Int? = null,
        var episodes_lesser: Int? = null,
        var format: MediaFormat? = null,
        var format_in: List<MediaFormat>? = null,
        var format_not: MediaFormat? = null,
        var format_not_in: List<MediaFormat>? = null,
        var genre: String? = null,
        var genre_in: List<String>? = null,
        var genre_not_in: List<String>? = null,
        var id: Long? = null,
        var idMal: Long? = null,
        var idMal_in: List<Long>? = null,
        var idMal_not: Long? = null,
        var idMal_not_in: List<Long>? = null,
        var id_in: List<Long>? = null,
        var id_not: Long? = null,
        var id_not_in: List<Long>? = null,
        var isAdult: Boolean? = null,
        var licensedBy: MediaLicensor? = null,
        var licensedBy_in: List<MediaLicensor>? = null,
        var minimumTagRank: Int? = null,
        var onList: Boolean? = null,
        var popularity: Int? = null,
        var popularity_greater: Int? = null,
        var popularity_lesser: Int? = null,
        var popularity_not: Int? = null,
        var search: String? = null,
        var season: MediaSeason? = null,
        var seasonYear: Int? = null,
        var sort: List<Sorting<MediaSort>>? = null,
        var source: MediaSource? = null,
        var source_in: List<MediaSource>? = null,
        var startDate: DateInt? = null,
        var startDate_greater: DateInt? = null,
        var startDate_lesser: DateInt? = null,
        var startDate_like: String? = null,
        var status: MediaStatus? = null,
        var status_in: List<MediaStatus>? = null,
        var status_not: MediaStatus? = null,
        var status_not_in: List<MediaStatus>? = null,
        var tag: String? = null,
        var tagCategory: String? = null,
        var tagCategory_in: List<String>? = null,
        var tagCategory_not_in: List<String>? = null,
        var tag_in: List<String>? = null,
        var tag_not_in: List<String>? = null,
        var type: MediaType? = null,
        var volumes: Int? = null,
        var volumes_greater: Int? = null,
        var volumes_lesser: Int? = null
    ) : IParam
}

object MediaDiscoverFilterRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun sorting(): Class<out Fragment>
        fun general(): Class<out Fragment>
        fun genre(): Class<out Fragment>
        fun tag(): Class<out Fragment>
    }

    fun forSorting() = provider.sorting()
    fun forGeneral() = provider.general()
    fun forGenre() = provider.genre()
    fun forTag() = provider.tag()

    /**
     * Filter action models
     */
    sealed class Action : IParam {

        /**
         * @return true if this current action impl has not been changed
         */
        abstract fun isDefault(): Boolean

        companion object {
            const val KEY = "MediaDiscoverFilterRouter#Action"
        }

        @Parcelize
        data class Sort(
            var sort: List<Sorting<MediaSort>>
        ) : Action() {
            /**
             * @return true if this current action impl has not been changed
             */
            override fun isDefault() =
                sort.isEmpty()
        }

        @Parcelize
        data class General(
            var id: Long?
        ) : Action() {
            /**
             * @return true if this current action impl has not been changed
             */
            override fun isDefault() =
                id == null
        }

        @Parcelize
        data class Genre(
            var genre_in: List<String>? = null,
            var genre_not_in: List<String>? = null,
        ) : Action() {
            /**
             * @return true if this current action impl has not been changed
             */
            override fun isDefault() =
                genre_in.isNullOrEmpty() && genre_not_in.isNullOrEmpty()
        }

        @Parcelize
        data class Tag(
            var tagCategory_in: List<String>? = null,
            var tagCategory_not_in: List<String>? = null,
            var tag_in: List<String>? = null,
            var tag_not_in: List<String>? = null
        ) : Action() {
            /**
             * @return true if this current action impl has not been changed
             */
            override fun isDefault() =
                tagCategory_in.isNullOrEmpty() &&
                        tagCategory_not_in.isNullOrEmpty() &&
                        tag_in.isNullOrEmpty() &&
                        tag_not_in.isNullOrEmpty()
        }
    }
}

object MediaCarouselRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object CharacterRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class CharacterParam(
        val id: Long?,
        val name: String?
    ) : IParam
}

object CharacterDiscoverRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class CharacterDiscoverParam(
        val id: Long
    ) : IParam
}

object StudioRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class StudioParam(
        val id: Long,
    ) : IParam
}

object StaffRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class StaffParam(
        val id: Long?,
        val name: String?
    ) : IParam
}

object StaffDiscoverRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class StaffDiscoverParam(
        val id: Long?,
        val name: String?
    ) : IParam
}

object ProfileRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class ProfileParam(
        val userId: Long? = null,
        val userName: String? = null
    ) : IParam
}

object ForumRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object FeedRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object ReviewRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object ReviewDiscoverRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class ReviewDiscoverParam(
        val mediaId: Long? = null,
        val userId: Long? = null,
        val mediaType: MediaType? = null,
        val sort: List<Sorting<ReviewSort>>? = null,
        val scoreFormat: ScoreFormat = ScoreFormat.POINT_100
    ) : IParam
}

object RecommendationDiscoverRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class RecommendationDiscoverParam(
        val id: Int? = null,
        val mediaId: Int? = null,
        val mediaRecommendationId: Int? = null,
        val onList: Boolean? = null,
        val rating: Int? = null,
        val ratingGreatThan: Int? = null,
        val ratingLessThan: Int? = null,
        val sort: List<Sorting<RecommendationSort>>? = null,
        val userId: Int? = null
    ) : IParam
}

object NotificationRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object NewsRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class NewsParam(
        val link: String,
        val title: String,
        val subTitle: String,
        val description: String?,
        val content: String
    ) : IParam
}

object EpisodeRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
        fun sheet(): Class<out DialogFragment>
    }

    fun forFragment() = provider.fragment()
    fun forSheet() = provider.sheet()

    @Parcelize
    data class EpisodeParam(
        val id: Long
    ) : IParam
}

object SuggestionRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()
}

object UpdaterRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = ProfileRouter.provider.fragment()
}

object MediaListRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun fragment(): Class<out Fragment>
    }

    fun forFragment() = provider.fragment()

    @Parcelize
    data class MediaListParam(
        val customListName: String? = null,
        val mediaId_in: List<Int>? = null,
        val mediaId_not_in: List<Int>? = null,
        val isFollowing: Boolean? = null,
        val userId_in: List<Long>? = null,
        val compareWithAuthList: Boolean? = null,
        val scoreFormat: ScoreFormat = ScoreFormat.POINT_100,
        val type: MediaType,
        val userId: Long? = null,
        val userName: String? = null,
        val completedAt: DateInt? = null,
        val completedAt_greater: DateInt? = null,
        val completedAt_lesser: DateInt? = null,
        val completedAt_like: DateLike? = null,
        val notes: String? = null,
        val notes_like: String? = null,
        val sort: List<Sorting<MediaListSort>>? = null,
        val startedAt: DateInt? = null,
        val startedAt_greater: DateInt? = null,
        val startedAt_lesser: DateInt? = null,
        val startedAt_like: DateLike? = null,
        val status: MediaListStatus? = null,
        val status_in: List<MediaListStatus>? = null,
        val status_not: MediaListStatus? = null,
        val status_not_in: List<MediaListStatus>? = null,
    ) : IParam
}

object MediaListEditorRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun sheet(): Class<out DialogFragment>
    }

    fun forSheet() = provider.sheet()

    @Parcelize
    data class MediaListEditorParam(
        val mediaId: Long,
        val mediaType: MediaType,
        val scoreFormat: ScoreFormat,
    ) : IParam
}

object ImageViewerRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider

    @Parcelize
    data class ImageSourceParam(
        val imageSrc: CharSequence
    ) : IParam
}

object GenreTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
        fun scheduler(): WorkSchedulerController
    }

    fun forWorker() = provider.worker()
    fun forScheduler() = provider.scheduler()
}

object TagTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
        fun scheduler(): WorkSchedulerController
    }

    fun forWorker() = provider.worker()
    fun forScheduler() = provider.scheduler()
}

object AccountTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun signOutWorker(): Class<out ListenableWorker>
        fun signInWorker(): Class<out ListenableWorker>
    }

    fun forSignOutWorker() = provider.signOutWorker()
    fun forSignInWorker() = provider.signInWorker()

    @Parcelize
    data class AccountParam(
        val id: Long
    ) : IParam
}

object UserTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun accountSyncWorker(): Class<out ListenableWorker>
        fun followToggleWorker(): Class<out ListenableWorker>
        fun statisticSyncWorker(): Class<out ListenableWorker>

        fun accountSyncScheduler(): WorkSchedulerController
        fun statisticSyncScheduler(): WorkSchedulerController
    }

    fun forAccountSyncWorker() = provider.accountSyncWorker()
    fun forFollowToggleWorker() = provider.followToggleWorker()
    fun forStatisticSyncWorker() = provider.statisticSyncWorker()

    fun forAccountSyncScheduler() = provider.accountSyncScheduler()
    fun forStatisticSyncScheduler() = provider.accountSyncScheduler()
}

object MediaListTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun mediaListSaveEntryWorker(): Class<out ListenableWorker>
        fun mediaListSaveEntriesWorker(): Class<out ListenableWorker>
        fun mediaListDeleteEntryWorker(): Class<out ListenableWorker>
        fun mediaListDeleteCustomListWorker(): Class<out ListenableWorker>
        fun animeSyncWorker(): Class<out ListenableWorker>
        fun mangaSyncWorker(): Class<out ListenableWorker>

        fun animeSyncScheduler(): WorkSchedulerController
        fun mangaSyncScheduler(): WorkSchedulerController
    }

    fun forMediaListSaveEntryWorker() = provider.mediaListSaveEntryWorker()
    fun forMediaListSaveEntriesWorker() = provider.mediaListSaveEntriesWorker()
    fun forMediaListDeleteEntryWorker() = provider.mediaListDeleteEntryWorker()
    fun forMediaListDeleteCustomListWorker() = provider.mediaListDeleteCustomListWorker()

    fun forMediaListAnimeSyncWorker() = provider.animeSyncWorker()
    fun forMediaListMangaSyncWorker() = provider.mangaSyncWorker()

    fun forAnimeScheduler() = provider.animeSyncScheduler()
    fun forMangaScheduler() = provider.mangaSyncScheduler()

    sealed class Param : IParam {

        @Parcelize
        data class SaveEntry(
            var id: Long? = null,
            var mediaId: Long,
            var status: MediaListStatus,
            var scoreFormat: ScoreFormat,
            var score: Float? = null,
            var scoreRaw: Int? = null,
            var progress: Int? = null,
            var progressVolumes: Int? = null,
            var repeat: Int? = null,
            var priority: Int? = null,
            var private: Boolean? = null,
            var notes: String? = null,
            var hiddenFromStatusLists: Boolean? = null,
            var customLists: List<String>? = null,
            var advancedScores: List<Float>? = null,
            var startedAt: @RawValue FuzzyDate? = null,
            var completedAt: @RawValue FuzzyDate? = null,
        ) : Param()

        @Parcelize
        data class SaveEntries(
            val ids: List<Long>,
            val status: MediaListStatus,
            val scoreFormat: ScoreFormat,
            val score: Float? = null,
            val scoreRaw: Int? = null,
            val progress: Int? = null,
            val progressVolumes: Int? = null,
            val repeat: Int? = null,
            val priority: Int? = null,
            val private: Boolean? = null,
            val notes: String? = null,
            val hiddenFromStatusLists: Boolean? = null,
            val advancedScores: List<Float>? = null,
            val startedAt: @RawValue FuzzyDate? = null,
            val completedAt: @RawValue FuzzyDate? = null,
        ) : Param()

        @Parcelize
        data class DeleteEntry(
            val id: Long
        ) : Param()

        @Parcelize
        data class DeleteCustomList(
            val customList: String,
            val type: MediaType
        ) : Param()
    }
}

object NewsTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
        fun scheduler(): WorkSchedulerController
    }

    fun forWorker() = provider.worker()
    fun forScheduler() = provider.scheduler()
}

object EpisodeTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
        fun scheduler(): WorkSchedulerController
    }

    fun forWorker() = provider.worker()
    fun forScheduler() = provider.scheduler()
}

object ReviewTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun reviewVoteEntryWorker(): Class<out ListenableWorker>
        fun reviewSaveEntryWorker(): Class<out ListenableWorker>
        fun reviewDeleteEntryWorker(): Class<out ListenableWorker>
    }

    fun forReviewRateWorker() = provider.reviewVoteEntryWorker()
    fun forReviewSaveWorker() = provider.reviewSaveEntryWorker()
    fun forReviewDeleteWorker() = provider.reviewDeleteEntryWorker()

    sealed class Param : IParam {

        @Parcelize
        data class SaveEntry(
            val id: Long? = null,
            val mediaId: Long,
            val body: String,
            val summary: String,
            val score: Int,
            val private: Boolean
        ) : Param()

        @Parcelize
        data class RateEntry(
            val id: Long,
            val rating: ReviewRating
        ) : Param()

        @Parcelize
        data class DeleteEntry(
            val id: Long
        ) : Param()
    }
}

object ConfigTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
        fun scheduler(): WorkSchedulerController
    }

    fun forWorker() = provider.worker()
    fun forScheduler() = provider.scheduler()
}

object FavouriteTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>
    }

    fun forWorker() = provider.worker()

    sealed class Param : IParam {
        @Parcelize
        data class MediaToggleParam(
            val id: Long,
            val mediaType: MediaType,
        ) : Param()

        @Parcelize
        data class CharacterToggleParam(val id: Long) : Param()

        @Parcelize
        data class StaffToggleParam(val id: Long) : Param()
    }
}

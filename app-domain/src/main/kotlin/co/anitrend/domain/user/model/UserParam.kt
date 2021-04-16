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

package co.anitrend.domain.user.model

import co.anitrend.domain.common.sort.SortWithOrder
import co.anitrend.domain.common.sort.contract.ISortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.enums.UserSort
import co.anitrend.domain.user.enums.UserStatisticsSort
import co.anitrend.domain.user.enums.UserTitleLanguage

sealed class UserParam {

    /** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by id of the user
     * @param name Filter by the name of the user
     */
    data class Identifier(
        val name: String,
        val id: Long? = null
    ) : UserParam()

    /** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by the user id
     */
    data class Profile(
        val id: Long
    ) : UserParam()

    /** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by the user id
     * @param statisticsSort Sort for stats
     */
    data class Statistic(
        val id: Long,
        val statisticsSort: List<ISortWithOrder<UserStatisticsSort>>? = listOf(
            SortWithOrder(UserStatisticsSort.MEAN_SCORE, SortOrder.DESC)
        )
    ) : UserParam()

    /** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param search Filter by search query
     * @param sort The order the results will be returned in
     */
    data class Search(
        val search: String,
        val sort: List<ISortWithOrder<UserSort>>? = null
    ) : UserParam()

    /** [UpdateUser mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * @param about User's about/bio text
     * @param titleLanguage User's title language
     * @param displayAdultContent If the user should see media marked as adult-only
     * @param airingNotifications If the user should get notifications when a show they are watching airs
     * @param scoreFormat The user's list scoring system
     * @param timeZone Timezone offset format: -?HH:MM
     * @param rowOrder The user's default list order
     * @param profileColor Profile highlight color
     * @param notificationOptions Notification Options
     * @param animeListOptions The user's anime list options
     * @param mangaListOptions The user's anime list options
     */
    data class Update(
        val about: String?,
        val titleLanguage: UserTitleLanguage,
        val displayAdultContent: Boolean = false,
        val airingNotifications: Boolean = true,
        val scoreFormat: ScoreFormat,
        val timeZone: String?,
        val rowOrder: String,
        val profileColor: String,
        val notificationOptions: List<NotificationOption>,
        val animeListOptions: MediaListOptions,
        val mangaListOptions: MediaListOptions
    ) {
        /** [MediaListOptionsInput](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistoptionsinput.doc.html)
         *
         * @param sectionOrder The order each list should be displayed in
         * @param splitCompletedSectionByFormat If the completed sections of the list should be separated by format
         * @param customLists The names of the user's custom lists
         * @param advancedScoring The names of the user's advanced scoring sections
         * @param advancedScoringEnabled If advanced scoring is enabled
         */
        data class MediaListOptions(
            val sectionOrder: List<String>? = null,
            val splitCompletedSectionByFormat: Boolean = false,
            val customLists: List<String>? = null,
            val advancedScoring: List<String>? = null,
            val advancedScoringEnabled: Boolean = false
        )

        /** [NotificationOptionInput](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationoptioninput.doc.html)
         *
         * Notification option input
         *
         * @param enabled Whether this type of notification is enabled
         * @param type The type of notification
         */
        data class NotificationOption(
            val enabled: Boolean,
            val type: NotificationType
        )
    }

    /** [ToggleFollow mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Toggle the un/following of a user
     *
     * @param userId The id of the user to un/follow
     */
    data class ToggleFollow(
        val userId: Long? = null
    ) : UserParam()
}

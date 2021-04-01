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

package co.anitrend.data.user.model.mutation

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.data.notification.model.input.NotificationOptionInput
import co.anitrend.domain.user.model.UserParam

internal sealed class UserMutation : IGraphPayload {

    data class ToggleFollow(
        val param: UserParam.ToggleFollow
    ) : UserMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "userId" to param.userId
        )
    }

    data class Update(
        val param: UserParam.Update
    ) : UserMutation() {

        data class MediaListOptions(
            val param: UserParam.Update.MediaListOptions
        ) : UserMutation() {

            /**
             * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
             */
            override fun toMap() = mapOf(
                "sectionOrder" to param.sectionOrder,
                "splitCompletedSectionByFormat" to param.splitCompletedSectionByFormat,
                "customLists" to param.customLists,
                "advancedScoring" to param.advancedScoring,
                "advancedScoringEnabled" to param.advancedScoringEnabled
            )
        }

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "about" to param.about,
            "titleLanguage" to param.titleLanguage,
            "displayAdultContent" to param.displayAdultContent,
            "airingNotifications" to param.airingNotifications,
            "scoreFormat" to param.scoreFormat,
            "timeZone" to param.timeZone,
            "rowOrder" to param.rowOrder,
            "profileColor" to param.profileColor,
            "notificationOptions" to param.notificationOptions.map(::NotificationOptionInput)
                .map(NotificationOptionInput::toMap),
            "animeListOptions" to MediaListOptions(param.animeListOptions).toMap(),
            "mangaListOptions" to MediaListOptions(param.mangaListOptions).toMap()
        )
    }
}

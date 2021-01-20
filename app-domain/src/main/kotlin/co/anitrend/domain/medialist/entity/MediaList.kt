/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.domain.medialist.entity

import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.extension.INVALID_ID
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.entity.base.IMediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.user.entity.User

sealed class MediaList : IMediaList {

    data class CustomList(
        override val name: CharSequence,
        override val enabled: Boolean
    ) : IMediaList.ICustomList

    data class AdvancedScore(
        override val name: String,
        override val score: Float
    ) : IMediaList.IAdvancedScore

    data class Core(
        override val advancedScores: List<AdvancedScore>,
        override val customLists: Collection<CustomList>,
        override val userId: Long,
        override val priority: Int?,
        override val createdOn: Long?,
        override val startedOn: FuzzyDate,
        override val finishedOn: FuzzyDate,
        override val mediaId: Long,
        override val score: Float,
        override val status: MediaListStatus,
        override val progress: MediaListProgress,
        override val privacy: MediaListPrivacy,
        override val id: Long
    ) : MediaList() {
        companion object {
            fun empty() = Core(
                advancedScores = emptyList(),
                customLists = emptyList(),
                userId = 0,
                priority = 0,
                createdOn = 0,
                startedOn = FuzzyDate.empty(),
                finishedOn = FuzzyDate.empty(),
                mediaId = 0,
                score = 0f,
                status = MediaListStatus.PLANNING,
                progress = MediaListProgress.Anime.empty(),
                privacy = MediaListPrivacy.empty(),
                id = INVALID_ID,
            )
        }
    }

    data class Extended(
        val owner: User,
        override val advancedScores: List<AdvancedScore>,
        override val customLists: Collection<CustomList>,
        override val userId: Long,
        override val priority: Int?,
        override val createdOn: Long?,
        override val startedOn: FuzzyDate,
        override val finishedOn: FuzzyDate,
        override val mediaId: Long,
        override val score: Float,
        override val status: MediaListStatus,
        override val progress: MediaListProgress,
        override val privacy: MediaListPrivacy,
        override val id: Long
    ) : MediaList() {
        companion object {
            fun empty() = Extended(
                owner = User.empty(),
                advancedScores = emptyList(),
                customLists = emptyList(),
                userId = 0,
                priority = 0,
                createdOn = 0,
                startedOn = FuzzyDate.empty(),
                finishedOn = FuzzyDate.empty(),
                mediaId = 0,
                score = 0f,
                status = MediaListStatus.PLANNING,
                progress = MediaListProgress.Anime.empty(),
                privacy = MediaListPrivacy.empty(),
                id = INVALID_ID,
            )
        }
    }
}
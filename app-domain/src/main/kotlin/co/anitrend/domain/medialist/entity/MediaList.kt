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
import co.anitrend.domain.medialist.entity.base.IMediaListExtended
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus

data class MediaList(
    override val advancedScores: Map<String, Int>,
    override val customLists: Collection<CharSequence>,
    override val userId: Long,
    override val priority: Float,
    override val createdOn: Long,
    override val startedOn: FuzzyDate,
    override val finishedOn: FuzzyDate,
    override val mediaId: Long,
    override val score: Float,
    override val status: MediaListStatus,
    override val progress: MediaListProgress,
    override val privacy: MediaListPrivacy,
    override val id: Long
) : IMediaListExtended {

    companion object {
        fun empty() = MediaList(
            advancedScores = emptyMap(),
            customLists = emptyList(),
            userId = 0,
            priority = 0f,
            createdOn = 0,
            startedOn = FuzzyDate.empty(),
            finishedOn = FuzzyDate.empty(),
            mediaId = 0,
            score = 0f,
            status = MediaListStatus.COMPLETED,
            progress = MediaListProgress.Anime.empty(),
            privacy = MediaListPrivacy.empty(),
            id = INVALID_ID,
        )
    }
}
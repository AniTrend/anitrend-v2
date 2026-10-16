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
package co.anitrend.data.user.mapper

import co.anitrend.data.staff.datasource.local.StaffLocalSource
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.tag.datasource.local.TagLocalSource
import co.anitrend.data.user.converter.UserStatisticPayload
import co.anitrend.data.user.datasource.local.statistic.UserStatisticLocalSource

internal fun interface UserStatisticWriterContract {
    suspend fun persist(payload: UserStatisticPayload)
}

internal class UserStatisticWriter(
    private val localSource: UserStatisticLocalSource,
    private val tagLocalSource: TagLocalSource,
    private val staffLocalSource: StaffLocalSource,
    private val studioLocalSource: StudioLocalSource,
) : UserStatisticWriterContract {
    override suspend fun persist(payload: UserStatisticPayload) {
        // dimension rows carry fk parents which must exist before the inserts below
        tagLocalSource.upsert(payload.tagParents)
        staffLocalSource.upsert(payload.staffParents)
        studioLocalSource.upsert(payload.studioParents)
        localSource.upsert(payload.statistic)
        localSource.upsertCountries(payload.countries)
        localSource.upsertFormats(payload.formats)
        localSource.upsertGenres(payload.genres)
        localSource.upsertLengths(payload.lengths)
        localSource.upsertReleaseYears(payload.releaseYears)
        localSource.upsertScores(payload.scores)
        localSource.upsertStaff(payload.staff)
        localSource.upsertStartYears(payload.startYears)
        localSource.upsertStatuses(payload.statuses)
        localSource.upsertStudios(payload.studios)
        localSource.upsertTags(payload.tags)
        localSource.upsertVoiceActors(payload.voiceActors)
    }
}

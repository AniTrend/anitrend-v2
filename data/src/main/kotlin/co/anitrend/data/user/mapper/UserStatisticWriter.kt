/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.mapper

import co.anitrend.data.user.converter.UserStatisticPayload
import co.anitrend.data.user.datasource.local.statistic.UserStatisticLocalSource

internal fun interface UserStatisticWriterContract {
    suspend fun persist(payload: UserStatisticPayload)
}

internal class UserStatisticWriter(
    private val localSource: UserStatisticLocalSource,
) : UserStatisticWriterContract {
    override suspend fun persist(payload: UserStatisticPayload) {
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

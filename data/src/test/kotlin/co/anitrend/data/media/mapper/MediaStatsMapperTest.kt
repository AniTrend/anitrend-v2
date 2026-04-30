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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.media.datasource.local.MediaStatsLocalSource
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.stats.MediaScoreDistributionEntity
import co.anitrend.data.media.entity.stats.MediaStatusDistributionEntity
import co.anitrend.data.media.entity.view.MediaStatsEntityView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaStatsMapperTest {
    @Test
    fun `given normalized stats when persisting then transaction runner stores parent and distributions`() =
        runBlocking {
            val transactionRunner = FakeTransactionRunner()
            val writer = FakeMediaStatsWriter()
            val mapper = MediaStatsMapper(writer = writer, transactionRunner = transactionRunner)

            mapper.onResponseDatabaseInsert(
                MediaStatsMapper.Payload(
                    stats = MediaStatsEntity(id = 42L),
                    scoreDistribution =
                        listOf(
                            MediaScoreDistributionEntity(amount = 100, score = 90, mediaId = 42L),
                        ),
                    statusDistribution =
                        listOf(
                            MediaStatusDistributionEntity(amount = 60, status = "CURRENT", mediaId = 42L),
                        ),
                ),
            )

            assertEquals(1, transactionRunner.invocationCount)
            assertEquals(listOf(42L), writer.upsertedStatsIds)
            assertEquals(listOf(90), writer.upsertedScores.map(MediaScoreDistributionEntity::score))
            assertEquals(listOf("CURRENT"), writer.upsertedStatuses.map(MediaStatusDistributionEntity::status))
        }

    private class FakeTransactionRunner : TransactionRunner {
        var invocationCount = 0

        override suspend fun run(block: suspend () -> Unit) {
            invocationCount += 1
            block()
        }
    }

    private class FakeMediaStatsWriter : MediaStatsWriterContract {
        val upsertedStatsIds = mutableListOf<Long>()
        val upsertedScores = mutableListOf<MediaScoreDistributionEntity>()
        val upsertedStatuses = mutableListOf<MediaStatusDistributionEntity>()

        override suspend fun persist(payload: MediaStatsMapper.Payload) {
            upsertedStatsIds += payload.stats.id
            upsertedScores += payload.scoreDistribution
            upsertedStatuses += payload.statusDistribution
        }
    }
}

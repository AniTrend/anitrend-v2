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
package co.anitrend.data.recommendation.mapper

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.anitrend.data.android.network.model.NetworkMessage
import co.anitrend.data.core.IsolatedSidecarTestSuite
import co.anitrend.data.core.api.factory.AniListApiFactory
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.toConnectionPreviewEntity
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.source.contract.MediaConnectionSource
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.data.settings.refresh.IRefreshBehaviourSettings
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.medialist.enums.ScoreFormat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4ClassRunner::class)
internal class RecommendationRefreshPersistenceTest : IsolatedSidecarTestSuite() {
    private var server: MockWebServer? = null

    @After
    fun shutdownServer() {
        server?.shutdown()
        server = null
    }

    @Test
    fun refreshReplacesOnlyTheRequestedMediaSnapshot() =
        runTest {
            seedRecommendation(mediaId = X, entryId = A, targetId = 942_001L)
            seedRecommendation(mediaId = X, entryId = B, targetId = 942_002L)
            seedRecommendation(mediaId = Y, entryId = C, targetId = 942_003L)

            val source = recommendationsSource()
            server!!.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(RESPONSE_B_ONLY),
            )

            source(MediaParam.Recommendations(id = X, scoreFormat = ScoreFormat.POINT_100))
            awaitRows { it == listOf(B) }

            assertEquals(listOf(B), entryIds(X))
            assertEquals(listOf(C), entryIds(Y))
        }

    @Test
    fun emptySnapshotClearsOnlyTheRequestedMedia() =
        runTest {
            seedRecommendation(mediaId = X, entryId = A, targetId = 942_001L)
            seedRecommendation(mediaId = Y, entryId = C, targetId = 942_003L)

            val source = recommendationsSource()
            server!!.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(RESPONSE_EMPTY),
            )

            source(MediaParam.Recommendations(id = X, scoreFormat = ScoreFormat.POINT_100))
            awaitRows { it.isEmpty() }

            assertTrue(entryIds(X).isEmpty())
            assertEquals(listOf(C), entryIds(Y))
        }

    private suspend fun entryIds(mediaId: Long): List<Long> =
        database
            .mediaRecommendationConnectionDao()
            .entriesByMediaIdFlow(mediaId)
            .first()
            .map { it.entryId }
            .sorted()

    private suspend fun seedRecommendation(
        mediaId: Long,
        entryId: Long,
        targetId: Long,
    ) {
        val target =
            MediaConverter()
                .convertFrom(
                    MediaModel.Core(
                        isReviewBlocked = false,
                        title = MediaModel.Title(romaji = "Target $targetId"),
                        type = MediaType.ANIME,
                        favourites = 0,
                        siteUrl = "https://anilist.co/anime/$targetId",
                        id = targetId,
                    ),
                ).toConnectionPreviewEntity()
        database.mediaRecommendationConnectionDao().upsertConnections(
            listOf(
                MediaRecommendationConnectionEntity(
                    mediaId = mediaId,
                    entryId = entryId,
                    rating = 1,
                    userName = null,
                    userRating = null,
                    sortIndex = 0,
                    target = target,
                ),
            ),
        )
    }

    private suspend fun awaitRows(predicate: (List<Long>) -> Boolean) {
        runCatching {
            withContext(Dispatchers.Default) {
                withTimeout(20_000) {
                    database
                        .mediaRecommendationConnectionDao()
                        .entriesByMediaIdFlow(X)
                        .map { rows -> rows.map { it.entryId }.sorted() }
                        .first(predicate)
                }
            }
        }
    }

    private fun recommendationsSource(): MediaConnectionSource.Recommendations {
        val mockServer = MockWebServer()
        mockServer.start()
        server = mockServer
        val endpoint =
            object : IEndpointType {
                override val url: HttpUrl = mockServer.url("/")
            }
        GlobalContext.get().loadModules(
            listOf(
                module {
                    single<IRefreshBehaviourSettings> { mockk(relaxed = true) }
                    factory<MediaRecommendationMapper> {
                        MediaRecommendationMapper(
                            localSource = database.mediaRecommendationConnectionDao(),
                            converter = MediaConverter(),
                        )
                    }
                    single<AniListApiFactory> {
                        mockk {
                            every { endpointType } returns endpoint
                            every { okHttpConfig(any()) } returns OkHttpClient()
                        }
                    }
                    single {
                        NetworkMessage(
                            unrecoverableErrorTittle = "Unrecoverable",
                            connectivityErrorTittle = "Connectivity",
                            connectivityErrorMessage = "Network unavailable",
                        )
                    }
                },
            ),
            allowOverride = true,
        )
        return get<MediaConnectionSource.Recommendations>()
    }

    private companion object {
        const val X = 940_001L
        const val Y = 940_002L
        const val A = 941_001L
        const val B = 941_002L
        const val C = 941_003L

        const val RESPONSE_B_ONLY =
            """{"data":{"Media":{"id":940001,"recommendations":{"edges":[{"node":{"id":941002,"rating":1,"mediaRecommendation":{"id":942002,"type":"ANIME","isReviewBlocked":false,"favourites":0,"siteUrl":"https://anilist.co/anime/942002","title":{"romaji":"Target B"}}}}]}}}}"""

        const val RESPONSE_EMPTY =
            """{"data":{"Media":{"id":940001,"recommendations":{"edges":[]}}}}"""
    }
}

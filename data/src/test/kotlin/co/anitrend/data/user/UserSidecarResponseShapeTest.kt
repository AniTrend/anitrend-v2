package co.anitrend.data.user

import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.serialization.json.Json

/**
 * Pins the GetUserProfileOverview, GetUserProfileFeed and GetUserWithStatistic
 * response shapes to their container models.
 *
 * The fixtures mirror the selection keys of the graphql fragments
 * (UserProfileMediaPreview, UserProfileListActivity, UserProfileReviewPreview,
 * UserCore, UserAnimeStatistics, UserMangaStatistics) exactly. Decoding runs
 * with unknown keys rejected, so if a model grows a required field that the
 * fragments do not select, decoding fails here instead of silently dropping
 * every profile sidecar row at runtime.
 */
class UserSidecarResponseShapeTest {
    private val json = Json {
        ignoreUnknownKeys = false
        coerceInputValues = true
        isLenient = true
    }

    // Selection of fragments UserProfileMediaPreview + MediaTitle + MediaImage
    private val mediaNode =
        """
        {
            "id": 21450,
            "title": {
                "english": "JoJo",
                "romaji": "JoJo",
                "native": null,
                "userPreferred": "JoJo"
            },
            "coverImage": {
                "color": "#ffffff",
                "extraLarge": "x",
                "large": "x",
                "medium": "x"
            },
            "type": "ANIME",
            "format": "TV",
            "status": "FINISHED",
            "episodes": 39,
            "chapters": null,
            "volumes": null,
            "isFavourite": true,
            "isReviewBlocked": false,
            "meanScore": 85,
            "averageScore": 85,
            "favourites": 42,
            "siteUrl": "https://anilist.co/anime/21450",
            "mediaListEntry": {
                "id": 710101,
                "mediaId": 21450,
                "createdAt": 1700000000,
                "user": {
                    "id": 92058,
                    "name": "AniTrend",
                    "siteUrl": "https://anilist.co/user/AniTrend"
                },
                "status": "COMPLETED",
                "progress": 39,
                "progressVolumes": null,
                "updatedAt": 1700000001
            }
        }
        """.trimIndent()

    // Selection of fragment UserProfileListActivity
    private val listActivity =
        """
        {
            "id": 1163474123,
            "createdAt": 1790197647,
            "status": "plans to watch",
            "progress": null,
            "siteUrl": "https://anilist.co/activity/1163474123",
            "type": "ANIME_LIST",
            "media": $mediaNode
        }
        """.trimIndent()

    @Test
    fun overviewResponseDecodesFavouritesAndActivities() {
        val data =
            """
            {
                "User": {
                    "id": 92058,
                    "favourites": {
                        "anime": {
                            "edges": [
                                { "favouriteOrder": 1, "node": $mediaNode }
                            ]
                        },
                        "manga": { "edges": [] }
                    }
                },
                "Page": {
                    "activities": [ $listActivity ]
                }
            }
            """.trimIndent()

        val parsed = json.decodeFromString<UserSidecarModelContainer.Overview>(data)

        val edges = parsed.user?.favourites?.anime?.edges.orEmpty()
        assertEquals(1, edges.size)
        assertEquals(1, edges.first().favouriteOrder)
        assertEquals(21450L, edges.first().node?.id)
        assertEquals(92058L, edges.first().node?.mediaListEntry?.user?.id)
        assertEquals(1, parsed.page?.activities?.size)
        assertEquals("plans to watch", parsed.page?.activities?.first()?.status)
    }

    @Test
    fun feedResponseDecodesReviewsAndListActivities() {
        val review =
            """
            {
                "id": 877001,
                "summary": "Great",
                "score": 80,
                "rating": 5,
                "ratingAmount": 12,
                "siteUrl": "https://anilist.co/review/877001",
                "createdAt": 1,
                "updatedAt": 2,
                "mediaId": 21450,
                "mediaType": "ANIME",
                "media": $mediaNode
            }
            """.trimIndent()
        val data =
            """
            {
                "User": { "id": 92058 },
                "reviewPage": { "reviews": [ $review ] },
                "activityPage": { "listActivity": [ $listActivity ] }
            }
            """.trimIndent()

        val parsed = json.decodeFromString<UserSidecarModelContainer.Feed>(data)

        val reviews = parsed.reviewPage?.reviews.orEmpty()
        assertEquals(1, reviews.size)
        assertEquals(21450L, reviews.first().mediaId)
        assertEquals(92058L, reviews.first().media?.mediaListEntry?.user?.id)
        assertEquals(1, parsed.activityPage?.listActivity?.size)
        assertEquals(1163474123L, parsed.activityPage?.listActivity?.first()?.id)
    }

    @Test
    fun statisticResponseDecodesSummaryAndDimensions() {
        // Selection of fragment UserCore + UserAnimeStatistics + UserMangaStatistics
        val data =
            """
            {
                "User": {
                    "id": 92058,
                    "name": "AniTrend",
                    "about": null,
                    "avatar": null,
                    "bannerImage": null,
                    "isFollowing": false,
                    "isFollower": false,
                    "isBlocked": false,
                    "donatorTier": 1,
                    "donatorBadge": "Donator",
                    "siteUrl": "https://anilist.co/user/AniTrend",
                    "updatedAt": 1,
                    "createdAt": 0,
                    "statistics": {
                        "anime": {
                            "count": 80,
                            "meanScore": 67.4,
                            "episodesWatched": 2319,
                            "minutesWatched": 54676,
                            "standardDeviation": 12.8,
                            "countries": [
                                {
                                    "country": "JP",
                                    "minutesWatched": 54676,
                                    "count": 80,
                                    "meanScore": 67.4,
                                    "mediaIds": [20954]
                                }
                            ],
                            "formats": [
                                {
                                    "format": "TV",
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "genres": [
                                {
                                    "genre": "Action",
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "lengths": [
                                {
                                    "length": "1-17",
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "releaseYears": [
                                {
                                    "releaseYear": 2013,
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "scores": [
                                {
                                    "score": 95,
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "staff": [
                                {
                                    "staff": $staffNode,
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "startYears": [
                                {
                                    "startYear": 2013,
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "statuses": [
                                {
                                    "status": "COMPLETED",
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "studios": [
                                {
                                    "studio": {
                                        "favourites": 23525,
                                        "id": 21,
                                        "isAnimationStudio": true,
                                        "isFavourite": false,
                                        "name": "Kyoto Animation",
                                        "siteUrl": "https://anilist.co/studio/21"
                                    },
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "tags": [
                                {
                                    "tag": {
                                        "category": "Theme-Other",
                                        "description": "Set during wartime.",
                                        "id": 111,
                                        "isAdult": false,
                                        "isGeneralSpoiler": false,
                                        "name": "War"
                                    },
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ],
                            "voiceActors": [
                                {
                                    "voiceActor": $staffNode,
                                    "minutesWatched": 1,
                                    "count": 1,
                                    "meanScore": 1.0,
                                    "mediaIds": [21450]
                                }
                            ]
                        },
                        "manga": {
                            "count": 15,
                            "meanScore": 70.0,
                            "chaptersRead": 1200,
                            "volumesRead": 300,
                            "standardDeviation": 10.0,
                            "countries": [
                                {
                                    "country": "JP",
                                    "chaptersRead": 1200,
                                    "count": 15,
                                    "meanScore": 70.0,
                                    "mediaIds": [30010]
                                }
                            ],
                            "formats": [],
                            "genres": [],
                            "lengths": [],
                            "releaseYears": [],
                            "scores": [],
                            "staff": [],
                            "startYears": [],
                            "statuses": [],
                            "studios": [],
                            "tags": [],
                            "voiceActors": []
                        }
                    }
                }
            }
            """.trimIndent()

        val parsed = json.decodeFromString<UserModelContainer.WithStatistic>(data)

        val statistics = assertNotNull(parsed.user.statistics)
        assertEquals(80, statistics.anime.count)
        assertEquals(15, statistics.manga.count)
        assertEquals("JP", statistics.anime.countries?.first()?.country)
        assertEquals("War", statistics.anime.tags?.first()?.tag?.name)
        assertEquals(21L, statistics.anime.studios?.first()?.studio?.id)
        assertEquals(97007L, statistics.anime.staff?.first()?.staff?.id)
    }

    private companion object {
        // Selection of fragment StaffCore
        val staffNode =
            """
            {
                "age": null,
                "dateOfBirth": null,
                "dateOfDeath": null,
                "description": null,
                "favourites": 12,
                "gender": null,
                "homeTown": null,
                "id": 97007,
                "image": null,
                "isFavourite": false,
                "isFavouriteBlocked": false,
                "languageV2": null,
                "name": null,
                "primaryOccupations": [],
                "siteUrl": "https://anilist.co/staff/97007",
                "updatedAt": 1,
                "yearsActive": []
            }
            """.trimIndent()
    }
}

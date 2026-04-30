/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.mapper

import co.anitrend.data.android.mapper.PersistEmbedded

internal fun interface UserProfileOverviewWriterContract {
    suspend fun persist()
}

internal fun interface UserProfileFeedWriterContract {
    suspend fun persist()
}

internal class UserProfileOverviewWriter(
    private val mediaPersistence: PersistEmbedded,
    private val favouritePersistence: PersistEmbedded,
    private val statusPersistence: PersistEmbedded,
) : UserProfileOverviewWriterContract {
    override suspend fun persist() {
        mediaPersistence.persistEmbedded()
        favouritePersistence.persistEmbedded()
        statusPersistence.persistEmbedded()
    }
}

internal class UserProfileFeedWriter(
    private val mediaPersistence: PersistEmbedded,
    private val reviewPreviewPersistence: PersistEmbedded,
    private val reviewConnectionPersistence: PersistEmbedded,
    private val statusPersistence: PersistEmbedded,
) : UserProfileFeedWriterContract {
    override suspend fun persist() {
        mediaPersistence.persistEmbedded()
        reviewPreviewPersistence.persistEmbedded()
        reviewConnectionPersistence.persistEmbedded()
        statusPersistence.persistEmbedded()
    }
}

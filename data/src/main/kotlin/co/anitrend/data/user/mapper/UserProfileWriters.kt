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

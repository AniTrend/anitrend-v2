/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.presenter

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.anitrend.core.worker.MediaGenreCollectionWorker
import co.anitrend.core.worker.MediaTagCollectionWorker
import co.anitrend.data.util.Settings
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.core.presenter.SupportPresenter
import java.util.concurrent.TimeUnit

class CorePresenter(
    context: Context,
    settings: Settings
): SupportPresenter<Settings>(context, settings) {

    /**
     * Invokes works to run in the background to fetch genres and tags
     */
    fun syncMediaGenresAndTags() {
        val genreCollectionWorker = OneTimeWorkRequestBuilder<MediaGenreCollectionWorker>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .addTag(MediaGenreCollectionWorker.TAG)
            .build()

        val tagCollectionWorker = OneTimeWorkRequestBuilder<MediaTagCollectionWorker>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .addTag(MediaTagCollectionWorker.TAG)
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(
                GENRE_TAG_SYNC_WORKERS,
                ExistingWorkPolicy.KEEP,
                listOf(
                    genreCollectionWorker,
                    tagCollectionWorker
                )
            ).enqueue()
    }

    companion object {
        private const val GENRE_TAG_SYNC_WORKERS = "GENRE_TAG_SYNC_WORKERS"
    }
}
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
    context: Context?,
    settings: Settings
): SupportPresenter<Settings>(context, settings) {

    /**
     * Invokes works to run in the background to fetch genres and tags
     */
    fun syncMediaGenresAndTags() {
        context?.also {

            val genreCollectionWorker = OneTimeWorkRequestBuilder<MediaGenreCollectionWorker>()
                .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                .addTag(MediaGenreCollectionWorker.TAG)
                .build()

            val tagCollectionWorker = OneTimeWorkRequestBuilder<MediaTagCollectionWorker>()
                .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                .addTag(MediaTagCollectionWorker.TAG)
                .build()

            WorkManager.getInstance(it)
                .beginUniqueWork(
                    GENRE_TAG_SYNC_WORKERS,
                    ExistingWorkPolicy.KEEP,
                    listOf(
                        genreCollectionWorker,
                        tagCollectionWorker
                    )
                ).enqueue()
        }
    }

    companion object {
        private const val GENRE_TAG_SYNC_WORKERS = "GENRE_TAG_SYNC_WORKERS"
    }
}
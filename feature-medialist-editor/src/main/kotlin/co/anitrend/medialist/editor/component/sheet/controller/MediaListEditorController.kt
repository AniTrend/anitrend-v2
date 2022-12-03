/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.medialist.editor.component.sheet.controller

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.common.medialist.ui.widget.counter.model.CounterEditModel
import co.anitrend.common.medialist.ui.widget.progress.model.ProgressEditModel
import co.anitrend.common.medialist.ui.widget.score.model.ScoreEditModel
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.entity.base.IMediaList
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.medialist.editor.databinding.MediaListEditorContentBinding
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import coil.request.Disposable
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

class MediaListEditorController(
    private val settings: IUserSettings
) : SupportLifecycle {

    private var disposable: Disposable? = null

    private var param by Delegates.notNull<MediaListTaskRouter.Param.SaveEntry>()

    private fun initializeParams(media: Media) {
        param = MediaListTaskRouter.Param.SaveEntry(
            id = media.mediaList?.id,
            mediaId = media.id,
            status = media.mediaList?.status ?: MediaListStatus.PLANNING,
            scoreFormat = settings.scoreFormat.value,
            score = media.mediaList?.score,
            scoreRaw = null,
            progress = media.mediaList?.progress?.progress,
            progressVolumes = when (val progress = media.mediaList?.progress) {
                is MediaListProgress.Manga -> progress.volumeProgress
                else -> null
            },
            repeat = media.mediaList?.progress?.repeated,
            priority = media.mediaList?.priority,
            private = media.mediaList?.privacy?.isPrivate,
            notes = media.mediaList?.privacy?.notes?.toString(),
            hiddenFromStatusLists = media.mediaList?.privacy?.isHidden,
            customLists = media.mediaList?.customLists?.map { it.name.toString() },
            advancedScores = media.mediaList?.advancedScores?.map(IMediaList.IAdvancedScore::score),
            startedAt = media.mediaList?.startedOn,
            completedAt = media.mediaList?.finishedOn,
        )
    }

    private fun bindModel(
        media: Media,
        binding: MediaListEditorContentBinding
    ) {
        disposable = binding.mediaImage.using(
            media.image.toMediaRequestImage(
                RequestImage.Media.ImageType.POSTER
            ),
            listOf(RoundedCornersTransformation(6f.dp))
        )
        binding.mediaStatusWidget.setBackgroundUsing(media.status)
        binding.mediaTitle.text = media.title.userPreferred
        binding.mediaSubTitleWidget.setUpSubTitle(media)
        binding.mediaScheduleTitleWidget.setUpAiringSchedule(media)

        binding.mediaListProgress.setUpUsing(
            ProgressEditModel(
                current = media.mediaList?.progress?.progress ?: 0,
                maximum = media.category.total
            )
        )

        when (val category = media.category) {
            is Media.Category.Anime -> {
                binding.mediaListProgressVolumes.gone()
                binding.mediaListProgressVolumesLabel.gone()
            }
            is Media.Category.Manga -> {
                binding.mediaListProgressVolumes.visible()
                binding.mediaListProgressVolumesLabel.visible()

                val progress = media.mediaList?.progress as? MediaListProgress.Manga
                binding.mediaListProgressVolumes.setUpUsing(
                    ProgressEditModel(
                        current = progress?.volumeProgress ?: 0,
                        maximum = category.volumes
                    )
                )
            }
        }

        binding.mediaListFuzzyDateStart.setUpUsing(
            media.mediaList?.startedOn
        )
        binding.mediaListFuzzyDateEnd.setUpUsing(
            media.mediaList?.finishedOn
        )

        binding.mediaListStatusSpinner.setCurrentSelection(
            media.mediaList?.status ?: MediaListStatus.PLANNING
        )

        binding.mediaListScore.setUpUsing(
            ScoreEditModel(
                media.mediaList?.score ?: 0f
            ),
            settings
        )

        binding.mediaListRepeated.setUpUsing(
            CounterEditModel(
                media.mediaList?.progress?.repeated ?: 0
            )
        )

        binding.mediaListNotes.setText(
            media.mediaList?.privacy?.notes
        )
    }

    private fun observeChanges(
        binding: MediaListEditorContentBinding,
        lifecycleScope: LifecycleCoroutineScope
    ) {
        lifecycleScope.launch {
            binding.mediaListStatusSpinner
                .selectionChangeFlow
                .onEach {
                    param.status = it
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListScore
                .currentEditableFlow
                .onEach {
                    param.score = it.toFloat()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListProgress
                .currentEditableFlow
                .onEach {
                    param.progress = it.toInt()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListProgressVolumes
                .currentEditableFlow
                .onEach {
                    param.progressVolumes = it.toInt()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListRepeated
                .currentEditableFlow
                .onEach {
                    param.repeat = it.toInt()
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListFuzzyDateStart
                .dateChangeFlow
                .onEach {
                    param.startedAt = it
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListFuzzyDateEnd
                .dateChangeFlow
                .onEach {
                    param.completedAt = it
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launch {
            binding.mediaListNotes
                .editableFlow
                .onEach {
                    param.notes = it
                }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
    }

    fun onPostModelChange(
        media: Media,
        binding: MediaListEditorContentBinding,
        lifecycleScope: LifecycleCoroutineScope,
        onActionCompletedCallback: () -> Unit
    ) {
        initializeParams(media)
        bindModel(media, binding)
        observeChanges(binding, lifecycleScope)

        if (param.id == null)
            binding.mediaListActionDelete.gone()
        else
            binding.mediaListActionDelete.visible()

        binding.mediaListActionDelete.setOnClickListener {
            MediaListTaskRouter.forMediaListDeleteEntryWorker()
                .createOneTimeUniqueWorker(
                    it.context,
                    MediaListTaskRouter.Param.DeleteEntry(
                        requireNotNull(param.id)
                    )
                ).enqueue()
        }

        binding.mediaListActionSave.setOnClickListener {
            MediaListTaskRouter.forMediaListSaveEntryWorker()
                .createOneTimeUniqueWorker(it.context, param)
                .enqueue()
            onActionCompletedCallback()
        }
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable?.dispose()
        disposable = null
    }
}